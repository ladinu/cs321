package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for bitwise or expressions (|).
 */
public class BOr extends BinBitwiseExpr {

    /** Default constructor.
     */
    public BOr(Position pos, Expr left, Expr right) {
        super(pos, left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "BOr"; }

    /** Print out this expression.
     */
    public void print(TextOutput out) { binary(out, "|"); }

    /** Constant folding for binary operators with two known integer
     *  arguments.
     */
    Expr fold(int n, int m) { return new IntLit(pos, n|m); }

    /** Simplification of a binary expression when the left operand
     *  (but not the right) is a known integer constant.
     */
    Expr simpL(int m) {
        // Commutative operator: swap operands and then use simpR:
        Expr temp = left; left = right; right = temp;
        return simpR(m);
    }

    /** Simplification of a binary expression when the right operand
     *  (but not the left) is a known integer constant.
     */
    Expr simpR(int m) { return left.simpBOr(this, m);  }

    /** Simplify a bitwise or with a known integer as the right argument.
     */
    Expr simpBOr(BOr orig, int m) {
        IntLit rightInt = right.isIntLit();
        return (rightInt==null)
                ? orig
                : left.newBOr(orig.pos, m | rightInt.getNum());
    }

    /** Evaluate this expression.
     */
    public int eval()
      throws Failure { return left.eval() | right.eval(); }
}
