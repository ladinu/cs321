import java.io.*;
import ast1.*;

class TestReachability {
  public static void main(String [] args) {
    try {
      if (args.length == 1) {
	FileInputStream stream = new FileInputStream(args[0]);
	Ast.Program p = new AstParser(stream).Program();
	stream.close();
	p.setReachability();
	System.out.print(p);
      } else {
	System.out.println("Need an AST file name as command-line argument.");
      }
    } catch (TokenMgrError e) {
      System.err.println(e.toString());
    } catch (ParseException e) {
      System.err.println(e.toString());
    } catch (IOException e) {
      System.err.println(e.toString());
    }     
  }
}
