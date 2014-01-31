import java.io.PrintStream;

/** Abstract syntax (base class) for expressions.
 */
abstract class Expr {
    abstract int eval();
}

/** Abstract syntax for integer (literal) expressions.
 */
class IntExpr extends Expr {
    private int i;
    IntExpr(int i) { this.i = i; }

    int eval() { return i; }
}

/** Abstract syntax base class for binary expressions.
 */
abstract class BinExpr extends Expr {
    protected Expr l;
    protected Expr r;
    BinExpr(Expr l, Expr r) { this.l = l; this.r = r; }
}

/** Abstract syntax for addition expressions.
 */
class AddExpr extends BinExpr {
    AddExpr(Expr l, Expr r) { super(l,r); }

    int eval() { return l.eval() + r.eval(); }
}

/** Abstract syntax for subtraction expressions.
 */
class SubExpr extends BinExpr {
    SubExpr(Expr l, Expr r) { super(l,r); }

    int eval() { return l.eval() - r.eval(); }
}

/** Abstract syntax for multiplication expressions.
 */
class MulExpr extends BinExpr {
    MulExpr(Expr l, Expr r) { super(l,r); }

    int eval() { return l.eval() * r.eval(); }
}
