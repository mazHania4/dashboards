
#! /bin/bash
echo "COMPILING PARSER"
echo "-JFLEX"
jflex Lexer.flex
mv Lexer.java ../java/com/compi1/parsers/Lexer.java

echo "-CUP"
java -jar /home/hania/java-cup-11b.jar Parser.cup
mv Parser.java ../java/com/compi1/parsers/Parser.java
mv ParserSym.java ../java/com/compi1/parsers/ParserSym.java
