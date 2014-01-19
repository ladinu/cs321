// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Represents a single state in a DFA, as generated using the
 *  subset construction from an initial NFA.  Each of these DFA
 *  states is annotated with a bit vector that describes the
 *  associated set of states in the original NFA.
 */
class DFAState extends State {
    /** Records the set of NFA states associated with this DFA state.
     */
    private boolean[] stateSet;

    /** Construct a new DFA state corresponding to a given set of NFA
     *  states.
     */
    DFAState(boolean[] stateSet) {
        this.stateSet = stateSet;
    }

    /** Test to see if this DFA state corresponds to a specific set of
     *  NFA states given by the bit vector parameter that is passed in.
     */
    public boolean setEq(boolean[] b) {
        if (b.length==stateSet.length) {
            for (int i=0; i<b.length; i++) {
                if (b[i]!=stateSet[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /** Explore this DFA state by calculating all the transitions
     *  that can be made from any of the states in the underlying NFA.
     */
    public void explore(SubsetConstruction dfa, State[] nfaStates) {
        DFATrans trs = new DFATrans(nfaStates.length);
        for (int i=0; i<stateSet.length; i++) {
            if (stateSet[i]) {
                trs.addTrans(nfaStates[i].trans);
                if (nfaStates[i].accept>0 &&
                     (accept==0 || nfaStates[i].accept < accept)) {
                    accept = nfaStates[i].accept;
                }
            }
        }
        trans = trs.getTransitions(dfa);
    }

    /** Output a description of a DFA state, prefixing it
     *  with the set of NFA states that it represents.
     */
    public void display() {
        System.out.print(label());
        super.display();
    }

    /** Generate a String label for this state.
     */
    public String label() {
        StringBuffer buf  = new StringBuffer("{");
        String punc = "";
        for (int i=0; i<stateSet.length; i++) {
            if (stateSet[i]) {
                buf.append(punc);
                buf.append(i);
                punc = ", ";
            }
        }
        buf.append("}");
        return buf.toString();
    }
}
