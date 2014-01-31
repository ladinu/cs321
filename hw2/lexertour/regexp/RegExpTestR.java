// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

import compiler.*;
import java.io.*;

/** A test that exercises most of the features of the regexp
 *  package by: parsing a regexp to build a RegExp AST;
 *  generating a corresponding NFA; using the subset construction
 *  to find a DFA (but we do not attempt minimization); and then
 *  using the resulting DFA to attempt matches of input strings.
 */
public class RegExpTestR {
    public static void main(String[] args) {
        // Read regular expression:
        Handler handler = new SimpleHandler();
        RegExp  r       = RDRegExpParser.parse(handler, args);

        // Print out in fully parenthesized form:

        // Now build an NFA for r, number its states, and
        // collect them together in an array.
        State m        = r.toNFA(1);
        int count      = m.numberStates(0);
        State[] states = new State[count];
        m.collectStates(states);
 
        // Output a description of the machine, including the
        // transitions from each state.

        // Run the subset construction to generate a corresponding
        // DFA and then output a description of that DFA.
        State start = new SubsetConstruction(states).getDFA();
        State[] dfa = new State[start.numberStates(0)];
        start.collectStates(dfa);

        System.out.println("states: "+ dfa.length);

        // Read input lines and use the generated DFA to match them
        // against the original regular expression.
    }
}
