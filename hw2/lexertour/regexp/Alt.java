// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Represents a regular expression r1 | r2 with two alternatives.
 */
class Alt extends RegExp {
    private RegExp r1;
    private RegExp r2;
    Alt(RegExp r1, RegExp r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    /** Return a string with a fully parenthesized version of this
     *  regular expression.
     */
    public String fullParens() {
        return "(" + r1.fullParens() + "|" + r2.fullParens() + ")";
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */ 
    public int toDot(DotOutput dot, int n) {
        return r2.toDot(dot, n,
               r1.toDot(dot, n,
               dot.node("Alt", n)));
    }

    /** Construct an NFA that will recognize a string matching this
     *  regular expression and then transition to the follow-on NFA
     *  with start state s.
     */
    State toNFA(State s) {
        State n = new State();
        n.trans = new Transition[] {
          new Transition(r1.toNFA(s)),
          new Transition(r2.toNFA(s))
        };
        return n;
    }
}
