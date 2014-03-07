import java.io.*;
import ast2.*;

class Test {
  public static void main(String [] args) {
	Ast.Exp e1 = new Ast.IntLit(3);
	Ast.Exp e2 = new Ast.Binop(Ast.BOP.ADD, new Ast.IntLit(2), 
          new Ast.IntLit(2));


    Ast.Binop u = new Ast.Binop(Ast.BOP.LE, e1, e2);
	System.out.println(u.cval());
  }
}
