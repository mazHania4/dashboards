package com.compi1.parsers;
import java_cup.runtime.*;

%%

%public
%class Lexer
%type java_cup.runtime.Symbol
%cup
%unicode
%line
%column

whitespace = [ \t]+
newline = [\n\r]+
hex = \"#([0-9a-fA-F]{6})\"
string = \"[\w \n\r,@%!¡¿?#<>&*+=:;\.\(\)\[\]\{\}\/\|\-\$]+\"
id = [\w]+
number = \d*\.?\d*
link = \"https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)\"

%{
    StringBuffer stringBuffer = new StringBuffer();

    private  Symbol symbol(int type){
        return new Symbol(type, yyline+1, yycolumn+1);
    }
    private  Symbol symbol(int type, Object value){
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }
%}

%eofval{
    return symbol(ParserSym.EOF);
%eofval}

%%
    //reserved words for control structures
    "if" { return symbol(ParserSym.IF); }
    "else" { return symbol(ParserSym.ELSE); }
    "for" { return symbol(ParserSym.FOR); }
    "do" { return symbol(ParserSym.DO); }
    "while" { return symbol(ParserSym.WHILE); }
    "true" { return symbol(ParserSym.TRUE); }
    "false" { return symbol(ParserSym.FALSE); }
    //reserved words for graphs and dashboard
    "\"title\""  { return symbol(ParserSym.TITTLE); }
    "\"description\""  { return symbol(ParserSym.DESCRIPTION); }
    "\"keywords\""  { return symbol(ParserSym.KEYWORDS); }
    "\"header\""  { return symbol(ParserSym.HEADER); }
    "\"footer\""  { return symbol(ParserSym.FOOTER); }
    "\"copyright\""  { return symbol(ParserSym.COPYRIGHT); }
    "\"backgroundColor\""  { return symbol(ParserSym.BG_COLOR); }
    "\"fontFamily\""  { return symbol(ParserSym.FONT_FAM); }
    "\"fontSize\""  { return symbol(ParserSym.FONT_SIZE); }
    "\"data\""  { return symbol(ParserSym.DATA); }
    "\"category\""  { return symbol(ParserSym.CATEGORY); }
    "\"value\""  { return symbol(ParserSym.VALUE); }
    "\"label\""  { return symbol(ParserSym.LABEL); }
    "\"x\""  { return symbol(ParserSym.X); }
    "\"y\""  { return symbol(ParserSym.Y); }
    "\"name\""  { return symbol(ParserSym.NAME); }
    "\"points\""  { return symbol(ParserSym.POINTS); }
    "\"color\""  { return symbol(ParserSym.COLOR); }
    "\"size\""  { return symbol(ParserSym.SIZE); }
    "\"lineStyle\""  { return symbol(ParserSym.LINE_STYLE); }
    "\"icon\""  { return symbol(ParserSym.ICON); }
    "\"link\""  { return symbol(ParserSym.LINK); }
    "\"chart\""  { return symbol(ParserSym.CHART); }
    "\"xAxisLabel\""  { return symbol(ParserSym.X_AXIS_LBL); }
    "\"yAxisLabel\""  { return symbol(ParserSym.Y_AXIS_LBL); }
    "\"legendPosition\""  { return symbol(ParserSym.LEGEND_POS); }
    // symbols
    ":" { return symbol(ParserSym.COLON); }
    ";" { return symbol(ParserSym.SEMICOLON); }
    "," { return symbol(ParserSym.COMMA); }
    "{" { return symbol(ParserSym.OP_BRACE); }
    "}" { return symbol(ParserSym.CL_BRACE); }
    "[" { return symbol(ParserSym.OP_BRACKET); }
    "]" { return symbol(ParserSym.CL_BRACKET); }
    "(" { return symbol(ParserSym.OP_PAREN); }
    ")" { return symbol(ParserSym.CL_PAREN); }
    "+" { return symbol(ParserSym.PLUS); }
    "++" { return symbol(ParserSym.INCREMENT); }
    "-" { return symbol(ParserSym.MINUS); }
    "--" { return symbol(ParserSym.DECREMENT); }
    "*" { return symbol(ParserSym.MULT); }
    "/" { return symbol(ParserSym.DIV); }
    "=" { return symbol(ParserSym.ASSIGN); }
    "+=" { return symbol(ParserSym.PLUS_ASSIGN); }
    "-=" { return symbol(ParserSym.MINUS_ASSIGN); }
    "*=" { return symbol(ParserSym.MULT_ASSIGN); }
    "/=" { return symbol(ParserSym.DIV_ASSIGN); }
    "==" { return symbol(ParserSym.EQUALS); }
    "!=" { return symbol(ParserSym.DIFFERENT); }
    "<" { return symbol(ParserSym.LESS_THAN); }
    "<=" { return symbol(ParserSym.LESS_EQUAL_THAN); }
    ">" { return symbol(ParserSym.MORE_THAN); }
    ">=" { return symbol(ParserSym.MORE_EQUAL_THAN); }

    {hex} {
        String val = yytext().substring(1, yytext().length()-1);
        return symbol(ParserSym.HEX, val);}
    {link} {
            String val = yytext().substring(1, yytext().length()-1);
            return symbol(ParserSym.URL, val);}
    {string} {
        String val = yytext().substring(1, yytext().length()-1);
        return symbol(ParserSym.STRING, val);}
    {id} { return symbol(ParserSym.ID, yytext()); }
    {number} { return symbol(ParserSym.NUMBER, yytext()); }
    {whitespace} { }
    {newline} { }
    [^] { return symbol(ParserSym.LEXICAL_ERROR); }
