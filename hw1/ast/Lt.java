package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for less than expressions.
 */
public class Lt extends BinCompExpr {

    /** Default constructor.
     */
    public Lt(Position pos, Expr left, Expr right) {
        super(pos, left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Lt"; }

    /** Print out this expression.
     */
    public void print(TextOutput out) { binary(out, "<"); }

    /** Constant folding for binary operators with two known integer
     *  arguments.
     */
    Expr fold(int n, int m) { return new BoolLit(pos, n<m); }

    /** Evaluate this expression.
     */
    public int eval()
      throws Failure { return fromBool(left.eval() <  right.eval()); }
}
