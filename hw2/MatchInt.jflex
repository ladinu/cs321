// Given a HTML file that is generated from MiniColor,
// this program remove all the HTML tags leaving only the 
// original mini program
//
//    $ jflex Uncolor.jflex
//    $ javac Uncolor.java
//    $ java Uncolor input.html
%%

%standalone

%class MatchInt


positiveDigit    = [1-9]
digit            = [0-9]

tens             = {positiveDigit}{digit}
hundreds         = {positiveDigit}{digit}{digit}
thousands        = {positiveDigit}{digit}{digit}{digit}
tenThousands     = {positiveDigit}{digit}{digit}{digit}{digit}
hundredThousands = {positiveDigit}{digit}{digit}{digit}{digit}{digit}
million          = {positiveDigit}{digit}{digit}{digit}{digit}{digit}{digit}
tenMillion       = {positiveDigit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}
hundredMillion   = {positiveDigit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}

match0  = 1{digit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}
match1  = 20{digit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}
match2  = 21[0-3]{digit}{digit}{digit}{digit}{digit}{digit}{digit}
match3  = 214[0-6]{digit}{digit}{digit}{digit}{digit}{digit}
match4  = 2147[0-3]{digit}{digit}{digit}{digit}{digit}
match5  = 21474[0-7]{digit}{digit}{digit}{digit}
match6  = 214748[0-2]{digit}{digit}
match7  = 2147483[0-5]{digit}{digit}
match8  = 21474836[1-3]{digit}
match9  = 214748364[0-6]
match10 = 2147483647

ints = {positiveDigit} | {tens}       | {hundreds}       | {thousands} | {tenThousands} | {hundredThousands} |
       {million}       | {tenMillion} | {hundredMillion} | {match0}    | {match1}       | {match2}           |
       {match3}        | {match4}     | {match5}         | {match6}    | {match7}       | {match8}           |
       {match9}        | {match10}

i = {tens} | {hundreds}

newline = \n
%%

{i}   { System.out.println(yytext()); }
{ints}   { System.out.println("mmm"); }
{newline}   { /* ignore */ }

