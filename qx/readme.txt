Solution to QxBranch problem

How to build
- make              to use MS cl compiler
- make GCC=1        to use GCC

How to run one case
- ?

How to run tests
- Run 'run.sh' inside 'tests'

Disign decisions
- Overall design is simplistic - simple algorithmic problem
- Use one source file - easy for another person to build and to run
- No dependencies on other languages and tools, except C++
- Use std::string for exceptions - amount of code
- Use simple sh script for tests - easy to inspect

