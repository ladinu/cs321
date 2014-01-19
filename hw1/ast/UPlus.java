package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for unary plus expressions.
 */
public class UPlus extends UnArithExpr {

    /** Default constructor.
     */
    public UPlus(Position pos, Expr exp) {
        super(pos, exp);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "UPlus"; }

    /** Print out this expression.
     */
    public void print(TextOutput out) { unary(out, "+"); }

    /** Rewrite this expression using algebraic identities to reduce
     *  the amount of computation that is required at runtime.  The
     *  algorithms used here implement a range of useful optimizations
     *  including, for example:
     *     x + 0  ==>  x
     *     n + m  ==>  (n+m)           if n,m are known integers
     *     (x + n) +m ==>  x + (n+m)   if n,m are known integers
     *  etc. with corresponding rules for *, &, |, and ^.  However,
     *  there are still plenty of other opportunities for simplification,
     *  including:
     *    identies/constant folding on booleans
     *    removing double negations, complements, etc...
     *    distributivity properties, such as (x+n)+(y+m) ==> (x+y)+(n+m)
     *    and so on ...
     */
    Expr simplify() {
        return exp.simplify();
    }

    /** Evaluate this expression.
     */
    public int eval()
      throws Failure { return +exp.eval(); }
}
