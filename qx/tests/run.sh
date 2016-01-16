#!/bin/sh

qx=../qxt.exe
isak=../isakov_win.exe

if [ "$1" = "clean" ]; then
rm -f *.out
exit
fi

cntr=0

for i in *.inp
do
echo -n $i
rm -f $i.out
$qx $i 2> $i.err && $isak -l $i.isakov -s 100 -r 1000 | $qx -r 1> $i.out
cat $i.err >> $i.out
#rm -f $i.err $i.isakov

if cmp $i.gold $i.out 2> /dev/null > /dev/null
then
echo " OK"
rm -f $i.out $i.err $i.isakov
else
echo " ERROR"
cntr=$((cntr+1))
fi

done

echo "number of errors: $cntr"

