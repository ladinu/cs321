// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Implements the subset construction to generate a DFA from an NFA.
 */
class SubsetConstruction {
    /** Records the set of states for the input NFA, with the
     *  assumption that the start state is in the first (0)
     *  slot.
     */
    private State[] nfaStates;

    /** Records the set of DFA states that have been constructed so
     *  far.  Only the first numStates entries of this array will
     *  contain valid states.  The array will be resized as necessary
     *  as new elements are added.
     */
    private DFAState[] dfaStates = new DFAState[10];

    /** Records the number of DFA states that have been constructed
     *  so far.
     */
    private int numStates = 0;

    /** Construct a new DFA from a given NFA using the subset
     *  construction to explore the set of DFA states that are
     *  needed.
     */
    public SubsetConstruction(State[] nfaStates) {
        this.nfaStates = nfaStates;
  
        // Add the initial DFA state, labeled as {0}
        boolean[] initStateSet = new boolean[nfaStates.length];
        initStateSet[0]        = true;
        nfaStates[0].epsilonClose(initStateSet);
        addState(initStateSet);
  
        // Now explore each DFA state in queue fashion until we're done
        for (int i=0; i<numStates; i++) {
            dfaStates[i].explore(this, nfaStates);
        }
    }

    /** Return a pointer to the first state in the generated DFA.
     */
    public State getDFA() {
        return dfaStates[0];
    }

    /** Compute the epsilon closure of the set of NFA states
     *  represented by the bit vector b, which contains one bit
     *  for each NFA state.
     */
    void epsilonClose(boolean[] b) {
        for (int i=0; i<b.length; i++) {
            if (b[i]) {
                nfaStates[i].epsilonClose(b);
            }
        }
    }

    /** Add a new state to the generated DFA corresponding to a given
     *  set of NFA states, or return an already constructed state if
     *  the given bit vector has been seen previously.
     */
    DFAState addState(boolean[] stateSet) {
        // Look for an existing state labelled with stateSet
        for (int i=0; i<numStates; i++) {
          if (dfaStates[i].setEq(stateSet)) {
             return dfaStates[i];
          }
        }

        // If we get here, then we have found a new state
        if (numStates>=dfaStates.length) {
            // Expand the array of DFA states as necessary
            DFAState[] newDfaStates = new DFAState[2*dfaStates.length];
            for (int i=0; i<dfaStates.length; i++) {
                newDfaStates[i] = dfaStates[i];
            }
            dfaStates = newDfaStates;
        }

        // Construct a new DFA state and add it to the end of the array.
        return dfaStates[numStates++] = new DFAState(stateSet);
    }
}
