// 07 A period doesn't match newlines (Part 1)

// jflex Example.jflex 
// javac Yylex.java
// java Yylex test.mini

/* user code */

%%

/* operations and declarations */

%{
boolean output = true;
%}

%standalone

%%

/* lexical rules */

^BEGIN		{ output = false; }
^END		{ output = true;  }
.		{ if (output) System.out.print(yytext()); }
