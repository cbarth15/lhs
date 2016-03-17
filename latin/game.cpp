#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <map>
#include <cstdlib>
#include <random>
#include <algorithm>
#include <iterator>
#include <functional>
#include <cmath>


using std::string;


class Rnd
{
        std::default_random_engine reng;
        std::uniform_real_distribution<double> dist;
        std::function<double()> rnd;

    public:

        static unsigned long seed;

        Rnd(): reng(seed), dist(0, 1), rnd( std::bind(dist, reng) ) {}
        double operator()() { return rnd(); }
};
unsigned long Rnd::seed = 0;
Rnd rnd;

struct Pos
{
    int x, y;
    Pos() {}
    Pos(int a, int b): x(a), y(b) {}
    Pos(string);
    string str() const;
};

double dist2(Pos a, Pos b)
{
    double x = a.x-b.x;
    double y = a.y-b.y;
    return x*x+y*y;
}

class Field;
class Soldier
{
        Pos pos;
        Pos next;
        double accuracy;
        double stealth;
        double speed;
        friend class Field;

        Soldier(Pos b, double a, double s, double v): pos(b), accuracy(a), stealth(s), speed(v) {}
        void move(Pos sz, Pos b, const std::vector<Soldier> & enemies);
};

class Field
{
        Pos size;
        Pos baseB;
        Pos baseR;
        std::vector<Soldier> blues;
        std::vector<Soldier> reds;

        void init(string filename);

    public:
        Field(string filename) { init(filename); }
        string title() const;
        string map() const;

        void move();
        string shoot();
};

int main()
try
{
    Field field("game.conf");
    std::cout << field.title();

    for( int i=0; i<23; i++ )
    {
        field.move();
        std::cout << field.map();
    }

}
catch (string e)
{
    std::cout << "Error: " << e << '\n';
    return 1;
}
catch (...)
{
    std::cout << "Error\n";
    return 2;
}

void Field::init(string filename)
{
    std::ifstream in(filename.c_str());

    if ( !in )
        throw "Cannot open " + filename;

    std::map<string, string> dict;

    int mode = 0; // 0-none, 1-blue, 2-red

    size = Pos(-1, -1);
    Pos base[3];
    int ns[3] = {0, 0, 0};
    double acc[3];
    double stl[3];
    double vel[3];

    string s;
    for (; in >> s;)
    {
        std::istringstream in(s);
        string k, v;
        std::getline(in, k, '=');
        std::getline(in, v);

        if ( k.empty() ) throw "corrupted " + filename;

        for ( int i = 0; i < 100; i++ )
        {
            void replaceAll(string & s, const string & r, const string & to);
            for ( auto j : dict )
                replaceAll(v, j.first, j.second);
        }

        if (0);
        else if ( k[0] == '$' ) dict[k] = v;
        else if ( k == "[blue]" ) mode = 1;
        else if ( k == "[red]" ) mode = 2;
        else if ( k == "field" ) size = Pos(v);
        else if ( k == "base" ) base[mode] = Pos(v);
        else if ( k == "N" ) ns[mode] = std::atoi(v.c_str());
        else if ( k == "accuracy" ) acc[mode] = std::atof(v.c_str());
        else if ( k == "stealth" ) stl[mode] = std::atof(v.c_str());
        else if ( k == "speed" ) vel[mode] = std::atof(v.c_str());

        else
            throw "Unexpected [" + k + "] in " + filename;
    }

    if ( size.x < 3 ) throw "Field size undefined in " + filename;

    baseB = base[1];
    baseR = base[2];

    for ( int i = 0; i < ns[1]; i++ )
        blues.push_back(Soldier(baseB, acc[1], stl[1], vel[1]));

    for ( int i = 0; i < ns[2]; i++ )
        reds.push_back(Soldier(baseR, acc[2], stl[2], vel[2]));
}

void replaceAll(string & s, const string & r, const string & to)
{
    while (1)
    {
        size_t i = s.find(r);
        if ( i == string::npos ) return;
        s.replace(i, r.size(), to);
    }
}

Pos::Pos(string s)
{
    std::istringstream i(s);
    char a;
    i >> x >> a >> y;
}

string Pos::str() const
{
    std::ostringstream o;
    o << x << ',' << y;
    return o.str();
}

string Field::title() const
{
    std::ostringstream o;
    o << size.str() << " baseB=[" << baseB.str() << "] baseR=[" << baseR.str() << "]";
    if ( !blues.empty() )
    {
        o << " accB=" << blues[0].accuracy;
        o << " stlB=" << blues[0].stealth;
        o << " velB=" << blues[0].speed;
    }

    if ( !reds.empty() )
    {
        o << " accR=" << reds[0].accuracy;
        o << " stlR=" << reds[0].stealth;
        o << " velR=" << reds[0].speed;
    }

    o << '\n';

    return o.str();
}

string Field::map() const
{
    std::ostringstream o;

    ///for ( int i = 0; i < size.x; i++ ) o << "--"; o << '\n';

    //B R M ! a-z #
    std::vector<string> v;
    for ( int j = 0; j < size.y; j++ )
        for ( int i = 0; i < size.x; i++ )
            v.push_back("  ");

    Pos sz = size;
    auto c = [&v,sz](Pos z) -> string&
    {
        return v[(z.y-1)*sz.x+(z.x-1)];
    };

    c(baseB) = "B!";
    c(baseR) = "R!";

    for( int i =0; i<blues.size(); i++ )
    {
        char ch = 'a'+ char(i>25?25:i);
        string & s = c(blues[i].pos);
        if( s[0]==' ' ) s = string("B")+ch;
        else if( s[0]=='B' ) s = "B#";
        else s = "M#";
    }

    for( int i =0; i<reds.size(); i++ )
    {
        char ch = 'a'+ char(i>25?25:i);
        string & s = c(reds[i].pos);
        if( s[0]==' ' ) s = string("R")+ch;
        else if( s[0]=='R' ) s = "R#";
        else s = "M#";
    }

    for ( int j = 0; j < size.y; j++ )
    {
        for ( int i = 0; i < size.x; i++ )
            o<<v[i+j*sz.x];
        o<<'\n';
    }

    for ( int i = 0; i < size.x; i++ ) o << "--";
    o << '\n';
    return o.str();
}

void Field::move()
{
//    blues[0].move(size,baseR,reds);
    for( auto & i : blues ) i.move(size,baseR,reds);
    for( auto & i : reds ) i.move(size,baseB,blues);

    for( auto & i : blues ) i.pos = i.next;
    for( auto & i : reds ) i.pos = i.next;
}

string draw(std::vector<Pos> cells)
{
    int m = 0;
    int r = 0;
    for( auto i : cells )
    {
     if( i.x > m ) m = i.x;
     if( i.y > r ) r = i.y;
    }

    std::vector<string> v;
    for ( int j = 0; j < r; j++ )
        for ( int i = 0; i < m; i++ )
            v.push_back("  ");

    auto c = [&v,m](Pos z) -> string& { return v[(z.y-1)*m+(z.x-1)]; };

    for( auto i : cells )
        c(i) = "[]";    

    std::ostringstream o;
    for ( int j = 0; j < r; j++ )
    {
        for ( int i = 0; i < m; i++ )
            o<<v[i+j*m];
        o<<'\n';
    }

    return o.str();
}


void Soldier::move(Pos sz, Pos base, const std::vector<Soldier> & enemies)
{
    // first collect the list of all possible places

    double sp2 = speed*speed;

    // if base is close than speed, then do not consider going away
    double tobase = dist2(base,pos);
    double tob2 = tobase*tobase;
    if( tob2 < sp2 ) sp2 = tob2;

    std::vector<Pos> possible;
    for( int j = pos.y - speed; j<= pos.y+speed; j++ )
    for( int i = pos.x - speed; i<= pos.x+speed; i++ )
    {
        if( i<1 || j<1 || i>sz.x || j>sz.y ) continue;

        double d2 = dist2(Pos(i,j),pos);

        if( d2 > sp2 +0.1 ) continue;

        possible.push_back(Pos(i,j));
    }

    //std::cout<<"AAA "<<possible.size()<< ' ' <<speed <<'\n'<<draw(possible);
  
    double rmax2=0, rmin2=1e6;
    for( auto i : possible )
    {
        double r2 = dist2(i,base);
        if( r2>rmax2 ) rmax2=r2;
        if( r2<rmin2 ) rmin2=r2;
    }

    double rmax = std::sqrt(rmax2);
    double rmin = std::sqrt(rmin2);
    double delta = rmax-rmin;

    //std::cout<<"AAA "<<rmax<<' '<<rmin<<' '<<delta<<'\n';

    std::vector<double> prob(possible.size());
    for( int i=0; i<possible.size(); i++ )
    {
        double r = std::sqrt(dist2(possible[i],base));
        double p = (rmax-r)/delta;

        ///p = std::pow(p,4.0);

        for( auto j : enemies )
        {
            const double & s = stealth;
            p *= 1 - accuracy*std::exp( -r*r/(s*s) );
        }
    
        prob[i] = p;
        ///std::cout<<p<<'\n';
    }

    // normilize
    double sum =0;
    for( auto i : prob ) sum += i;
    for( auto &i : prob ) i /= sum;

    // normalize to max 1
    double pmax = 0;
    for( auto i : prob ) if( pmax<i ) pmax = i;
    for( auto &i : prob ) i /= pmax;

    ///for( auto i : prob ) std::cout<<i<<'\n';;

    for( int i=0; i<1000000; i++ )
    {
        double a = rnd();
        double b = rnd();
        a *= possible.size()+2;
        int idx = int(a);
        if( idx >= possible.size() ) continue;

        if( prob[idx] < b ) continue;

        next = possible[idx];
        return;
    }

    throw string()+"Cannot sample";
}













