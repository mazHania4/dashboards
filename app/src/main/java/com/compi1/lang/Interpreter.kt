package com.compi1.lang

import com.compi1.graphs.Graph
import java.lang.RuntimeException

class Interpreter {
    private val symTable: HashMap<String, Symbol> = HashMap()

    fun execute(s: Statement, graphs: ArrayList<Graph>){
        when (s.type) {
            StatementType.GRAPH ->  s.graph?.let { graphs.add(it) }
            StatementType.INSTRUCTION -> executeInstruction(s.instruction!!)
            StatementType.IF -> executeIfElse(s, graphs)
            StatementType.ELSE -> executeIfElse(s, graphs)
            StatementType.FOR -> {
                executeInstruction(s.instruction!!) //first instruction of for, only executed once
                executeFor(s, graphs)
            }
            StatementType.WHILE -> executeWhile(s, graphs)
            StatementType.DO_WHILE -> executeDoWhile(s, graphs)
        }
    }

    private fun executeInstruction(it: Instruction){
        when (it.type) {
            InstrType.ASSIGN_VALUE -> {
                val symType: SymType = if (it.value?.type?.equals(ValueType.STRING)!!) SymType.STRING else SymType.BOOLEAN
                val bool: Boolean = it.value?.type?.equals(ValueType.TRUE)!!
                val string: String = it.value?.string!!
                val sym = Symbol(symType, string, 0.0, bool)
                symTable[it.id] = sym
            }
            InstrType.ASSIGN_EXPR -> {
                try {
                    val expr: Double = getExprResult(it.expression!!)
                    var number = 0.0
                    when (it.operator!!){
                        AsOperator.ASSIGN -> number = expr
                        AsOperator.PLUS_ASSIGN -> number += expr
                        AsOperator.MINUS_ASSIGN -> number -= expr
                        AsOperator.MULT_ASSIGN -> number *= expr
                        AsOperator.DIV_ASSIGN -> number /= expr
                    }
                    val sym = Symbol(SymType.NUMBER, " ", number, false)
                    symTable[it.id] = sym
                } catch (e: RuntimeException){
                    throw RuntimeException(e.message + " . On line"+it.line)
                }
            }
            InstrType.ASSIGN_COMPARISON -> {
                try {
                    val bool: Boolean = getComparisonResult(it.comparison!!)
                    val sym = Symbol(SymType.BOOLEAN, " ", 0.0, bool)
                    symTable[it.id] = sym
                } catch (e: RuntimeException){
                    throw RuntimeException(e.message + " . On line"+it.line)
                }
            }
            InstrType.INCREMENT -> {
                if (!symTable.containsKey(it.id))
                    throw RuntimeException("Can't find reference '"+it.id+"', on line"+it.line)
                if (!symTable[it.id]?.type?.equals(SymType.NUMBER)!!)
                    throw RuntimeException("Can't increment value in '"+it.id+"' with type '"+symTable[it.id]?.type+"' , on line"+it.line)
                symTable[it.id]?.number = symTable[it.id]?.number?.plus(1)!!
            }
            InstrType.DECREMENT -> {
                if (!symTable.containsKey(it.id)) throw RuntimeException("Can't find reference '"+it.id+"', on line"+it.line)
                if (!symTable[it.id]?.type?.equals(SymType.NUMBER)!!)
                    throw RuntimeException("Can't decrement value in '"+it.id+"' with type '"+symTable[it.id]?.type+"' , on line"+it.line)
                symTable[it.id]?.number = symTable[it.id]?.number?.minus(1)!!
            }
            InstrType.EXPRESSION -> { }
        }
    }

    private fun executeIfElse(s: Statement, graphs: ArrayList<Graph>){
        if (getComparisonResult(s.comparison!!)){
            for (subS in s.statements){
                execute(subS, graphs)
            }
        } else if (s.type == StatementType.ELSE){
            for (subS in s.elseStatements){
                execute(subS, graphs)
            }
        }
    }

    private fun executeFor(s: Statement, graphs: ArrayList<Graph>){
        if (getComparisonResult(s.comparison!!)){
            for (subS in s.statements){
                execute(subS, graphs)
            }
            executeInstruction(s.instruction2!!)
            executeFor(s, graphs)
        }
    }

    private fun executeWhile(s: Statement, graphs: ArrayList<Graph>){
        while (getComparisonResult(s.comparison!!)){
            for (subS in s.statements){
                execute(subS, graphs)
            }
        }
    }

    private fun executeDoWhile(s: Statement, graphs: ArrayList<Graph>){
        do {
            for (subS in s.statements){
                execute(subS, graphs)
            }
        } while (getComparisonResult(s.comparison!!))
    }


    fun getExprResult(n: Expression): Double {
        if (n.value1.type != ValueType.NUMBER && n.value1.type != ValueType.ID)
            throw RuntimeException("Can't use type '"+n.value1.type+"' for numeric values or arithmetic operations")
        if (n.value1.type == ValueType.ID && !symTable.containsKey(n.value1.id))
            throw RuntimeException("Can't find reference '"+n.value1.id+"'")
        if (n.value1.type == ValueType.ID && !symTable[n.value1.id]?.type?.equals(SymType.NUMBER)!!)
            throw RuntimeException("Can't use type '"+symTable[n.value1.id]?.type+"' for numeric values or arithmetic operations")
        val val1 = if (n.value1.type == ValueType.NUMBER) n.value1.number else symTable[n.value1.id]?.number!!
        if (n.isJustOne){
            return val1
        } else {
            if (n.value2?.type!! != ValueType.NUMBER && n.value2?.type!! != ValueType.ID)
                throw RuntimeException("Can't use type '"+n.value2?.type!!+"' for numeric values or arithmetic operations")
            if (n.value2?.type!! == ValueType.ID && !symTable.containsKey(n.value2?.id!!))
                throw RuntimeException("Can't find reference '"+n.value2?.id!!+"'")
            if (n.value2?.type!! == ValueType.ID && !symTable[n.value2?.id!!]?.type?.equals(SymType.NUMBER)!!)
                throw RuntimeException("Can't use type '"+symTable[n.value2?.id!!]?.type+"' for numeric values or arithmetic operations")
            val val2 = if (n.value1.type == ValueType.NUMBER) n.value2?.number!! else symTable[n.value2?.id!!]?.number!!
            val result = when(n.operator!!){
                ArOperator.DIV -> val1 / val2
                ArOperator.PLUS -> val1 + val2
                ArOperator.MINUS -> val1 - val2
                ArOperator.MULT -> val1 * val2
            }
            return result
        }
    }

    private fun getComparisonResult(c: Comparison): Boolean {
        var ans = false
        when(c.type){
            ComparisonType.EXPRESSIONS -> {
                val exp1 = getExprResult(c.expr1!!)
                val exp2 = getExprResult(c.expr2!!)
                ans = when(c.comparator!!){
                    Comparator.EQUALS -> exp1 == exp2
                    Comparator.DIFFERENT -> exp1 != exp2
                    Comparator.MORE_THAN -> exp1 > exp2
                    Comparator.MORE_EQUAL_THAN -> exp1 >= exp2
                    Comparator.LESS_THAN -> exp1 < exp2
                    Comparator.LESS_EQUAL_THAN -> exp1 <= exp2
                }
            }
            ComparisonType.VALUE -> {
                ans = when(c.value?.type!!){
                    ValueType.TRUE -> true
                    ValueType.FALSE -> false
                    ValueType.ID -> {
                        if ( !symTable.containsKey(c.value?.id!!) )
                            throw RuntimeException("Can't find reference '"+c.value?.id+"'")
                        if ( !symTable[c.value?.id!!]?.type?.equals(SymType.BOOLEAN)!!)
                            throw RuntimeException("Can't use type '"+symTable[c.value?.id!!]?.type+"' as boolean value")
                        symTable[c.value?.id!!]?.bool!!
                    }
                    else -> false
                }
            }
            ComparisonType.STRINGS -> {
                ans = when(c.comparator!!){
                    Comparator.EQUALS -> c.string1 == c.string2
                    Comparator.DIFFERENT -> c.string1 != c.string2
                    else -> false
                }
            }
        }
        return ans
    }
}

class Symbol(
    var type: SymType,
    var string: String,
    var number: Double,
    var bool : Boolean
)
enum class SymType { STRING, NUMBER, BOOLEAN }
