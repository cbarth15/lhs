#!/bin/bash

x=1

	make #> a.out.tmp	commented out to remove build output
	./game >> a.out.tmp
	cat a.out.tmp > a.out
	make clean >> a.out.tmp
	rm -rf a.out.tmp
exit 0
