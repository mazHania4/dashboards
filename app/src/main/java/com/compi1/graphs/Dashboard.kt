package com.compi1.graphs

class Dashboard (
    val title: String,
    val description: String,
    val keywords: List<String>,
    val header: String,
    val footer: String,
    val bgColor: String,
    val fontFamily: String,
    val fontSize: String
) {
    val graphs: ArrayList<Graph> = ArrayList();
}