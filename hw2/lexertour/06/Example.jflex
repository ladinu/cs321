// 06 There is no need for actions to produce output

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
.|\n		{ if (output) System.out.print(yytext()); }
