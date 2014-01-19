// 04 If two rules match the same input, choose the earliest rule

// jflex Example.jflex 
// javac Yylex.java
// java Yylex test.mini

/* user code */

%%

/* operations and declarations */
%standalone

%%

/* lexical rules */

[a-z]+		{ System.out.print(yytext()); }
"i"		{ System.out.print("count"); }
"t"             { System.out.print("total"); }
