// This is supporting software for CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package regexp;

import java.io.*;

/** Represents an output phase for producing textual output of abstract
 *  syntax trees in dot format, suitable for the AT&T Graphviz tools.
 */
public class DotOutput {
    private PrintStream out;
    public DotOutput(PrintStream out) {
        this.out = out;
    }

    //- Generate graphs for abstract syntax trees ----------------------

    /** Create a dot graph for the abstract syntax tree of the
     *  given RegExp.
     */
    public void toDot(RegExp r) {
        out.println("digraph AST {");
        out.println("node [shape=box style=filled fontname=Courier];");
        r.toDot(this, 0);
        out.println("}");
    }

    /** Output a description of an AST node.  The label specifies the
     *  "friendly" description of the node that will be used
     *  in the output, while the nodeNo is an integer value
     *  that uniquely identifies this particular node.  We
     *  return nodeNo+1 at the end of this function, indicating
     *  the next available node number.
     */
    public int node(String label, int nodeNo) {
        out.println(nodeNo + "[label=\"" + label + "\"];");
        return nodeNo+1;
    }
    
    /** Output an edge between the specified pair of nodes
     *  in the appropriate format for the dot tools.
     */
    public void join(int from, int to) {
        out.println(from + " -> " + to + ";");
    }

    //- Generate graphs for state machines -----------------------------

    /** Write a dot description of an automaton to the specified file.
     */
    public static void toDot(State[] states, String name) {
        try {
            FileOutputStream file = new FileOutputStream(name);
            new DotOutput(new PrintStream(file)).toDot(states);
            file.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    /** Create a dot graph for a state machine.
     */
    public void toDot(State[] states) {
        out.println("digraph Automaton {");
        out.println("graph[rankdir=LR];");
        out.println("node [fontname=Courier];");
        out.println("edge [fontname=Courier];");
        out.println("start[style=invis];");
        out.println("start -> 0;");
        for (int i=0; i<states.length; i++) {
            states[i].toDot(this);
        }
        out.println("}");
    }
    /** Output a node corresponding to a state.
     */
    public void state(int num, String label, int accept) {
        // Create a version of the label that is spread
        // over multiple lines, if possible, by breaking at
        // spaces, to reduce the size of the circle that will
        // need to be drawn for this state.
        int len  = label.length();
        int step = 3;
        while (step*(step-1) <= len) {
            step+=2;
        }
        StringBuffer buf = new StringBuffer();
        int          w   = 0;
        for (int i=0; i<len; i++) {
            char c = label.charAt(i);
            if (++w>step && c==' ') {
                buf.append("\\n");
                w = 0;
            } else {
                buf.append(c);
            }
        }
        if (accept>1) {
            buf.append("[");
            buf.append(accept);
            buf.append("]");
        }
        // Output a dot description of this node:
        out.println(num + "[label=\"" + buf + "\", shape="
                        + (accept>0 ? "doublecircle" : "circle") + "];");
    }

    /** Output an edge between the specified pair of nodes
     *  in the appropriate format for the dot tools, including
     *  a label on the edge.
     */
    public void trans(int from, int to, String lab) {
        out.println(from + " -> " + to + "[label=\"" + lab + "\"];");
    }
}
