package ast;
import compiler.Failure;
import compiler.Position;

class VarSet {

    private Env head;

    private VarSet rest;

    /** Default constructor.
     */
    VarSet(Env head, VarSet rest) {
        this.head = head;
        this.rest = rest;
    }

    /** Test to see if a given variable appears in the specified variable set.
     */
    public static boolean includes(Env cand, VarSet vars) {
        for (; vars!=null; vars=vars.rest) {
            if (vars.head==cand) {
                return true;
            }
        }
        return false;
    }

    /** Trim an initial prefix from the front of the VarSet start, up to
     *  but not included the VarSet end that is assumed to be a tail of
     *  start.
     */
    public static VarSet trim(VarSet start, VarSet end) {
        if (start==end) {
            return null;
        } else {
            VarSet curr = start;
            while (curr.rest!=null && curr.rest!=end) {
                curr = curr.rest;
            }
            curr.rest = null;
            return start;
        }
    }

    /** Compute the union of two variable sets; destructive operations
     *  are allowed on the left/first argument, but not on the right/second.
     */
    public static VarSet union(VarSet left, VarSet right) {
        while (left!=null) {
            if (includes(left.head, right)) {
                left = left.rest;
            } else {
                VarSet temp = left.rest;
                left.rest   = right;
                right       = left;
                left        = temp;
            }
        }
        return right;
    }

    /** Compute the intersection of two variable sets; destructive operations
     *  are allowed on the left/first argument, but not on the right/second.
     */
    public static VarSet intersect(VarSet left, VarSet right) {
        VarSet result = null;
        while (left!=null) {
            if (includes(left.head, right)) {
                VarSet temp = left.rest;
                left.rest   = result;
                result      = left;
                left        = temp;
            } else {
                left = left.rest;
            }
        }
        return result;
    }

    /** Return a printable representation of this set of variables.
     */
    public static String toString(VarSet vs) {
        StringBuffer buf   = new StringBuffer("{");
        for (boolean first = true; vs!=null; vs=vs.rest) {
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append(vs.head.getId().getName());
        }
        buf.append("}");
        return buf.toString();
    }
}
