// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

import compiler.*;

/** A quick test for the regexp lexer, reading tokens from the text
 *  entered on the command line and displaying the corresponding list
 *  of token codes.
 */
public class LexerTest {
    public static void main(String[] args) {
        Handler        handler = new SimpleHandler();
        Source         source  = new StringArraySource(handler,"input",args);
        RegExpLexer    lexer   = new RegExpLexer(handler, source);
    
        while (lexer.nextToken()!=0) {
          System.out.print((char)lexer.getToken());
        }
        System.out.println();
    }
}
