// 05 There is no need for actions to produce output

// jflex Example.jflex 
// javac Yylex.java
// java Yylex test.mini

/* user code */

%%

/* operations and declarations */

%{
int numLines = 0;
int numChars = 0;
%}

%eof{
   System.out.println("Number of lines = " + numLines);
   System.out.println("Number of chars = " + numChars);
%eof}

%standalone

%%

/* lexical rules */

\n		{ numChars++; numLines++; }
.		{ numChars++; }
