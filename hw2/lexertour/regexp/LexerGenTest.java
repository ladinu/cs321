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
public class LexerGenTest {
    public static void main(String[] args) {
        if (args==null || args.length<1) {
           System.err.println("No regular expressions to match");
        }
        Handler handler = new SimpleHandler();

        // Create an initial state with epsilon transitions
        // to NFAs for each of the regexps:
        State m = new State();
        m.trans = new Transition[args.length];
        for (int i=0; i<args.length; i++) {
            // Build an accepting machine that will recognize the
            // ith regular expression and then accept with code i+1.
            RegExp r = RDRegExpParser.parse(handler, args[i]);
            m.trans[i] = new Transition(r.toNFA(i+1));
        }

        // Now build the full machine, number its states, and
        // collect them together in an array.
        int count      = m.numberStates(0);
        State[] states = new State[count];
        m.collectStates(states);
        DotOutput.toDot(states, "nfa.dot");
 
        // Output a description of the machine, including the
        // transitions from each state.
        System.out.println("number of NFA states = " + states.length);
        for (int st=0; st<states.length; st++) {
            states[st].display();
        }

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
        System.out.println("Enter text to tokenize, or an empty line to end:");
        while ((line=input.readLine())!=null && line.length()>0) {
            int pos = 0;
            int newpos;
            while ((newpos = dfa[0].match(line, pos)) > pos) {
                System.out.println("Matched \""
                                  + line.substring(pos, newpos)
                                  + "\", accept code = "
                                  + State.acceptState.accept);
                pos = newpos;
            }
            if (newpos<line.length()) {
                System.out.println("Unmatched trailing input: "
                                  + line.substring(newpos));
            }
        }
    }
}
