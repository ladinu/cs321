package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for unary expressions that operate on
 *  numeric arguments.
 */
public abstract class UnArithExpr extends UnExpr {

    /** Default constructor.
     */
    public UnArithExpr(Position pos, Expr exp) {
        super(pos, exp);
    }

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
        return type = exp.require(typing, Type.INT);
    }
}
