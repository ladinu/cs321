package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for bitwise not expressions (~).
 */
public class BNot extends UnExpr {

    /** Default constructor.
     */
    public BNot(Position pos, Expr exp) {
        super(pos, exp);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "BNot"; }

    /** Print out this expression.
     */
    public void print(TextOutput out) { unary(out, "~"); }

    /** Run type checking analysis on this expression.  The typing parameter
     *  provides access to the scope analysis phase (in particular,
     *  to the associated error handler), and the env parameter
     *  reflects the environment in which the expression is evaluated.
     *  Unlike scope analysis for statements, there is no return
     *  result here: an expression cannot introduce new variables in
     *  to a program, so the final environment will always be the same
     *  as the initial environment.
     */
    public Type analyze(TypeAnalysis typing) {
        return type = exp.require(typing, Type.INT, Type.BOOLEAN);
    }

    /** Constant folding for unary operators with a known integer
     *  argument.
     */
    Expr fold(int n) { return new IntLit(pos, ~n); }

    /** Evaluate this expression.
     */
    public int eval()
      throws Failure {
        // We need a type test here because we need different implementations
        // of this operator for the int and boolean types.  A simple bitwise
        // negate works fine for integers, but maps the 0 and 1 values that
        // we are using to represent Booleans to non Boolean values.
        if (type==Type.BOOLEAN) {
            return exp.eval() ^ 1;
        } else {
            return ~exp.eval();
        }
      }
}
