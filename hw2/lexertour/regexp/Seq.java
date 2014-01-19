// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Represents a sequence regular expression of the form r1 r2, which
 *  matches a string matching r1 followed by a string matching r2.
 */
class Seq extends RegExp {
    private RegExp r1;
    private RegExp r2;
    Seq(RegExp r1, RegExp r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    /** Return a string with a fully parenthesized version of this
     *  regular expression.
     */
    public String fullParens() {
        return "(" + r1.fullParens() + r2.fullParens() + ")";
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return r2.toDot(dot, n,
               r1.toDot(dot, n,
               dot.node("Seq", n)));
    }

    /** Construct an NFA that will recognize a string matching this
     *  regular expression and then transition to the follow-on NFA
     *  with start state s.
     */
    State toNFA(State s) {
        return r1.toNFA(r2.toNFA(s));
    }
}
