// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

import compiler.*;

/** A recursive descent parser for the simple language of regular
 *  expressions that is defined by the following grammar:
 *
 *            regexp = seq   |  seq '|' regexp    
 *            seq    = rep   |  rep seq           
 *            rep    = atom  |  rep '*'  |  rep '+'
 *            atom   = '(' regexp ')'  |  'c'  |  '%'   
 *
 */
public class RDRegExpParser extends Phase {
    /** Construct a parser for regular expressions using the
     *  specified handler for diagnostics.
     */
    public RDRegExpParser(Handler handler) {
        super(handler);
    }

    /** Stores a pointer to the lexer that is being used to supply
     *  tokens for this parser.
     */
    private RegExpLexer lexer;

    /** Records the token code for the most recently read lexeme.
     */
    private int token;

    /** Read the next token from the current lexer, updating the
     *  token variable to hold the result.
     */
    private void nextToken() {
        token = lexer.nextToken();
    }

    /** Use this parser to construct a RegExp from the tokens that
     *  the given lexer produces.
     */
    public RegExp parseRegExp(RegExpLexer lexer) {
        this.lexer = lexer;
        nextToken();
        RegExp r = regexp();
        if (token!=0) {
            report(new Failure(lexer.getPos(),
                               "extra tokens at the end of the input string"));
        }
        return r;
    }

    // One method for each of the nonterminals in the grammar

    /** Parse a regexp: regexp = seq | seq '|' regexp
     */
    private RegExp regexp() {
        RegExp r = seq();              // read a sequence
        if (token=='|') {              // look for a '|'
            nextToken();               // ... followed by another
            r = new Alt(r, regexp());  //     regexp
        }
        return r;
    }

    /** Parse a seq:   seq = rep  |  rep seq
     */
    private RegExp seq() {
        RegExp r = rep();              // read a rep
        if (token=='c' || token=='%' || token=='(') {
                                       // if an atom could come next,
            r = new Seq(r, seq());     // then look for another seq()
        }
        return r;
    }

    /** Parse a rep:   rep = atom  |  rep '*'
     */
    private RegExp rep() {
        RegExp r = atom();             // read an atom
        while (token=='*') {           // followed by zero or more '*'s
            nextToken();
            r = new Rep(r);
        }
        return r;
    }

    /** Parse an atom:  atom  =  '(' regexp ')'  |  'c'  |  '%'
     */
    private RegExp atom() {
        if (token=='c') {               // check for single character
            RegExp r = lexer.getSemantic();
            nextToken();
            return r;
        } else if (token=='%') {        // check for an epsilon
            nextToken();
            return new Epsilon();
        } else if (token=='(') {        // look for a parenthesized
            nextToken();                // expression ...
            RegExp r = regexp();
            if (token==')') {
                nextToken();
            } else {
                report(new Failure(lexer.getPos(),
                                   "missing close parenthesis"));
            }
            return r;
        }
        report(new Failure(lexer.getPos(),
                           "syntax error in regular expression"));
        return new Epsilon(); // represents missing regular expression
    }

    /** Convenience method that combines a lexer and a parser to parse
     *  a sequence of strings as a regular expression.
     */
    public static RegExp parse(Handler handler, String[] args) {
        RDRegExpParser parser  = new RDRegExpParser(handler);
        Source         source  = new StringArraySource(handler,"input",args);
        RegExpLexer    lexer   = new RegExpLexer(handler, source);
        return parser.parseRegExp(lexer);
    }

    /** Parse a single string to extract the AST for a regular expression.
     */
    public static RegExp parse(Handler handler, String arg) {
        return parse(handler, new String[] { arg });
    }
}
