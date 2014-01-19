// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Represents a single state in a finite automaton (either an NFA
 *  or a DFA).  Each state has an associated array of transitions as
 *  well as a flag to indicate if this is an accept state.
 */
class State {
    /** The set of transitions associated with this state.
     */
    Transition[] trans = null;

    /** An accept code for this state.  A zero value indicates that
     *  this is not an accepting state.  A positive value indicates
     *  an accept state.  We can use multiple distinct positive
     *  accept codes within a given machine to represent different
     *  "reasons" for being able to accept.
     */
    int accept = 0;

    /** Output a description of this machine state.
     */
    void display() {
        System.out.println("State no: " + num);
        if (accept>0) {
            System.out.println("Accept state! [code="+accept+"]");
        }
        for (int i=0; i<trans.length; i++) {
            int   c  = trans[i].on;
            State to = trans[i].target;
            if (c==Transition.epsilon) {
                System.out.println("Epsilon transition to " + to.num);
            } else {
                System.out.println("Transition on " + (char)c +
                                   " to state " + to.num);
            }
        }
        System.out.println();
    }

    /** Holds the number/unique identifier of this state.  A negative
     *  value here indicates that the state has not yet been assigned
     *  a number.
     */
    int num = (-1);

    /** Perform a depth-first search of the automaton, starting at
     *  this state, so that every reachable state receives a distinct
     *  identifier.  The returned result indicates the number of
     *  distinct states that were encountered.
     */
    public int numberStates(int n) {
        if (num<0) {
            num = n++;
            for (int i=0; i<trans.length; i++) {
                n = trans[i].target.numberStates(n);
            }
        }
        return n;
    }

    /** A follow-up to numberStates that performs a second
     *  depth-first search, building up an array of all the
     *  states in this machine.
     */
    public void collectStates(State[] states) {
        if (states[num]==null) {
            states[num] = this;
            for (int i=0; i<trans.length; i++) {
                trans[i].target.collectStates(states);
            }
        }
    }

    /** Compute the epsilon closure of this state, adding to
     *  and recording the results in the given bit vector.
     */
    void epsilonClose(boolean[] visited) {
        for (int i=0; i<trans.length; i++) {
            int    c  = trans[i].on;
            State  to = trans[i].target;
            if (c==Transition.epsilon && !visited[to.num]) {
                visited[to.num] = true;
                to.epsilonClose(visited);
            }
        }
    }

    /** Attempt to match a maximal initial portion of the given
     *  string using this state as the start state.
     */
    public int match(String s) {
        return match(s, 0);
    }

    /** Attempt to match the given string, beginning at the specified position,
     *  using this state as the start state.  Matching may not succeed if the
     *  machine is non-deterministic because it may choose the "wrong"
     *  transitions; this is one of the reasons why we prefer to use a DFA
     *  instead of an NFA.
     */
    public int match(String s, int pos) {
        State current   = this;      // track current state in DFA
        int   acceptPos = (-1);      // position of last accept state
        acceptState     = null;
        while (current!=null) {
            if (current.accept>0) {  // is this an accept state?
                acceptState = current;
                acceptPos   = pos;
            }
            if (pos>=s.length()) {   // finished reading input?
                break;
            }
            Transition[] trs = current.trans;
            int   c = s.charAt(pos);
            current = null;          // look for matching transition
            for (int i=0; i<trs.length; i++) {
                if (c==trs[i].on) {
                    current = trs[i].target;
                    pos++;
                    break;
                }
            }
        }
        return acceptPos;
    }

    /** Records the last accept state that was encountered during matching.
     */
    public static State acceptState = null;

    /** Generate a String label for this state.
     */
    public String label() {
        return Integer.toString(num);
    }

    /** Output a description of this state in dot format.
     */ 
    public void toDot(DotOutput dot) {
        dot.state(num, label(), accept);
        if (trans!=null) {
            for (int i=0; i<trans.length; i++) {
               String sym = (trans[i].on==Transition.epsilon)
                             ? ""
                             : Character.toString((char)trans[i].on);
               dot.trans(num, trans[i].target.num, sym);
            }
        }
    }
}
