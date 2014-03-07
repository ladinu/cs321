import java.io.*;
import ast2.*;

class Test {
  public static void main(String [] args) {
	Ast.Exp e = new Ast.IntLit(4);
	System.out.println(e.cval());

	e = new Ast.BoolLit(false);
    Ast.Unop u = new Ast.Unop(Ast.UOP.NEG, e);
	System.out.println(u.cval());
  }
}
