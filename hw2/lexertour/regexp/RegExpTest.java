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
public class RegExpTest {
    public static void main(String[] args) {
        // Read regular expression:
        Handler handler = new SimpleHandler();
        RegExp  r       = RDRegExpParser.parse(handler, args);

        // Print out in fully parenthesized form:
        System.out.println("r = " + r. fullParens());
        r.toDot("ast.dot");

        // Now build an NFA for r, number its states, and
        // collect them together in an array.
        State m        = r.toNFA(1);
        int count      = m.numberStates(0);
        State[] states = new State[count];
        m.collectStates(states);
 
        // Output a description of the machine, including the
        // transitions from each state.
        System.out.println("number of NFA states = " + states.length);
        for (int st=0; st<states.length; st++) {
            states[st].display();
        }
        DotOutput.toDot(states, "nfa.dot");

        // Run the subset construction to generate a corresponding
        // DFA and then output a description of that DFA.
        State start = new SubsetConstruction(states).getDFA();
        State[] dfa = new State[start.numberStates(0)];
        start.collectStates(dfa);
        System.out.println("number of DFA states = " + dfa.length);
        for (int st=0; st<dfa.length; st++) {
            dfa[st].display();
        }
        DotOutput.toDot(dfa, "dfa.dot");

        // Read input lines and use the generated DFA to match them
        // against the original regular expression.
        Source input = new StdinSource(handler);
        String line;
        System.out.println("Enter text to match, or an empty line to end:");
        while ((line=input.readLine())!=null && line.length()>0) {
            int n = dfa[0].match(line, 0);
            if (n<0) {
                System.out.println("No match!");
            } else {
                for (int i=0; i<n; i++) {
                    System.out.print(" ");
                }
                System.out.println("^");
            }
        }
    }
}
