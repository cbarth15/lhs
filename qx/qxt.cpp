// This program coverts a list of node edges
// into a list of Ising coefficeints with
// Hamiltonian H = sum_ij (2-e_ij) s_i s_j,
// where e_ij=1 if edge exists between i and j,
// and e_ij=0 otherwise.
// Input file is a list of edges defined as
// two numbers (indices i j) per line
// Comments (#) and empty lines allowed.
// Repetitions are not allowed.
// Standalone node can be defined as edge to itself.

#include <iostream>

void usage()
{
    std::cout << "Usage: 'qxt input_file' or 'qxt -r'";
}

int main(int ac, char * av[])
{
    if ( ac < 2 )
    {
        usage();
        return 0;
    }
}
