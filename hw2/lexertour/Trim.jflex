// 00 Basic structure of a .jflex file
//    Text matching rules with empty actions is ignored
//    Text not matching any rule is copied unchanged to the output

// jflex Example.jflex 
// javac Yylex.java
// java Yylex test.mini

/* user code */

%%

%class Trim
%standalone

%%


(.|\n)*ABORT\n	{ /* ignore */ }
