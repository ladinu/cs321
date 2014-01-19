// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

import compiler.*;
import java.io.*;

public class Explode {
    public static void main(String[] args) {
        if (args==null || args.length!=1) {
            System.err.println("Please enter a (small) number on the command line");
        } else {
            try {
                int n = Integer.parseInt(args[0]);
                explode(n);
            } catch (NumberFormatException e) {
                System.err.println("Expecting a numeric argument");
            }
        }
    }

    static void explode(int n) {
        System.out.print("Building a machine to recognize: (a|b)*a");
        for (int i=0; i<n; i++) {
            System.out.print("(a|b)");
        }
        System.out.println();

        // Build the (accepting) machine that we'll goto when then
        // regular expression has been recognized.
        State s  = new State();
        s.accept = 1;
        s.trans  = new Transition[0];

        // Add some initial sequence of states allowing either a or b:
        for (int i=0; i<n; i++) {
            State m = new State();
            m.trans = new Transition[] {
                        new Transition('a', s),
                        new Transition('b', s)
                      };
            s       = m;
        }
 
        // Create the start state:
        State m = new State();
        m.trans = new Transition[] {
                    new Transition('a', m),
                    new Transition('a', s),
                    new Transition('b', m)
                  };
       
        // Now number the states, and
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
    }
}

