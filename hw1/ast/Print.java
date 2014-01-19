package ast;
import compiler.Failure;
import compiler.Position;

/** Abstract syntax for print statements.
 */
public class Print extends PosStmt {

    /** The value that should be printed out.
     */
    private Expr exp;

    /** Default constructor.
     */
    public Print(Position pos, Expr exp) {
        super(pos);
        this.exp = exp;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Print");
        exp.indent(out, n+1);
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void print(TextOutput out, int n) {
        out.indent(n);
        out.print("print ");
        exp.print(out);
        out.println(";");
    }

    /** Output a description of this node (with id n) in dot format,
     *  adding an extra node for each subtree.
     */
    public int toDot(DotOutput dot, int n) {
        return exp.toDot(dot, n, "exp",
               node(dot, "Print", n));
    }

    /** Run scope analysis on this statement.  The scoping parameter
     *  provides access to the scope analysis phase (in particular,
     *  to the associated error handler), and the env parameter
     *  reflects the environment at the start of the statement.  The
     *  return result is the environment at the end of the statement.
     */
    public Env analyze(ScopeAnalysis scoping, Env env) {
        exp.analyze(scoping, env);
        return env;
    }

    /** Generate a dot description for the environment structure of this
     *  program.
     */
    public void dotEnv(DotEnvOutput dot) {
        /* nothing to do here */
    }

    /** Run type checker on this statement.  The typing parameter
     *  provides access to the scope analysis phase (specifically,
     *  to the associated error handler).
     */
    public void analyze(TypeAnalysis typing) {
        exp.require(typing, Type.INT);
    }

    /** Run initialization analysis on this statement.  The init
     *  parameter provides access to an initialization analysis phase
     *  object (specifically, to an associated error handler).  The
     *  initialized parameter is the set of variables (each represented
     *  by pointers to environment entries) that have definitely been
     *  initialized before this statement is executed.
     */
    public VarSet analyze(InitAnalysis init, VarSet initialized) {
        return exp.analyze(init, initialized);
    }

    /** Attempt to simplify all of the expressions in this statement.
     */
    public void simplify() {
        exp = exp.simplify();
    }

    /** Execute this program.
     */
    public void exec()
      throws Failure {
        System.out.println(exp.eval());
    }
}
