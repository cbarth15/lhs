// Simple generator of random graphs
// optional argument is seed - for repeatability

#include <iostream>
#include <ctime>
#include <functional>
#include <set>
#include <cstdlib>

const int ITER = 20; // number of iterations
const int DENS = 2; // Density factor; >=2

bool stop(int i, size_t r)
{
	return i>ITER;
}

bool increase(int i, size_t r)
{
	return r%DENS==0;
}

std::pair<int,int> ordpair(int x, int y)
{
	if( y<x ) std::swap(x,y);
	return std::make_pair(x,y);
}

int main(int ac, char *av[])
{
	size_t r = time(0);
	if( ac>1 ) r = std::atol(av[1]);

	std::cout<<"# seed "<<r<<'\n';

	std::set< std::pair<int,int> > edges;
	edges.insert(ordpair(0,1));

	int x = 1, mx=2;

	for( int i=0; i<2000; i++ )
	{
		r = std::hash<size_t>()(r);

		if( stop(i,r) && mx%2==0 ) break;

		if( increase(i,r) )
		{
			edges.insert(ordpair(x,mx));
			x = mx++;
			continue;
		}

		int y = r%mx;
		if( x!=y ) edges.insert(ordpair(x,y));
		x = y;
	}

	for( const auto & x : edges )
		std::cout<<x.first<<' '<<x.second<<'\n';
}
