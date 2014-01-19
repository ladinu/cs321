// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Represents a set of transitions in a DFA that can be expanded
 *  dynamically to include new values.  This provides some flexibility
 *  that is needed to enable the construction of DFAs as part of the
 *  subset construction: our representation for state machines uses an
 *  array to store transitions, assuming that the number is fixed.  But
 *  we cannot determine the total number of transitions in a DFA state
 *  until we have explored all of the corresponding transitions in the
 *  underlying NFA.
 */
class DFATrans {
    /** Records the total number of states in the underlying NFA.
     */
    private int numNFAstates;
    DFATrans(int numNFAstates) {
        this.numNFAstates = numNFAstates;
    }

    /** Holds a pointer to the root of the list of DFA transitions.
     *  A null indicates an empty list.
     */
    private Link root = null;

    /** A representation for the individual elements in
     *  a list of DFA transitions.
     */
    private class Link {
        int c;
        boolean[] targets;
        Link next;
        Link(int c, Link next) {
            this.c       = c;
            this.targets = new boolean[numNFAstates];
            this.next    = next;
        }
    }

    /** Add some transitions corresponding to a particular array
     *  of transitions in the underlying NFA.
     */
    void addTrans(Transition[] trs) {
        for (int j=0; j<trs.length; j++) {
            if (trs[j].on!=Transition.epsilon) {
                addTrans(trs[j].on, trs[j].target.num);
            }
        }
    }

    /** Add a DFA transition corresponding to a specific transition
     *  in the underlying NFA.
     */
    private void addTrans(int c, int num) {
        Link list = root;
        while (list!=null && c!=list.c) {
            list=list.next;
        }
        if (list==null) {
            root = new Link(c, root);
            list = root;
        }
        list.targets[num] = true;
    }

    /** Convert this list of transitions into an array, suitable
     *  for use in defining a DFA state.
     */
    public Transition[] getTransitions(SubsetConstruction dfa) {
        // Calculate the number of transitions.
        int len = 0;
        for (Link list=root; list!=null; list=list.next) {
            len++;
        }

        // Allocate an array to hold the transitions.
        Transition[] trans = new Transition[len];

        // Fill in the array, taking the epsilon closure of each
        // set of targets, and adding new states to the DFA if we
        // find a state that hasn't previously been encountered.
        int n = 0;
        for (Link list=root; list!=null; list=list.next) {
            dfa.epsilonClose(list.targets);
            trans[n++] = new Transition(list.c,
                                        dfa.addState(list.targets));
        }
        return trans;
    }
}
