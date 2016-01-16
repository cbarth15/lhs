all: qxt.exe

CL=cl -EHsc

ifdef UNIX
CL=g++ -Wall -o $@ -std=c++14 -Dto_string=my_string
endif

qxt.exe: qxt.cpp
	$(CL) $<

clean:
	rm -fr *.obj *.o qxt.exe
