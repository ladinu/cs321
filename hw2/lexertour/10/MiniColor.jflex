// 10 jflex can be used to build useful utilities!

/** This is an example of a syntax coloring program for the mini
 *  programming language that has been written using the jflex
 *  lexical analyzer generator (see http://www.jflex.de).
 */

%%

// We use the %standalone declaration, which tells jflex that we want to
// build a self-contained program.  This will result in the definition of a
// main function that reads a file name on the command line and then passes
// all of the text from that file through the generated scanner/lexer.  We
// will produce the corresponding syntax colored output simply by printing
// it on to the standard output device; this makes it easy to inspect the
// output while debugging, but we can also use a redirect operator to
// save the contents in an HTML file instead that is suitable for viewing
// in a browser.

%standalone

// Next, we use the %class declaration, selecting MiniColor as the name
// for the Java class that will be generated:

%class MiniColor

// Assuming that the JFlex source shown here is stored in MiniColor.jflex,
// we will now be able to generate and compile our program using the
// following commands:
// 
//   jflex MiniColor.jflex 
//   javac MiniColor.java
//
// The resulting program can then be run as follows:
//   
//   java MiniColor squares.mini  > sq.html

// Every HTML file that we generate should begin with set of lines to set
// basic details such as the title and the style options.  The %init
// feature of jflex provides An easy way to handle this, allowing us to
// specify some code that should be executed as part of the constructor for
// the MiniColor constructor; in this case, we just need to print the
// initial set of lines for the start of the HTML output.  (This would also
// be a good place to initialize an output file if we wanted to write the
// output from the program directly to a file instead of just printing it
// on the standard output.)

%init{
  System.out.println("<html>");
  System.out.println("<head>");
  System.out.println("<title>My Syntax Colored Web Page</title>");
  System.out.println("<style type=\"text/css\">");
  System.out.println("  body     {white-space:pre;");
  System.out.println("            background-color:#ffffcc;");
  System.out.println("            color:black;");
  System.out.println("            font-family:\"Lucida Console\",\"Courier New\",Monotype}");
  System.out.println("  .keyword {color:blue}");
  System.out.println("  .comment {color:orange}");
  System.out.println("  .literal {color:green}");
  System.out.println("  .invalid {color:red}");
  System.out.println("  .multiple {font-weight: bold}");
  System.out.println("</style>");
  System.out.println("<script type=\"text/javascript\">window.onload = function(){setInterval(function(){location.reload()}, 3000);}</script>");
  System.out.println("</head>");
  System.out.println("<body>");
%init}

// There is a similar %eof feature that allows us to specify code that
// will be executed when the program reaches the end of the input file.
// This provides a convenient place for us to put the code that produces
// the HTML lines that are required at the end of every output file:

%eof{
  System.out.println("</body>");
  System.out.println("</html>");
%eof}

// In general, our syntax coloring program will work by matching
// patterns in the input file and then outputing the original lexeme,
// sometimes wrapped between HTML tags to specify how the lexeme
// should be colored.  One small technical challenge in this is that
// we need to make sure that the three characters <, >, and &, which
// have special uses in HTML, are replaced by the appropriate escape
// sequences &lt;, &gt:, and &amp;, respectively in the output.
// In principle, it would be possible to handle this completely within
// the main set of jflex matching rules.  However, in this particular
// case, it seems easier (at least to this author) just to handle
// this with a little bit of Java code as shown in the following
// echo() function.  This function works by reading characters one
// at a time from the internal buffer that JFlex uses, checking for
// the three special cases, and performing the necessary output
// action each time.

%{
  void echo() {
    int len = yylength();       // Find length of current lexeme
    for (int i=0; i<len; i++) { // Run through each character in turn
      char c = yycharat(i);   
      switch (c) {              // and translate as appropriate ...
        case '<' : System.out.print("&lt;");  break;
        case '>' : System.out.print("&gt;");  break;
        case '&' : System.out.print("&amp;"); break;
        default  : System.out.print(c);       break;
      }
    }
  }
%}

// As a small technical aside, note that we have used a combination
// of the yylength() and yycharat() functions to access the text of
// the current lexeme.  We could achieve a similar effect using the
// yytext() function, but the approach used here is faster (at least
// in principle) because it avoids the overheads of constructing a
// new string object for each input element.

// The echo() method described above is good for printing the text
// of a lexeme directly, without any special syntax coloring.  For
// lexemes where we want to add some color, however, we can use the
// following tag() method, which adds an appropriate span tag before
// and after the lexeme text.

%{
  void tag(String cl) {
    System.out.print("<span class=\"" + cl + "\">");
    echo();
    System.out.print("</span>");
  }
%}

// The parameter cl is used to specify a particular token class/style;
// given the opening lines of HTML shown above, this should be one of
// "keyword", "comment", "literal", or "invalid".  We can define some
// quick helper methods for each of these four cases as follows:

%{
  void keyword()     { tag("keyword"); }
  void comment()     { tag("comment"); }
  void literal()     { tag("literal"); }
  void multiple4()   { tag("multiple"); }
  void invalid_lit() { tag("invalid"); }
  void invalid()     { tag("invalid"); }
%}

// Now we are ready to give regular expressions for each of the input
// elements that can appear in a valid mini program.  We will use the
// following rules to specify the syntax of identifiers, whitespace,
// and comments:

Identifier         = [:jletter:] [:jletterdigit:]*

LineTerminator     = \r|\n|\r\n
WhiteSpace         = {LineTerminator} | [ \t\f] 
InputCharacter     = [^\r\n]

Comment            = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment   = "//" {InputCharacter}* {LineTerminator}

// All that remains now is to define some rules for matching the different
// tokens that can appear in a valid mini program with corresponding actions
// to generate the appropriate output in each case.  As such, this puts us
// into the last section of our jflex input file:

posD             = [1-9]
d                = [0-9]

tens             = {posD}{d}
hundreds         = {posD}{d}{d}
thousands        = {posD}{d}{d}{d}
tenThousands     = {posD}{d}{d}{d}{d}
hundredThousands = {posD}{d}{d}{d}{d}{d}
million          = {posD}{d}{d}{d}{d}{d}{d}
tenMillion       = {posD}{d}{d}{d}{d}{d}{d}{d}
hundredMillion   = {posD}{d}{d}{d}{d}{d}{d}{d}{d}

match0           = 1{d}{d}{d}{d}{d}{d}{d}{d}{d}
match1           = 2[0-0]{d}{d}{d}{d}{d}{d}{d}{d}
match2           = 21[0-3]{d}{d}{d}{d}{d}{d}{d}
match3           = 214[0-6]{d}{d}{d}{d}{d}{d}
match4           = 2147[0-3]{d}{d}{d}{d}{d}
match5           = 21474[0-7]{d}{d}{d}{d}
match6           = 214748[0-2]{d}{d}{d}
match7           = 2147483[0-5]{d}{d}
match8           = 21474836[0-3]{d}
match9           = 214748364[0-6]
match10          = 2147483647


invalid_lit_0    = 0
invalid_lit_1    = 214748364[8-9]
invalid_lit_2    = 21474836[5-9]{d}
invalid_lit_3    = 2147483[7-9]{d}{d}
invalid_lit_4    = 214748[4-9]{d}{d}{d}
invalid_lit_5    = 21474[9-9]{d}{d}{d}{d}
invalid_lit_6    = 2147[5-9]{d}{d}{d}{d}{d}
invalid_lit_7    = 214[8-9]{d}{d}{d}{d}{d}{d}
invalid_lit_8    = 21[5-9]{d}{d}{d}{d}{d}{d}{d}
invalid_lit_9    = 2[2-9]{d}{d}{d}{d}{d}{d}{d}{d}
invalid_lit_10   = [3-9]{d}{d}{d}{d}{d}{d}{d}{d}{d}
invalid_lit_11   = {d}{d}{d}{d}{d}{d}{d}{d}{d}{d}*

// RegExps for matching positive ints that are multiples of 4
// For ints that have 3 or more digits, the last two digit is 
// goung to be a pattern. This pattern is all the multiples of 4
// which have 1 or 2 digits i.e {4, 8, 12, 16, 20, 24, ..., 96}

case1          = "12" | "16" | "20" | "24" | "28" | "32" | "36" | 
                 "40" | "44" | "48" | "52" | "56" | "60" | "64" | 
                 "68" | "72" | "76" | "80" | "84" | "88" | "92" | 
                 "96" | "04" | "08"

case2          = "00"

twoDigitMul4   = {case1}|{case2}

multiplesOf_4  = "4" | "8" | {case1} | [1-9][0-9]*{twoDigitMul4}


// Combine 
invalid_lit    = {invalid_lit_0} | {invalid_lit_1}  | {invalid_lit_2}  |
                 {invalid_lit_3} | {invalid_lit_4}  | {invalid_lit_5}  |
                 {invalid_lit_6} | {invalid_lit_7}  | {invalid_lit_8}  |
                 {invalid_lit_9} | {invalid_lit_10} | {invalid_lit_11}

literal        = {posD}           | {tens}             | {hundreds} | {thousands}  |
                 {tenThousands}   | {hundredThousands} | {million}  | {tenMillion} |
                 {hundredMillion} | {match0}           | {match1}   | {match2}     |
                 {match3}         | {match4}           | {match5}   | {match6}     |
                 {match7}         | {match8}           | {match9}   | {match10}

%%

// Once again, we can adopt the definitions for mini tokens that were
// provided in the original mini lexer.  We group the tokens here so
// that multiple patterns can be combined into a single regular expression
// and then share a single action.

// Basic punctuation and operator symbols are echoed directly to the output
// without any syntax coloring:

","  | "["  | "]"  | "("  | ")"  | "{"  | "}" | ";" |
"="  | "==" | ">"  | ">=" | "<"  | "<=" | "!" | "~" |
"!=" | "&"  | "&&" | "|"  | "||" | "^"  | "*" | "+" |
"-" | "/"       { echo(); }

// Keywords are matched and displayed using "keyword" tag:

"int"   | "boolean" | "while" |
"if"    | "else"    | "print"
                { keyword(); }

// TESTING
{multiplesOf_4} { multiple4(); }


// Integer literals are matched and displayed using the "literal" tag:
{literal}       { literal(); }


// Mark numbers bigger than 2147483647 as invalid

{invalid_lit}   { invalid_lit(); }


// Comments are matched and displayed using the "comment" tag:

{Comment}       { comment(); }

// Finally, identifiers and whitespace are output without any coloring
// annotations.  We could have combined these regular expressions with each
// other and with the rule for punctuation given previously.  However, we
// have chosen not to do that.  One reason is that there is at least a
// conceptual difference between these types of input elements (even if they
// are colored in the same way).  A second reason is that we want to make
// sure the rule for identifiers comes after the rule for keywords to ensure
// that keywords are colored using the earlier rule.

{Identifier}    { echo(); }
{WhiteSpace}    { echo(); }

// This completes the list of all valid tokens that can appear in a mini
// program, but we will end our list of lexer rules with a catch all that
// matches any input not already matched and aborts the program with an
// "Invalid input" error.

.|\n            { System.err.println("Invalid input");
                  System.exit(1);
                }

// ---------------------------------------------------------------------
