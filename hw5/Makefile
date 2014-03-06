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
