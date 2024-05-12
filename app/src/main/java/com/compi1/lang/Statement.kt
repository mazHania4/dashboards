package com.compi1.lang

import com.compi1.graphs.Graph

class Statement (
    val type: StatementType
){
    val statements: ArrayList<Statement> = ArrayList();
    var instruction: Instruction? = null ; // in case of being of type INSTRUCTION or FOR
    var instruction2: Instruction? = null ; // in case of being of type FOR
    var graph: Graph? = null ; // in case of being of type GRAPH
    var comparison: Comparison? = null;
    val elseStatements: ArrayList<Statement> = ArrayList();
}

enum class StatementType { INSTRUCTION, IF, ELSE, FOR, WHILE, DO_WHILE, GRAPH }
