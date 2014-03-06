
for testFile in `find tests -name "*.ast"`; do
   printf "Running $testFile: "
   expected=${testFile%.*}.ref
   java TestReachability $testFile > tests/pref.ref
   result=`diff $expected tests/pref.ref`
   if [ "$result" != "" ]; then
      echo "FAIL"
      echo $result
   else
      echo ""
   fi
done

rm -rf tests/pref.ref
