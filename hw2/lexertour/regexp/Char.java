// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Represents a single character regular expression.
 */
class Char extends RegExp {
    private int c;
    Char(int c) {
        this.c = c;
    }

    /** Return a string with a fully parenthesized version of this
     *  regular expression.
     */
    public String fullParens() {
        return Character.toString((char)c);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return dot.node(Character.toString((char)c), n);
    }

    /** Construct an NFA that will recognize a string matching this
     *  regular expression and then transition to the follow-on NFA
     *  with start state s.
     */
    State toNFA(State s) {
        State n = new State();
        n.trans = new Transition[] {
          new Transition(c, s)
        };
        return n;
    }
}
