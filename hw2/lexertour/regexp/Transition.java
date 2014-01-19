// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

/** Represents a single transition in a state machine, capturing both
 *  the target and the input symbol that allows the transition to occur.
 *  We do not store a source for each transition because we assume that
 *  each transition is stored as part of its source, thereby creating
 *  an implicit connection.
 */
class Transition {
    /** Captures the input symbol/character for this transition.
     *  The epsilon code defined below is used to identify epsilon
     *  transitions (i.e., transitions that do not consume any
     *  input).
     */
    int   on;

    /** The target state for this transition.
     */
    State target;

    /** Construct a transition.
     */
    Transition(int on, State target) {
        this.on = on;
        this.target = target;
    }

    /** Special code used to signal an epsilon transition.
     */
    static final int epsilon = '\0';

    /** Construct an epsilon transition to a specified target.
     */
    Transition(State target) {
        this(epsilon, target);
    }
}
