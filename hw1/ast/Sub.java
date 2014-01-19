package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for subtract expressions.
 */
public class Sub extends BinArithExpr {

    /** Default constructor.
     */
    public Sub(Position pos, Expr left, Expr right) {
        super(pos, left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Sub"; }

    /** Print out this expression.
     */
    public void print(TextOutput out) { binary(out, "-"); }

    /** Constant folding for binary operators with two known integer
     *  arguments.
     */
    Expr fold(int n, int m) { return new IntLit(pos, n-m); }

    /** Evaluate this expression.
     */
    public int eval()
      throws Failure { return left.eval() - right.eval(); }
}
