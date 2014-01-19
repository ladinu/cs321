// 11 jflex can be used to build lexers!

package mini;
import compiler.*;
import java.io.*;

%%

%class      MiniLexer
%public
%extends    Phase
%implements MiniTokens
%ctorarg    Handler handler
%init{
  super(handler);
%init}

%function   yylex
%int

%eofval{
  return ENDINPUT;
%eofval}

%{

  private int  token;

  private Object semantic;

  public int getToken() {
    return token;
  }

  public Object getSemantic() {
    return semantic;
  }

  public int nextToken() {
    try {
      semantic = null;
      token    = yylex();
    } catch (java.io.IOException e) {
      System.out.println("IO Exception occurred:\n" + e);
    }
    return token;
  }
  
  public String showToken() {
    switch (token) {
      case ','     : return "Comma \",\"";
      case INT     : return "INT";
      case BOOLEAN : return "BOOLEAN";

      case '('     : return "Open parenthesis \"(\"";
      case ')'     : return "Close parenthesis \")\"";
      case '{'     : return "Open brace \"{\"";
      case '}'     : return "Close brace \"}\"";
      case ';'     : return "Semicolon \";\"";

      case '='     : return "=";
      case EQL     : return "==";
      case '>'     : return ">";
      case GTE     : return ">=";
      case '<'     : return "<";
      case LTE     : return "<=";
      case '!'     : return "!";
      case '~'     : return "~";
      case NEQ     : return "!=";
      case '&'     : return "&";
      case LAND    : return "&&";
      case '|'     : return "|";
      case LOR     : return "||";
      case '^'     : return "^";
      case '*'     : return "*";
      case '+'     : return "+";
      case '-'     : return "-";
      case '/'     : return "/";

      case WHILE   : return "WHILE";
      case IF      : return "IF";
      case ELSE    : return "ELSE";
      case PRINT   : return "PRINT";

      case IDENT   : return "IDENT("+yytext()+")";

      case INTLIT  : return "INTLIT("+yytext()+")";

      default      : return "Unknown token";
    }
  }

%}

Identifier = [:jletter:] [:jletterdigit:]*

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f] 
InputCharacter = [^\r\n]

Comment            = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment   = "//" {InputCharacter}* {LineTerminator}

%%

","             { return ','; }
"["             { return '['; }
"]"             { return ']'; }
"int"           { return INT; }
"boolean"       { return BOOLEAN; }

"("             { return '('; }
")"             { return ')'; }
"{"             { return '{'; } 
"}"             { return '}'; }
";"             { return ';'; }

"="             { return '='; }
"=="            { return EQL; }
">"             { return '>'; }
">="            { return GTE; }
"<"             { return '<'; }
"<="            { return LTE; }
"!"             { return '!'; }
"~"             { return '~'; }
"!="            { return NEQ; }
"&"             { return '&'; }
"&&"            { return LAND; }
"|"             { return '|'; }
"||"            { return LOR; }
"^"             { return '^'; }
"*"             { return '*'; }
"+"             { return '+'; }
"-"             { return '-'; }
"/"             { return '/'; }

"while"         { return WHILE; }
"if"            { return IF; }
"else"          { return ELSE; }
"print"         { return PRINT; }

{Identifier}    { semantic = new Id(yytext());     return IDENT; }

[0-9]+          { semantic = new IntLit(yytext()); return INTLIT; }

{WhiteSpace}    { /* ignore */ }
{Comment}       { /* ignore */ }

.|\n            { System.out.println("Invalid input");
                  System.exit(1);
                }

