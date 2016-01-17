all: qxt.exe

CL=cl -EHsc

ifdef wingcc
CL=g++ -Wall -o $@ -std=c++11 -Dto_string=my_string
endif

ifneq ($(OS),Windows_NT)
CL=g++ -Wall -o $@ -std=c++11
endif

qxt.exe: qxt.cpp
	$(CL) $<

clean:
	rm -fr *.obj *.o qxt.exe
