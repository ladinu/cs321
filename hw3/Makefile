# Makefile for miniJava compiler.
#
JFLAGS = -g
JC = javac
JCC = javacc

.SUFFIXES: .jj .java .class

.jj.java:
	$(JCC) $*.jj

.java.class:
	$(JC) $(JFLAGS) $*.java

all:	mjGrammarLL.class

clean:
	rm mjGrammarLL*.java mjGrammarLL*.class Parse*.* Simple*.* Token*.*