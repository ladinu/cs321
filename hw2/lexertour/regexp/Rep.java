// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Represents a regular expression r* that matches a sequence of
 *  zero or more substrings, each of which matches r.
 */
class Rep extends RegExp {
    private RegExp r;
    Rep(RegExp r) {
        this.r = r;
    }

    /** Return a string with a fully parenthesized version of this
     *  regular expression.
     */
    public String fullParens() {
        return "(" + r.fullParens() + "*)";
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return r.toDot(dot, n,
               dot.node("Rep", n));
    }

    /** Construct an NFA that will recognize a string matching this
     *  regular expression and then transition to the follow-on NFA
     *  with start state s.
     */
    State toNFA(State s) {
        State n = new State();
        n.trans = new Transition[] {
          new Transition(r.toNFA(n)),
          new Transition(s)
        };
        return n;
    }
}
