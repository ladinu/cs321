// 01 Basic structure of a .jflex file
//    Text matching rules with empty actions is ignored
//    Text not matching any rule is copied unchanged to the output

// jflex Example.jflex 
// javac Yylex.java
// od -c test.mini 
// java Yylex test.mini | od -c

/* user code */

%%

/* operations and declarations */
%standalone

%%

/* lexical rules */

"\r"		{ /* ignore */ }
