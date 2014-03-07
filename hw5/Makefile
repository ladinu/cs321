# Makefile for miniJava static analyzer.
#
JFLAGS = -g
JC = javac
JCC = javacc

.SUFFIXES: .jj .java .class

.PRECIOUS: %.java

.jj.java:
	$(JCC) $*.jj

.java.class:
	$(JC) $(JFLAGS) $*.java

all: AstParser.java TestReachability.class 

clean:	
	rm -f AstParser*.java TokenMgrError.java Token.java ParseException.java SimpleCharStream.java
	rm -f ast*/*.class *.class
	rm -f test/pref.ref

.PHONY: test1
test1:
	./test.sh test1

.PHONY: test2
test2:
	./test.sh test2
