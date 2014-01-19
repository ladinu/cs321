// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

import compiler.*;

/** A hand-coded lexer for use in reading regular expressions.
 */
public class RegExpLexer extends SourceLexer {
    public RegExpLexer(Handler handler, Source source) {
        super(handler, source);
    }

    /** Return a code describing the next token in the input
     *  stream, and returning 0 at the end of the input.  We
     *  use the ASCII values of the characters (, ), *, |, and
     *  % as the codes for the corresponding tokens (% is used
     *  in place of epsilon); we use the character c as the code
     *  for a single character regular expression (saving the
     *  actual character value in tokChar); and we use a 0 code
     *  to signal the end of the input.
     */
    public int nextToken() {
        for (;;) {
          switch (c) {
              case EOF  : return token=0;  // end of input
              case ' '  :                  // skip whitespace
              case '\t' :
              case '\r' : c = nextChar();
                          continue;
              case EOL  : nextLine();      // skip newlines
                          continue;
              case '('  :
              case ')'  :
              case '*'  :
              case '|'  :
              case '%'  : token = c;
                          c     = nextChar();
                          return token;
              case '\\' : c = nextChar();
                          if (c==EOF) {
                              report(new Failure("Missing character"));
                              return '\\';
                          }
                          /* intentional fall-thru */
              default   : tokChar = c;
                          c       = nextChar();
                          return token = 'c';
          }
        }
    }

    /** Stores the value of the most recently read single character token.
     */
    private int tokChar;

    /** Return a single character regular expression for matching the most
     *  recently read single character.
     */
    public RegExp getSemantic() { return new Char(tokChar); }
}
