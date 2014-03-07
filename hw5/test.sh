#!/bin/sh
function runTests() {
   for testFile in `find $1 -name "*.ast"`; do
      printf "Running $testFile: "
      expected=${testFile%.*}.ref
      java TestReachability $testFile > $1/pref.ref
      result=`diff $expected $1/pref.ref`
      if [ "$result" != "" ]; then
         echo "FAIL"
         diff $expected $1/pref.ref
      else
         echo ""
      fi
   done
}

function clean() {
   rm -rf $1/pref.ref
}

runTests $1
clean $1
