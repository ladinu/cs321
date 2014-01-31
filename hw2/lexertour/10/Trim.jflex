// Given a HTML file that is generated from MiniColor,
// this program remove all the HTML tags leaving only the 
// original mini program
//
// Run using direction below:
//
//    $ jflex Uncolor.jflex
//    $ javac Uncolor.java
//    $ java Uncolor input.html
%%

%standalone
%class Trim

abort = \n"ABORT"\n(.|\n)*

%%
  <abort><<EOF>>      { System.out.print(yytext()); }
