package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for unary minus expressions.
 */
public class UMinus extends UnArithExpr {

    /** Default constructor.
     */
    public UMinus(Position pos, Expr exp) {
        super(pos, exp);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "UMinus"; }

    /** Print out this expression.
     */
    public void print(TextOutput out) { unary(out, "-"); }

    /** Constant folding for unary operators with a known integer
     *  argument.
     */
    Expr fold(int n) { return new IntLit(pos, -n); }

    /** Evaluate this expression.
     */
    public int eval()
      throws Failure { return -exp.eval(); }
}
