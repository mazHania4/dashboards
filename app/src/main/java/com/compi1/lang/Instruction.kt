package com.compi1.lang

class Instruction (
    val type: InstrType,
    val line: Int,
    val id: String,
){
    var operator: AsOperator? = null;
    var comparison: Comparison? = null;
    var value: Value? = null;
    var expression: Expression? = null;
}

class Comparison (
    val type: ComparisonType,
){
    var value: Value? = null;
    var expr1: Expression? = null;
    var expr2: Expression? = null;
    var string1: String = " ";
    var string2: String = " ";
    var comparator: Comparator? = null;
}

class Expression (
    val isJustOne: Boolean,
    val value1: Value,
){
    var value2: Value? = null;
    var operator: ArOperator? = null;
}

class Value (
    val type: ValueType,
){
    var string: String = " ";
    var number: Double = 0.0;
    var id: String = " ";
}

enum class InstrType { EXPRESSION, INCREMENT, DECREMENT,
    ASSIGN_COMPARISON, ASSIGN_VALUE, ASSIGN_EXPR }

enum class ValueType { STRING, NUMBER, ID, TRUE, FALSE }

enum class ComparisonType { VALUE, STRINGS, EXPRESSIONS}

enum class AsOperator {
      ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, MULT_ASSIGN, DIV_ASSIGN }

enum class ArOperator { PLUS, MINUS, DIV, MULT }

enum class Comparator { EQUALS, DIFFERENT, MORE_THAN,
    MORE_EQUAL_THAN, LESS_THAN, LESS_EQUAL_THAN }
