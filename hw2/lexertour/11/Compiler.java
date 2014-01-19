// (Comparable to minitour, version 06)
import compiler.*;
import mini.*;
import java.io.FileReader;

public class Compiler {
  public static void main(String[] args) {
    Handler handler = new SimpleHandler();
    try {
      if (args.length!=1) {
        throw new Failure("This program requires exactly one argument");
      }

      // Read program:
      String     input  = args[0] + ".mini";
      FileReader reader = new FileReader(input);
      MiniLexer  lexer  = new MiniLexer(reader, handler);

      while ((lexer.nextToken())!=MiniTokens.ENDINPUT) {
        System.out.println(lexer.getToken() + "\t" + lexer.showToken());
      }
      System.out.println("ENDINPUT");
        
    } catch (Failure f) {
      handler.report(f);
    } catch (Exception e) { 
      handler.report(new Failure("Exception: " + e));
    }     
  }     
}
