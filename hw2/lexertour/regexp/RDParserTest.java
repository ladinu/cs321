// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

import compiler.*;
import java.io.*;

/** A quick test program for the recursive descent regexp parser
 *  in RDRegExpParser.  Uses the text formed by concatenating the
 *  command line arguments as the source, and prints out a fully
 *  parenthesized version of the same regular expression.
 */
public class RDParserTest {
    public static void main(String[] args) {
        RegExp r = RDRegExpParser.parse(new SimpleHandler(), args);
        System.out.println("r = " + r. fullParens());
        r.toDot("ast.dot");
    }
}
