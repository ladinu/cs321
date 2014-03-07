// This is supporting software for CS321/CS322 Compilers and Language Design.
// Copyright (c) Portland State University
// 
// AST Definition (for miniJava v2.0 - W'14)
//
// (Last modified on 2/6/14 by Jingke Li.)   
//
// With reachability flags on statements, reflected in printing.
//
package ast2;
import java.util.*;

public class Ast {
  static int tab=0;	// indentation for printing AST.

  public abstract static class Node {
    String tab(boolean bang) {
      String str = "";
      int tb = Ast.tab;
      if (bang) {
	str = "!";
	tb = tb - 1;
      }
      for (int i = 0; i < tb; i++)
	str += " ";
      return str;
    }
}

  // Program Node -------------------------------------------------------

  public static class Program extends Node {
    public final ClassDecl[] classes;

    public Program(ClassDecl[] ca) { classes=ca; }
    public Program(List<ClassDecl> cl) { 
      this(cl.toArray(new ClassDecl[0]));
    }
    public String toString() { 
      String str = "# AST Program\n";
      for (ClassDecl c: classes) 
	str += c;
      return str;
    }
    public void setReachability() {
      for (ClassDecl c : classes)
	c.setReachability();
    }
  }   

  // Declarations -------------------------------------------------------

  public static class ClassDecl extends Node {
    public final String nm;	       // class name
    public final String pnm;	       // parent class name (could be null)
    public final VarDecl[] flds;       // fiels
    public final MethodDecl[] mthds;   // methods

    public ClassDecl(String c, String p, VarDecl[] va, MethodDecl[] ma) {
      nm=c; pnm=p; flds=va; mthds=ma;
    }
    public ClassDecl(String c, String p, List<VarDecl> vl, List<MethodDecl> ml) {
      this(c, p, vl.toArray(new VarDecl[0]), ml.toArray(new MethodDecl[0]));
    }
    public String toString() { 
      String str = " ClassDecl " + nm + " " + (pnm==null ? "" : pnm) + "\n"; 
      Ast.tab = 2; 
      for (VarDecl v: flds) 
	str += v;
      for (MethodDecl m: mthds) 
	str += m;
      return str;
    }
    public void setReachability() {
      for (MethodDecl m: mthds) 
	m.setReachability();
    }
  }

  public static class MethodDecl extends Node {
    public final Type t;	    // return type (could be null)
    public final String nm;	    // method name
    public final Param[] params;    // param parameters
    public final VarDecl[] vars;    // local variables
    public final Stmt[] stmts;	    // method body

    public MethodDecl(Type rt, String m, Param[] fa, VarDecl[] va, Stmt[] sa) {
      t=rt; nm=m; params=fa; vars=va; stmts=sa;
    }
    public MethodDecl(Type rt, String m, List<Param> fl, List<VarDecl> vl, List<Stmt> sl) {
      this(rt, m, fl.toArray(new Param[0]), 
	   vl.toArray(new VarDecl[0]), sl.toArray(new Stmt[0]));
    }
    public String toString() { 
      String str = "  MethodDecl " + (t==null ? "void" : t) + " " + nm + " ("; 
      for (Param f: params) 
	str += f + " ";
      str += ")\n";
      Ast.tab = 3; 
      for (VarDecl v: vars) 
	str += v;
      for (Stmt s: stmts) 
	str += s;
      return str;
    }
    public void setReachability() {
      boolean returnStmtFound = false;

      for (Stmt stmt : stmts) {
         if (returnStmtFound) {
            stmt.setUnReachable();
         }
         if (stmt.willExecuteReturnStmt()) {
            returnStmtFound = true;
         }
      }
    }
  }

  public static class VarDecl extends Node {
    public final Type t;     // variable type
    public final String nm;  // variable name
    public final Exp init;   // init expr (could be null)

    public VarDecl(Type at, String v, Exp e) { t=at; nm=v; init=e; }

    public String toString() { 
      return tab(false) + "VarDecl " + t + " " + nm + " " + 
	(init==null ? "()" : init) + "\n"; 
    }
  }

  public static class Param extends Node {
    public final Type t;     // parameter type
    public final String nm;  // parameter name

    public Param(Type at, String v) { t=at; nm=v; }

    public String toString() { 
      return "(Param " + t + " " + nm + ")"; 
    }
  }

  // Types --------------------------------------------------------------

  public static abstract class Type extends Node {}

  public static class IntType extends Type {
    public String toString() { return "IntType"; }
  }

  public static class BoolType extends Type {
    public String toString() { return "BoolType"; }
  }

  public static class ArrayType extends Type {
    public final Type et;  // array element type

    public ArrayType(Type t) { et=t; }

    public String toString() { 
      return "(ArrayType " + et + ")"; 
    }
  }

  public static class ObjType extends Type {
    public final String nm;  // object's class name

    public ObjType(String i) { nm=i; }

    public String toString() { 
      return "(ObjType " + nm + ")"; 
    }
  }

  // Statements ---------------------------------------------------------

  public static abstract class Stmt extends Node {
    public boolean reachable = true; // inital flag value choice is arbitrary
    public abstract void setUnReachable();
    public abstract boolean willExecuteReturnStmt();
  }

  public static class Block extends Stmt {
    public final Stmt[] stmts;

    public Block(Stmt[] sa) { stmts=sa; }
    public Block(List<Stmt> sl) { 
      this(sl.toArray(new Stmt[0])); 
    }
    public String toString() { 
      String s = "";
      if (stmts!=null) {
	s = tab(!reachable) + "{\n";
	Ast.tab++; 
	for (Stmt st: stmts) 
	  s += st;
	Ast.tab--;
	s += tab(!reachable) + "}\n"; 
      }
      return s;
    }
    public void setReachability() {
      boolean returnStmtFound = false;

      for (Stmt stmt : stmts) {
         if (returnStmtFound) 
            stmt.setUnReachable();
         
         if (stmt.willExecuteReturnStmt()) 
            returnStmtFound = true;
      }
    }

    public void setUnReachable() {
       reachable = false;
       for (Stmt s : stmts) {
          s.setUnReachable();
       }
    }

    public boolean willExecuteReturnStmt() {
       setReachability();
       for (Stmt s : stmts) {
          if (s.willExecuteReturnStmt())
             return true;
       }
       return false;
    }

  }

  public static class Assign extends Stmt {
    public final Exp lhs;  
    public final Exp rhs;  

    public Assign(Exp e1, Exp e2) { lhs=e1; rhs=e2; }

    public String toString() { 
      return tab(!reachable) + "Assign " + lhs + " " + rhs + "\n"; 
    }

    public void setUnReachable() {
       reachable = false;
    }

    public boolean willExecuteReturnStmt() {
       return false;
    }
  }

  public static class CallStmt extends Stmt {
    public final Exp obj;     // class object (should NOT be null)
    public final String nm;   // method name
    public final Exp[] args;  // arguments

    public CallStmt(Exp e, String s, Exp[] ea) { 
      obj=e; nm=s; args=ea; 
    }
    public CallStmt(Exp e, String s, List<Exp> el) { 
      this(e, s, el.toArray(new Exp[0])); 
    }
    public String toString() { 
      String s = tab(!reachable) + "CallStmt " + obj + " " + nm + " ("; 
      for (Exp e: args) 
	s += e + " "; 
      s += ")\n"; 
      return s;
    }

    public void setUnReachable() {
       reachable = false;
    }

    public boolean willExecuteReturnStmt() {
       return false;
    }
  }

  public static class If extends Stmt {
    public final Exp cond;  
    public final Stmt s1;   // then clause
    public final Stmt s2;   // else clause (could be null)

    public If(Exp e, Stmt as1, Stmt as2) { cond=e; s1=as1; s2=as2; }

    public String toString() { 
      String str = tab(!reachable) + "If " + cond + "\n"; 
      Ast.tab++; 
      str += s1; 
      Ast.tab--;
      if (s2 != null) {
	str += tab(!reachable) + "Else\n";
	Ast.tab++; 
	str += s2; 
	Ast.tab--;
      }
      return str;
    }

    public void setUnReachable() {
       reachable = false;
       s1.setUnReachable();
       if (s2 != null)
          s2.setUnReachable();
    }

    public boolean willExecuteReturnStmt() {
       if (s2 != null)
          return (s1.willExecuteReturnStmt() && s2.willExecuteReturnStmt());
       else
          return s1.willExecuteReturnStmt();
    }
  }

  public static class While extends Stmt {
    public final Exp cond;
    public final Stmt s;

    public While(Exp e, Stmt as) { cond=e; s=as; }

    public String toString() { 
      String str = tab(!reachable) + "While " + cond + "\n";
      Ast.tab++; 
      str += s; 
      Ast.tab--;
      return str;
    }
    public void setUnReachable() {
       reachable = false;
       s.setUnReachable();
    }

    public boolean willExecuteReturnStmt() {
       return false;
    }
  }   

  public static class Print extends Stmt {
    public final Exp arg;  // (could be null)

    public Print(Exp e) { arg=e; }

    public String toString() { 
      return tab(!reachable) + "Print " + (arg==null ? "()" : arg) + "\n"; 
    }
    public void setUnReachable() {
       reachable = false;
    }

    public boolean willExecuteReturnStmt() {
       return false;
    }
  }

  public static class Return extends Stmt {
    public final Exp val;  // (could be null)

    public Return(Exp e) { val=e; }

    public String toString() { 
      return tab(!reachable) + "Return " + (val==null ? "()" : val) + "\n"; 
    }
    public void setUnReachable() {
       reachable = false;
    }

    public boolean willExecuteReturnStmt() {
       return true;
    }
  }


  // Expressions --------------------------------------------------------

  public static abstract class Exp extends Node {
     public abstract Object cval();
  }

  public static enum BOP {
    ADD("+"), SUB("-"), MUL("*"), DIV("/"), AND("&&"), OR("||"),
    EQ("=="), NE("!="), LT("<"), LE("<="), GT(">"), GE(">=");
    private String name;

    BOP(String n) { name = n; }
    public String toString() { return name; }
  }

  public static enum UOP {
    NEG("-"), NOT("!");
    private String name;

    UOP(String n) { name = n; }
    public String toString() { return name; }
  }

  public static class Binop extends Exp {
    public final BOP op;
    public final Exp e1;
    public final Exp e2;

    public Binop(BOP o, Exp ae1, Exp ae2) { op=o; e1=ae1; e2=ae2; }

    public String toString() { 
      return "(Binop " + op + " " + e1 + " " + e2 + ")";
    }
    public Object cval() {
       if ( op == null || e1 == null || e2 == null || 
            e1.cval() == null || e2.cval() == null ) 
          return null;
       switch (op) {
          case EQ : return e1.equals(e2);
          case NE : return !e1.equals(e2);
          case AND: return (Boolean)e1.cval() && (Boolean)e2.cval();
          case OR : return (Boolean)e1.cval() || (Boolean)e2.cval();

          case LT: return (Integer)e1.cval() < (Integer)e2.cval();
          case LE: return (Integer)e1.cval() <= (Integer)e2.cval();
          case GT: return (Integer)e1.cval() > (Integer)e2.cval();
          case GE: return (Integer)e1.cval() >= (Integer)e2.cval();

          case ADD: return (Integer)e1.cval() + (Integer)e2.cval();
          case SUB: return (Integer)e1.cval() - (Integer)e2.cval();
          case MUL: return (Integer)e1.cval() * (Integer)e2.cval();
          case DIV: return (Integer)e1.cval() / (Integer)e2.cval();

          default: return null;
       }
    }
  }

  public static class Unop extends Exp {
    public final UOP op;
    public final Exp e;

    public Unop(UOP o, Exp ae) { op=o; e=ae; }

    public String toString() { 
      return "(Unop " + op + " " + e + ")";
    }
    public Object cval() {
       if ( op == null || e == null || e.cval() == null) {
          return null;
       } else if (op == UOP.NEG && e.cval() instanceof Integer) {
          return -1 * (Integer)e.cval();
       } else if (op == UOP.NOT && e.cval() instanceof Boolean) {
          return ! (Boolean)e.cval();
       } else {
          return null;
       }
    }
  }

  public static class Call extends Exp {
    public final Exp obj;     // class object (should NOT be null)
    public final String nm;   // method name
    public final Exp[] args;  // arguments

    public Call(Exp e, String s, Exp[] ea) { 
      obj=e; nm=s; args=ea; 
    }
    public Call(Exp e, String s, List<Exp> el) { 
      this(e, s, el.toArray(new Exp[0])); 
    }
    public String toString() { 
      String str ="(Call " + obj + " " + nm + " ("; 
      for (Exp e: args) 
	str += e + " "; 
      str += "))"; 
      return str; 
    }
    public Object cval() { return null; }
  }

  public static class NewArray extends Exp {
    public final Type et;  // element type
    public final int len;  // array length

    public NewArray(Type t, int i) { et=t; len=i; }

    public String toString() { 
      return "(NewArray " + et + " " + len + ")";
    }
    public Object cval() { return null; }
  }

  public static class ArrayElm extends Exp {
    public final Exp ar;   // array object
    public final Exp idx;  // element's index

    public ArrayElm(Exp e1, Exp e2) { ar=e1; idx=e2; }

    public String toString() { 
      return "(ArrayElm " + ar + " " + idx +")";
    }
    public Object cval() { return null; }
  }

  public static class NewObj extends Exp {
    public final String nm;   // class name
    public final Exp[] args;  // arguments to the constructor

    public NewObj(String s, Exp[] ea) { nm=s; args=ea; }
    public NewObj(String s, List<Exp> el) { 
      this(s, el.toArray(new Exp[0])); 
    }
    public String toString() { 
      String str = "(NewObj " + nm + " ("; 
      for (Exp e: args) 
	str += e + " ";
      str += "))"; 
      return str;
    }
    public Object cval() { return null; }
  }

  public static class Field extends Exp {
    public final Exp obj;    // class object
    public final String nm;  // field name

    public Field(Exp e, String s) { obj=e; nm=s; }

    public String toString() { 
      return "(Field " + obj + " " +  nm + ") ";
    }
    public Object cval() { return null; }
  }

  public static class Id extends Exp {
    public final String nm;  // id name

    public Id(String s) { nm=s; }
      //    public String toString() { return "(Id " + nm + ")"; }
    public String toString() { return nm; }
    public Object cval() { return null; }
  }

  public static class This extends Exp {
    public String toString() { return "This"; }
    public Object cval() { return null; }
  }

  public static class IntLit extends Exp {
    public final int i; 
    
    public IntLit(int ai) { i=ai; }
    public String toString() { return i + ""; }
    public Object cval() { return new Integer(i); }
  }

  public static class BoolLit extends Exp {
    public final boolean b;	

    public BoolLit(boolean ab) { b=ab; }
    public String toString() { return b + ""; }
    public Object cval() { return new Boolean(b); }
  }

  public static class StrLit extends Exp {
    public final String s;	

    public StrLit(String as) { s=as; }
    public String toString() { return "\"" + s + "\""; }
    public Object cval() { return null; }
  }

}
