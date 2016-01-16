#!/bin/sh

qx=../qxt.exe
isak=../isakov_win.exe

if [ "$1" = "clean" ]; then
rm -f *.out
exit
fi


run()
{
i=$1

	echo -n $i
	rm -f $i.out
	$qx $i 2> $i.err && $isak -l $i.isakov -s 100 -r 1000 | $qx -r 1> $i.out
	cat $i.err >> $i.out
	dos2unix $i.out 2> /dev/null
	#rm -f $i.err $i.isakov
	
	if cmp $i.gold $i.out 2> /dev/null > /dev/null
	then
		echo " OK"
		rm -f $i.out $i.err $i.isakov
	else
		echo " ERROR"
		cntr=$((cntr+1))
	fi
}

if [ "$1" = "" ]; then
	cntr=0
	for i in *.inp
	do
	run $i
	done
	echo "number of errors: $cntr"
	exit
fi

run $1
