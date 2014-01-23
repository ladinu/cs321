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

hundredMillion   = 1{digit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}
hundredMillion   = 20{digit}{digit}{digit}{digit}{digit}{digit}{digit}{digit}
hundredMillion   = 21[0-3]{digit}{digit}{digit}{digit}{digit}{digit}{digit}
hundredMillion   = 214[0-6]{digit}{digit}{digit}{digit}{digit}{digit}
hundredMillion   = 2147[0-3]{digit}{digit}{digit}{digit}{digit}
hundredMillion   = 21474[0-7]{digit}{digit}{digit}{digit}
hundredMillion   = 214748[0-2]{digit}{digit}
hundredMillion   = 2147483[0-5]{digit}{digit}
hundredMillion   = 21474836[1-3]{digit}
hundredMillion   = 214748364[0-6]
hundredMillion   = 2147483647

2147483647
%%

{lb}   { /* ignore */ }

