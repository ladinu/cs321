# Makefile for miniJava compiler.
#
JFLAGS = -g
JC = javac
JCC = javacc
DEBUG= -debug_parser

.SUFFIXES: .jj .java .class

.jj.java:
	$(JCC) $*.jj

.java.class:
	$(JC) $(JFLAGS) $*.java

all:	mjGrammarLL.class

dd:
	$(JCC) $(DEBUG) mjGrammarLL.jj
	$(JC) *.java
cc:
	$(JCC) mjGrammarLL.jj
	$(JC) *.java

tt:
	for file in $$(ls tst/test*.java); do \
	   echo "####### Testing $$file ##########"; \
	   java mjGrammarLL $$file; \
	done;

clean:
	rm mjGrammarLL*.class Parse*.* Simple*.* Token*.*
	
clean-hard:
	rm mjGrammarLL*.class Parse*.* Simple*.* Token*.*
	rm mjGrammarLL*.java Parse*.* Simple*.* Token*.*
