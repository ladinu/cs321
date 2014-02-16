# Makefile for miniJava compiler. (Jingke Li)
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

parser: ast/Ast.class mjParser.class

clean:	
	rm -f ast/*.class *.class
	rm -f *.java
