Solution to QxBranch problem
----------------------------

How to build
------------
- make              to use MS cl compiler
- make GCC=1        to use GCC
- compile qxt.cpp   using any C++14 compiler

How to run executable
---------------------
Prepare a file with a list of edges, one edge per line
For example, a file names 'f.inp' containing '0 1'.
Run 'qxt f.inp'. This will create a file 'f.inp.isakov'.
Run 'isakov -l f.inp.isakov -s 100 -r 1000'.
Grab the output from the previous command and feed 
it back to 'qxt -r' as stdin.

How to run tests
----------------
- Run 'run.sh' inside 'tests' to run all tests
- Run 'run.sh file.inp' to run only one test case
- Run 'run.sh clean' to clean outputs without fixing failing tests

Design decisions
----------------
- Overall design is simplistic; because it is a simple algorithmic problem
- Use one source file; to make it easy for another person to build and to run
- No dependencies on other languages and tools, except C++
- Use std::string for exceptions; to save some extra lines of code
- Use simple sh script for tests; easy to inspect and modify
- Brevity over efficiency; because main calculations are outside
- Linear code (no structure or classes); because algorithmic

Random graph generator
----------------------
Folder 'random' has a simple program to generate random graphs.
The size and connection density are two 'const int' parameters
declared at the beginning of the file.
Such generator can be used for more elaborate extensive testing.









..................
Oleg Mazonka, 2016
