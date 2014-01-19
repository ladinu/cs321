// 09 Files can introduce useful abbreviations

// jflex Example.jflex 
// javac Yylex.java
// java Yylex test.mini

/* user code */

%%

/* operations and declarations */

%standalone

Identifier         = [:jletter:] [:jletterdigit:]*

LineTerminator     = \r|\n|\r\n
WhiteSpace         = {LineTerminator} | [ \t\f] 
InputCharacter     = [^\r\n]

Comment            = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment   = "//" {InputCharacter}* {LineTerminator}

Keyword            = "if" | "else" | "while" | "print" | "int" | "boolean"

%%

/* lexical rules */

{Keyword}	{ System.out.print(yytext()); }

{Comment}	{ /* ignore comments */ }

{Identifier}	{ System.out.print("ID(" + yytext().toUpperCase() + ")"); }
