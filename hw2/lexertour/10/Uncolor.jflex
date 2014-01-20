// Given a HTML file that is generated from MiniColor,
// this program remove all the HTML tags leaving only the 
// original mini program
%%

%standalone

%class Uncolor


// HTML classes
minicolorClasses           = "comment" | "keyword" | "literal"
lt                         = "&lt;"
gt                         = "&gt;"
amp                        = "&amp;"

// The regular expressions
htmlTag                    = ("<html>"\n) | (\n"</html>")
bodyTag                    = ("<body>"\n) | (\n"</body>")
headTag                    = "<head>"\n (.|\n)* "</head>"\n
spanTag                    = "<span class=\""{minicolorClasses}"\">" | "</span>"

ignoreHtml                 = {htmlTag} | {bodyTag} | {headTag} | {spanTag}

%%

{ignoreHtml}   { /* ignore */ }

{lt}           { System.out.print("<"); }
{gt}           { System.out.print(">"); }
{amp}          { System.out.print("&"); }
