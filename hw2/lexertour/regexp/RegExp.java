// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

import java.io.*;

/** Base class for the representation of regular expressions.
 */
abstract class RegExp {
    /** Return a string with a fully parenthesized version of this
     *  regular expression.
     */
    public abstract String fullParens();

    /** Output a description of this node (with id n), including a
     *  link to its parent node (with id p) and returning the next
     *  available node id.
     */
    public int toDot(DotOutput dot, int p, int n) {
        dot.join(p, n);
        return toDot(dot, n);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public abstract int toDot(DotOutput dot, int n);

    /** Write a description of this node's abstract syntax tree to
     *  the specified file.
     */
    public void toDot(String name) {
        try {
            FileOutputStream file = new FileOutputStream(name);
            new DotOutput(new PrintStream(file)).toDot(this);
            file.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    /** Construct an NFA that will recognize a string matching this
     *  regular expression and then transition to the follow-on NFA
     *  with start state s.
     */
    abstract State toNFA(State s);

    /** Construct an NFA that will recognize a string matching this
     *  regular expression and then transition to a state that will
     *  accept with the given integer code.
     */
    public State toNFA(int accept) {
       State  s = new State();       // Build a new state
       s.accept = accept;            // with specified accept code
       s.trans  = new Transition[0]; // and no outgoing transitions.
       return this.toNFA(s);         // Generate recognizer.
    }
}
