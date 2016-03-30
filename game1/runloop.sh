#!/bin/bash

if [ -t 0 ]; then stty -echo -icanon -icrnl time 0 min 0; fi

count=0
keypress=''
echo "Press any key to stop the game"
while [ "x$keypress" = "x" ]; do
  let count+=1
  #echo -ne $count'\r'
  keypress="`cat -v`"

  make > a.out.tmp
  ./game >> a.out.tmp
  cat a.out.tmp > a.out
  sleep 3s
  
done

if [ -t 0 ]; then stty sane; fi

#echo "You pressed '$keypress' after $count loop iterations"
rm -rf a.out.tmp
make clean
exit 0