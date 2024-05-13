package com.compi1.graphs

class Graph(
    val type: GraphType
) {
    val data: ArrayList<Element> = ArrayList() // for bars, cake and dots
    val lines: ArrayList<Line> = ArrayList() // for lines
    var element: Element = Element("", 0.0) // for card
    var info: Info = Info("Tittle", "X axis", "Y axis", "center")
}

class Info(
    val title: String,
    val xAxisLbl: String,
    val yAxisLbl: String,
    val legendPos: String
)

class Element (
    val x: String,
    val y: Double
) {
    var color: String = "#1b1c1b";
    var label: String = " ";
    var size: String = "5";
    var icon: String = " ";
    var link: String = " ";
}

class Line (
    val color: String,
    val lineStyle: String
) {
    val points: ArrayList<Element> = ArrayList()
}

enum class GraphType { BARS, CAKE, DOTS, LINES, CARD }