// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** A quick test for the code to print fully parenthesized regular
 *  expressions.  We use the abstract syntax classes to build the
 *  representation of a particular regular expression and then
 *  call fullParens to display the result.
 */
public class FullParensTest {
    public static void main(String[] args) {
        // Build a representation of the regular expr (abc)*|(a*b*c*)
        RegExp  a = new Char('a');
        RegExp  b = new Char('b');
        RegExp  c = new Char('c');
        RegExp  r = new Alt(new Rep(new Seq(a, new Seq(b,c))),
                            new Seq(new Rep(a),
                                    new Seq(new Rep(b),
                                            new Rep(c))));
        System.out.println("r = " + r.fullParens());
        r.toDot("ast.dot");
    }
}
