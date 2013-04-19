#!/usr/bin/env bash

function test {
  "$@"
   status=$?
   if [ $status -ne 0 ]; then
     echo "error with $@"
     exit
   fi
   return $status
}

test play clean
test play test
test play publish

cd ~/code

if [ ! -d blopker.github.com ]; then
  git clone git@github.com:blopker/blopker.github.com.git
fi

#cp -rv ~/play/repository/local/com.blopker blopker.github.com/releases

cd ~/code/blopker.github.com
git add --all :/
git commit -am "Updated releases"
git push origin master
