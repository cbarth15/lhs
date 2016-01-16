all: qxt.exe

CL=cl -EHsc

ifdef UNIX
CL=g++ -o $@
endif

qxt.exe: qxt.cpp
	$(CL) $<

clean:
	rm -fr *.obj *.o *.exe
