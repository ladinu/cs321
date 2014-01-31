import java.io.PrintStream;

/** Abstract syntax (base class) for expressions.
 */
abstract class Expr {
  // Indentation: --------------------------------------------------
  public void indent() { indent(0); }

  // node-specific worker function:
  abstract void indent(int n);

  // general utility function:
  protected void indent(int n, String str) {
    for (int i=0; i<n; i++) {
      System.out.print("  ");
    }
    System.out.println(str);
  }

  // Graphviz trees: -----------------------------------------------
  public void toDot(String filename) {
    try {
      PrintStream out = new PrintStream(filename);
      out.println("digraph AST {");
      out.println("node [shape=box style=filled fontname=Courier];");
      toDot(out, 0);
      out.println("}");
      out.close();
    } catch (Exception e) {
      System.err.println("Exception: " + e);
    }
  }

  // node-specific worker function:
  abstract int toDot(PrintStream out, int n);

  // general utility functions:
  protected void node(PrintStream out, int n, String lab) {
    out.println(n + "[label=\"" + lab + "\"];");
  }

  protected void edge(PrintStream out, int src, int dst) {
    out.println(src + " -> " + dst + ";");
  }
}

/** Abstract syntax for integer (literal) expressions.
 */
class IntExpr extends Expr {
    private int i;
    IntExpr(int i) { this.i = i; }

    void indent(int n) {
      indent(n, Integer.toString(i));
    }

    int toDot(PrintStream out, int n) {
      node(out, n, Integer.toString(i));
      return n+1;
    }
}

/** Abstract syntax base class for binary expressions.
 */
abstract class BinExpr extends Expr {
    protected Expr l;
    protected Expr r;
    BinExpr(Expr l, Expr r) { this.l = l; this.r = r; }

    abstract String label();

    void indent(int n) {
      indent(n, label());
      l.indent(n+1);
      r.indent(n+1);
    }

    int toDot(PrintStream out, int n) {
      node(out, n, label());     // output this node
      int ln = n + 1;            // find id for left child
      edge(out, n, ln);          // output edge to left child
      int rn = l.toDot(out, ln); // output left child, find id for right
      edge(out, n, rn);          // output edge to right child
      return r.toDot(out, rn);   // output right child
    }
}

/** Abstract syntax for addition expressions.
 */
class AddExpr extends BinExpr {
    AddExpr(Expr l, Expr r) { super(l,r); }
    String label() { return "Add"; }
}

/** Abstract syntax for subtraction expressions.
 */
class SubExpr extends BinExpr {
    SubExpr(Expr l, Expr r) { super(l,r); }
    String label() { return "Sub"; }
}

/** Abstract syntax for multiplication expressions.
 */
class MulExpr extends BinExpr {
    MulExpr(Expr l, Expr r) { super(l,r); }
    String label() { return "Mul"; }
}

/** Abstract syntax for identifier (variable) expressions.
 */
class VarExpr extends Expr {
    private String var;
    VarExpr(String var) { this.var = var; }

    void indent(int n) {
      indent(n, var);
    }

    int toDot(PrintStream out, int n) {
      node(out, n, var);
      return n+1;
    }
}

/** Abstract syntax for assignment expressions.
 */
class AssignExpr extends Expr {
    private String lhs;
    private Expr   rhs;
    AssignExpr(String lhs, Expr rhs) { this.lhs = lhs; this.rhs = rhs; }

    void indent(int n) {
      indent(n, lhs + " =");
      rhs.indent(n+1);
    }

    int toDot(PrintStream out, int n) {
      node(out, n, lhs + " =");
      edge(out, n, n+1);
      return rhs.toDot(out, n+1);
    }
}

