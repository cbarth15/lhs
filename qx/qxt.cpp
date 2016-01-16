// This program converts a list of node edges
// into a list of Ising coefficeints with
// Hamiltonian H = sum_ij (2-e_ij) s_i s_j,
// where e_ij=1 if the edge exists between i and j,
// and e_ij=0 otherwise.
// Input file is a list of edges defined as
// two numbers (indices i j) per line
// Comments (#) and empty lines are allowed.
// Repetitions are not allowed.
// Standalone node can be defined as edge to itself.

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>

using std::string;

void usage()
{
    std::cout << "Usage: 'qxt input_file' or 'qxt -r'";
}

int main(int ac, char * av[])
try
{
    if ( ac < 2 )
    {
        usage();
        return 0;
    }

	if( string(av[1])=="-r" )
	{
		int parse_result();
		return parse_result();
	}

	std::ifstream in(av[1]);
	int line_counter = 0;
	for( string line; std::getline(in,line); )
	{
	}
}
catch(std::exception &e){ std::cerr<<"Error: "<<e.what()<<'\n'; return 2; }
catch(...){ std::cerr<<"Unknown error\n"; return 1; }



int parse_result()
{
	std::vector<string> v;
	for( string line; std::getline(std::cin,line); )
		v.push_back(line);

	for( auto x : v )
		std::cout<<x<<'\n';

	return 0;
}
