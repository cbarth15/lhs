#!/bin/bash

x=1

while [ $x -le 5 ]
do

	make #> a.out.tmp	commented out to remove build output
	./game >> a.out.tmp
	cat a.out.tmp > a.out
	make clean >> a.out.tmp
	rm -rf a.out.tmp
	cd javaCode
	java Launcher
	if [ $? != 4 ]
	then
		x=100
	fi
	cd ..
done	
exit 0
