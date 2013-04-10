#!/usr/bin/env bash

play clean
play publish-local

cd ~/code

if [ ! -d blopker.github.com ]; then
  echo "HI"
  git clone git@github.com:blopker/blopker.github.com.git
fi

cp -rv ~/play/repository/local/com.blopker blopker.github.com/releases

cd blopker.github.com
git add --all :/
git commit -am "Updated releases"
git push origin master
