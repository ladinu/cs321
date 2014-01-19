// 02 Output produced by an action replaces original input

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
