// 03 Longest lexeme wins

// jflex Example.jflex 
// javac Yylex.java
// java Yylex test.mini

/* user code */

%%

/* operations and declarations */
%standalone

%%

/* lexical rules */

"i"		{ System.out.print("count"); }
"t"             { System.out.print("total"); }
[a-z]+		{ System.out.print(yytext()); }
