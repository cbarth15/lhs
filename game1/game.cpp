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
#include <ctime>

using std::string;
using std::cout;


class Rnd
{
        std::default_random_engine reng;	//reng is from random engine
						//generator class now

        std::uniform_real_distribution<double> dist;//produces random
						//doubles within a range

        std::function<double()> rnd;//used as an object for containing bind

    public:

        static unsigned long seed;//a random number for auto-generation


	//initialize constructor that feeds in information to these function
	//s to set things up
        Rnd(): reng(seed), dist(0, 1), rnd( std::bind(dist, reng) ) {}
	//^bind is used to pass reng to dist and name it as rnd 


        double operator()() { return rnd(); }//when an object is created
						//rnd is ran
	

        void reseed(unsigned long s) { reng.seed(s); }// function to
							//make sure
							//things are random
};

unsigned long Rnd::seed = 0;	//initialize seed
Rnd * rnd = nullptr;		//initialize rnd

//made to mark potion for each soldier
struct Pos
{
    int x, y;
    Pos() {}
    Pos(int a, int b): x(a), y(b) {}
    Pos(string);
    string str() const;
	string output()
	{
	string out;
	out=std::to_string(x)+"\n"+std::to_string(y);
	return out;
	}
};

//if something is equal then the positions must be the same
bool operator==(const Pos & a, const Pos & b) { return a.x == b.x && a.y == b.y; }

//if something is not equal then the positions must be different
bool operator!=(const Pos & a, const Pos & b) { return !(a == b); }
std::ostream & operator<<(std::ostream & o, const Pos & a) { return o << a.str(); }

//function to calculate distance
double dist2(Pos a, Pos b)
{
    double x = a.x - b.x;
    double y = a.y - b.y;
    return x * x + y * y;
}

class Field;

//holds all info for each piece
class Soldier
{
        Pos pos;
        Pos next;
        double accuracy;
        double stealth;
        double speed;
        int fear = 1;

        bool dead = false;
        bool dying = false;

        char team;
        int name;

        friend class Field;
	//definition for Soldier initializer
        Soldier(Pos b, double a, double s, double v, int f, char t, int n):
            pos(b), accuracy(a), stealth(s), speed(v), fear(f), team(t), name(n) {}

        string nm() const { return string() + team + char('A' + char(name > 25 ? 25 : name)); }

        string move(Pos sz, Pos b, const std::vector<Soldier> & enemies, bool prn_move);

        string shoot(std::vector<Soldier> & enemies, bool prn_shoot);
};

class Field
{
        Pos baseB;	//location of Blue base
        Pos baseR;	//location of Red base
        std::vector<Soldier> blues;
        std::vector<Soldier> reds;
        int turn = 0;
        int rep = 0;
	// all these functions defined elsewhere
        void init(string filename); //takes in a game.config and adjusts accordingly
        bool arein(Pos base, const std::vector<Soldier> & s) const;
        bool alldead(const std::vector<Soldier> & s) const;
        int survived(const std::vector<Soldier> & s) const;

    public:

        Pos size;
        int prn_map = 1;
        int prn_result = 1;
        int prn_title = 1;
        int prn_move = 1;
        int prn_shoot = 1;

        int Nrep = 1;
        void reset();
	//initializer opens and grabs info from config file
        Field(string filename) { init(filename); }
	
	//functions declared and redefined elsewhere
        string title() const;
        string map() const;

        string move();
        string shoot();

        int isdone() const; // 0 go on; 1 - blues; 2 - reds; 3 draw
        string result(string = "") const;

	//how many reds or blues are alive
        int aliveR() const { return survived(reds); }
        int aliveB() const { return survived(blues); }
};

int main(int ac, char * av[])
try
{
	//initializes all variables from conf file
    Field field("game.conf");

	//reassign rnd to a new object of Rnd
	//makes a random double between 0 and 1
    Rnd rnd_main; rnd = &rnd_main;

	int stat[5] = {0,0,0,0,0};		//stores game statistics

	//Nrep is number of repetitions
						//This is where the game actually runs
    for ( int k = 0; k < field.Nrep; k++ )
    {
        field.reset();
	cout<<field.size.output()<<"\n";//added
			//gives java code field dimensions
        cout << field.title();

	int done=0;		

	//breaks out of for loop
        for ( int i = 0; i < 10000; i++ )
        {
            cout << field.move();
            cout << field.map();

            int ar = field.aliveR();
            int ab = field.aliveB();

            string kills = field.shoot();
		//if statement does not appear to be
		//used
            if ( !kills.empty() )
            {
                cout << kills << '\n';

                cout << "Killed/Survived: "
                     << (ab - field.aliveB()) << "/" << (field.aliveB()) << " blues, "
                     << (ar - field.aliveR()) << "/" << (field.aliveR()) << " reds\n";

                cout << field.map();
            }

			done = field.isdone();
            if ( done ) break;
        }

        cout << field.result("game.log");
		stat[done]++;

    }

	cout<<"Not finished  : "<<stat[0]<<'\n';
	cout<<"Won by Blues  : "<<stat[1]<<'\n';
	cout<<"Won by Reds   : "<<stat[2]<<'\n';
	cout<<"Draw all dead : "<<stat[3]<<'\n';
	cout<<"Draw both win : "<<stat[4]<<'\n';
}	//end of try
catch (string e)
{
    cout << "Error: " << e << '\n';
    return 1;
}
catch (...)
{
    cout << "Error\n";
    return 2;
}
//function handles the creation 
//of the field
void Field::init(string filename)
{
    std::ifstream in(filename.c_str());

    if ( !in )
        throw "Cannot open " + filename;

    std::map<string, string> dict;

    int mode = 0; // switches between arrays storing info according to 0-none, 1-blue, 2-red

    size = Pos(-1, -1);
    Pos base[3];
    int ns[3] = {0, 0, 0};
    double acc[3];
    double stl[3];
    double vel[3];
    double fea[3];

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
	//giving values to variables for field object
        if (0);
        else if ( k[0] == '#' );
        else if ( k[0] == '$' ) dict[k] = v;
        else if ( k == "[blue]" ) mode = 1;
        else if ( k == "[red]" ) mode = 2;
        else if ( k == "field" ) size = Pos(v);
        else if ( k == "base" ) base[mode] = Pos(v);
        else if ( k == "N" ) ns[mode] = std::atoi(v.c_str());
        else if ( k == "accuracy" ) acc[mode] = std::atof(v.c_str());
        else if ( k == "stealth" ) stl[mode] = std::atof(v.c_str());
        else if ( k == "speed" ) vel[mode] = std::atof(v.c_str());
        else if ( k == "fear" ) fea[mode] = std::atof(v.c_str());
	else if( k == "seed" )
	{ 
	std::srand(std::time(nullptr));
         Rnd::seed=std::rand();	//= std::atol(v.c_str());
	}
        else if ( k == "nrep" ) Nrep = std::atol(v.c_str());

        else if ( k == "prn_map" ) prn_map = std::atol(v.c_str());
        else if ( k == "prn_result" ) prn_result = std::atol(v.c_str());
        else if ( k == "prn_title" ) prn_title = std::atol(v.c_str());
        else if ( k == "prn_move" ) prn_move = std::atol(v.c_str());
        else if ( k == "prn_shoot" ) prn_shoot = std::atol(v.c_str());

        else
            throw "Unexpected [" + k + "] in " + filename;
    }

    if ( size.x < 3 ) throw "Field size undefined in " + filename;
//base location
    baseB = base[1];
    baseR = base[2];
//assigning values to each soldier
    for ( int i = 0; i < ns[1]; i++ )
        blues.push_back(Soldier(baseB, acc[1], stl[1], vel[1], fea[1], 'B', i));

    for ( int i = 0; i < ns[2]; i++ )
        reds.push_back(Soldier(baseR, acc[2], stl[2], vel[2], fea[2], 'R', i));
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

//take in new position
Pos::Pos(string s)
{
    std::istringstream i(s);
    char a;
    i >> x >> a >> y;
}

//output coordinates
string Pos::str() const
{
    std::ostringstream o;
    o << x << ',' << y;
    return o.str();
}

//outputs information about each team
string Field::title() const
{
    if( !prn_title ) return "";

    std::ostringstream o;

    o<<"rep="<<rep;

    o << " size=" << size.str() << " baseB=[" << baseB.str() << "] baseR=[" << baseR.str() << "]";
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
	//if not printing map return nothing
    if( !prn_map ) return "";

    std::ostringstream o;
	//printing the border
    for ( int i = 0; i < size.x; i++ ) o << "--"; o << '\n';

    //B R M ! a-z #
    std::vector<string> v;
    for ( int j = 0; j < size.y; j++ )
        for ( int i = 0; i < size.x; i++ )
            v.push_back("  ");

    Pos sz = size;
	//c is the map
    auto c = [&v, sz](Pos z) -> string&
    {
        return v[(z.y - 1) * sz.x + (z.x - 1)];
    };
//the bases are B! and R!
    c(baseB) = "B!";
    c(baseR) = "R!";

    for ( int i = 0; i < blues.size(); i++ )
    {
        if ( blues[i].dead ) continue;//if blue is dead leave
        string & s = c(blues[i].pos);
        if ( s[0] == ' ' ) s = blues[i].nm();//move blue
        else if ( s[0] == 'B' ) s = "Bq";//blue is not moving
        else s = "M#";		//murdered?
    }

    for ( int i = 0; i < reds.size(); i++ )
    {
        if ( reds[i].dead ) continue;
        string & s = c(reds[i].pos);
        if ( s[0] == ' ' ) s = reds[i].nm();
        else if ( s[0] == 'R' ) s = "R#";
        else s = "M#";
    }

    for ( int j = 0; j < size.y; j++ )
    {
        for ( int i = 0; i < size.x; i++ )
            o << v[i + j * sz.x];
        o << '\n';
    }

    for ( int i = 0; i < size.x; i++ ) o << "--";
    o << '\n';
    return o.str();
}

string Field::move()
{
    turn++;

    std::ostringstream o;

//    blues[0].move(size,baseR,reds);
//calls the soldier's move
    for ( auto & i : blues ) o << i.move(size, baseR, reds, prn_move);
    for ( auto & i : reds ) o << i.move(size, baseB, blues, prn_move);
	//redefine the soldier's position
    for ( auto & i : blues ) i.pos = i.next;
    for ( auto & i : reds ) i.pos = i.next;

//returns the stream as a string
    return prn_move?o.str()+'\n':"";
}

string draw(std::vector<Pos> cells)
{
    int m = 0;
    int r = 0;
    for ( auto i : cells )
    {
        if ( i.x > m ) m = i.x;
        if ( i.y > r ) r = i.y;
    }

    std::vector<string> v;
    for ( int j = 0; j < r; j++ )
        for ( int i = 0; i < m; i++ )
            v.push_back("  ");

    auto c = [&v, m](Pos z) -> string& { return v[(z.y - 1) * m + (z.x - 1)]; };

    for ( auto i : cells )
        c(i) = "[]";

    std::ostringstream o;
    for ( int j = 0; j < r; j++ )
    {
        for ( int i = 0; i < m; i++ )
            o << v[i + j * m];
        o << '\n';
    }

    return o.str();
}

string Soldier::move(Pos sz, Pos base, const std::vector<Soldier> & enemies, bool prn_move)
{
    if ( dead )
    {
        next = pos;
        return "";
    }

    // first collect the list of all possible places

    std::vector<Pos> possible;
    double sp2 = speed * speed;
    for ( int j = pos.y - speed; j <= pos.y + speed; j++ )		//all y positions
        for ( int i = pos.x - speed; i <= pos.x + speed; i++ )		//all x positions
        {
            if ( i < 1 || j < 1 || i > sz.x || j > sz.y ) continue;	//as long as it is with-in the game borders

            double d2 = dist2(Pos(i, j), pos);//d2 holds the distance between where the soldier is
						//and where he wants to go

            if ( d2 > sp2 + 0.1 ) continue;

            possible.push_back(Pos(i, j));		//adding possible positions based on speed
        }

	//prob is the probability of the soldier pursuing each enemy
	//based on both accuracy and fear of the soldier
    std::vector<double> prob(possible.size());
    for ( int i = 0; i < possible.size(); i++ )
    {
        double p = 1;

        const double s2 = stealth * stealth;
        for ( auto j : enemies )		//enemies is a vector of the opposing color
        {
            if ( j.dead ) continue;
            double r2 = dist2(possible[i], j.pos);	//distance between soldier and enemy position
            p *= 1 - accuracy * std::exp( -r2 / s2 );
        }

        double q = 1;
        for ( int j = 0; j < fear; j++ ) q *= p;

        prob[i] = 1 - q;
    }

    std::vector<Pos> poss2;//if the random number between 0 and 1 
			//is greater than probability
			//then this is a possible move
    for ( int i = 0; i < possible.size(); i++ )
    {
        if ( prob[i] < (*rnd)() ) poss2.push_back(possible[i]);
    }

    //cout<<"AAA "<<possible.size()<<' '<<poss2.size()<<'\n';


    if ( poss2.empty() ) // if there are
    {			//no moves for the soldier
        next = pos;	//panic
        return "";
    }

    double rmin2 = 1e6;
    for ( auto i : poss2 )	//for each remaining possible position
    {
        double r2 = dist2(i, base);
        if ( r2 < rmin2 )
        {
            rmin2 = r2;
            next = i;		//take the move remaining
        }			//that brings the soldier closest to its base
    }

    if( !prn_move ) return "";

    std::ostringstream o;

    if ( next != pos )
        o << "(" << nm() << ":" << pos << ">" << next << ")";

    return o.str();
}


string Field::shoot()
{
    std::ostringstream o;
	//run shoot for each soldier
    for ( auto & i : blues ) o << i.shoot(reds, prn_shoot);
    for ( auto & i : reds ) o << i.shoot(blues, prn_shoot);

	//if dying was true, now dead is true
    for ( auto & i : blues ) i.dead = i.dying;
    for ( auto & i : reds ) i.dead = i.dying;

    return o.str();
}

string Soldier::shoot(std::vector<Soldier> & enemies, bool prn_shoot)
{
    if ( dead )
    {
        next = pos;
        return "";
    }

    std::ostringstream o;

    for ( auto & i : enemies)		//based on all of red team
    {
        if ( i.dead || i.dying ) continue;	//dying people can not shoot
        i.dying = false;

        double s2e = i.stealth; s2e *= s2e;	//stealth

        double r2 = dist2(pos, i.pos);	//distance from enemy
        double p = accuracy * std::exp( -r2 / s2e );	//calculated together

        if ( (*rnd)() > p ) continue; // if p is greater than the random number,
					//the enemy is killed
        if(prn_shoot) o << "(" << nm() << "/" << i.nm() << ")";
        i.dying = true;			//the enemy is now dying
    }

    return o.str();
}

int Field::isdone() const
{	
	//functions that check who is dead
    bool rdead = alldead(reds);		
    bool bdead = alldead(blues);

	//figuring out which or both teams have died
    if ( rdead && bdead ) return 3;
    if ( rdead && !bdead ) return 1;
    if ( !rdead && bdead ) return 2;

		//is anyone in the base
    bool rin = arein(baseB, reds);
    bool bin = arein(baseR, blues);

    if ( !rin && !bin ) return 0;
		//anyone of the opposite side in the other's base
    bool rdef = arein(baseR, reds);
    bool bdef = arein(baseB, blues);

    if ( rin && bin && !rdef && !bdef ) return 4;

    if ( rin && !bdef ) return 2;
    if ( bin && !rdef ) return 1;

    return 0;
}

bool Field::alldead(const std::vector<Soldier> & s) const
{
    for ( const auto & i : s )
        if ( !i.dead ) return false;		//if alive false

    return true;
}

//counting the survived soldier's
int Field::survived(const std::vector<Soldier> & s) const
{

    int k = 0;
    for ( const auto & i : s )
        if ( !i.dead ) k++;

    return k;
}

bool Field::arein(Pos base, const std::vector<Soldier> & s) const
{
    for ( const auto & i : s )	//for each soldier
    {
        if ( i.dead ) continue; //if soldier is dead go to next soldier
        if ( base == i.pos ) return true; //if a soldier is in base, true
    }

    return false;
}

//print everything at the end
string Field::result(string log) const
{
    int end = isdone();		//depending on what isdone returns

    std::ostringstream o;
    switch (end)		//match it with the case
    {
        case 0: o << "Result: Not finished\n"; break;
        case 1: o << "Result: Blues win\n"; break;
        case 2: o << "Result: Reds win\n"; break;
        case 3: o << "Result: Draw (all dead)\n"; break;
        case 4: o << "Result: Draw (both sides win)\n"; break;
        default:
            throw string() + "Internal error 525";
    }

    int alB = aliveB();
    int alR = aliveR();
    int casB = (blues.size() - alB);
    int casR = (reds.size() - alR);

    o << "Casualties Blues: " << casB << " of " << blues.size() << '\n';
    o << "Casualties Reds : " << casR << " of " << reds.size() << '\n';

    o << "Turns : " << turn << '\n';

    if ( !log.empty() )
    {
        std::ofstream of(log.c_str(), std::ios::app);

        string s = ",   ";
        of << Rnd::seed << s << rep << s << end << s << casB << s
           << alB << s << casR << s << alR << s << turn << '\n';
    }

    return prn_result?o.str():"";
}

//increase the repitions
//reset all soldiers
void Field::reset()
{
    rep++;
    turn=0;

    for ( auto & i : blues ) i.pos = i.next = baseB;
    for ( auto & i : reds ) i.pos = i.next = baseR;
    for ( auto & i : blues ) i.dead = i.dying = false;
    for ( auto & i : reds ) i.dead = i.dying = false;
}



