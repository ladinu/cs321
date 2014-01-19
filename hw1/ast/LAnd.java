package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for logical and expressions (&&).
 */
public class LAnd extends BinLogicExpr {

    /** Default constructor.
     */
    public LAnd(Position pos, Expr left, Expr right) {
        super(pos, left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "LAnd"; }

    /** Print out this expression.
     */
    public void print(TextOutput out) { binary(out, "&&"); }

    /** Evaluate this expression.
     */
    public int eval()
      throws Failure { return fromBool(toBool(left.eval()) && toBool(right.eval())); }
}
