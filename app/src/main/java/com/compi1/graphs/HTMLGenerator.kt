package com.compi1.graphs

import kotlin.text.StringBuilder

class HTMLGenerator {

    fun write(dashboard: Dashboard): String {
        val finalGraphs = StringBuilder()
        var i = 0;
        for (graph in dashboard.graphs){
            i++;
            val g = when (graph.type){
                GraphType.BARS -> writeBars(graph, i)
                GraphType.CAKE -> writeCake(graph, i)
                GraphType.DOTS -> writeDots(graph, i)
                GraphType.LINES -> writeLines(graph, i)
                GraphType.CARD -> writeCard(graph, i)
            }
            finalGraphs.append(g)
        }
        return buildString {
            append("<!DOCTYPE html>")
            append("<html>")
            append("<head>")
            append("<meta charset=\"UTF-8\">")
            append("<title>Ejemplo con kotlinx.html</title>")
            append("\n<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js\"></script>")
            append("\n<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.1.2/chart.umd.js\"></script>")
            append("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">")
            append("<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>")
            append("</head>")
            append("<body>")
            append(finalGraphs.toString())
            append("<h2>hey</h2>")
            append("</body>")
            append("</html>")
        }
    }

    private fun writeBars(graph: Graph, i:Int): String {
        val xValues = StringBuilder()
        val yValues = StringBuilder()
        val colors = StringBuilder()
        for (elm in graph.data){
            xValues.append("\""+elm.x+"\", ")
            yValues.append(elm.y.toString()+", ")
            colors.append("\""+elm.color+"\", ")
        }
        val name = "bars_$i"
        val options = buildString {
            append("title:{display:true, text:\"${graph.info.title}\"},")
            append("scales:{ \n y:{title:{display:true, text:\"${graph.info.yAxisLbl}\"}}, x:{title:{display:true, text:\"${graph.info.xAxisLbl}\"}} }")
        }
        return buildString {
            append("\n<div><canvas id=\"$name\" width=\"700\" height=\"300\"></canvas></div>")
            append("\n<script>")
            append("\n const xValues = [$xValues];")
            append("\n const yValues = [$yValues];")
            append("\n const colors = [$colors];")
            append("\n const data = { \n labels: xValues, \n datasets:[{ data: yValues, backgroundColor: colors }] };")
            append("\n const config = { type:'bar', data:data, options:{${options}} };")
            append("\nlet ctx = document.getElementById('$name').getContext('2d');")
            append("\nlet $name = new Chart(ctx, config);")
            append("\n</script>")
        }
    }

    private fun writeCake(graph: Graph, i:Int): String {
        val xValues = StringBuilder()
        val yValues = StringBuilder()
        val colors = StringBuilder()
        for (elm in graph.data){
            xValues.append("\""+elm.x+"\", ")
            yValues.append(elm.y.toString()+", ")
            colors.append("\""+elm.color+"\", ")
        }
        val name = "cake_$i"
        val options = "\nplugins: {legend: {position:\"${graph.info.legendPos}\"}, title:{display:true, text:\"${graph.info.title}\"},}"
        return buildString {
            append("\n<div><canvas id=\"$name\" width=\"700\" height=\"500\"></canvas></div>")
            append("\n<script>")
            append("\n const labels = [$xValues];")
            append("\n const values = [$yValues];")
            append("\n const colors = [$colors];")
            append("\n const data = { \n labels: labels, \n datasets:[{ data: values, backgroundColor: colors }] };")
            append("\n const config = { type:'doughnut', data:data, options:{${options}} };")
            append("\nlet ctx = document.getElementById('$name').getContext('2d');")
            append("\nlet $name = new Chart(ctx, config);")
            append("\n</script>")
        }
    }

    private fun writeDots(graph: Graph, i:Int): String {
        val data = StringBuilder()
        val points = StringBuilder()
        val colors = StringBuilder()
        for (elm in graph.data){
            data.append("{x:"+elm.x+", y:"+elm.y+"},")
            points.append(elm.size+", ")
            colors.append("\""+elm.color+"\", ")
        }
        val name = "dots_$i"
        val options = buildString {
            append("\nplugins:{ title:{display:true, text:\"${graph.info.title}\"},},")
            append("scales:{ \n y:{title:{display:true, text:\"${graph.info.yAxisLbl}\"}}, x:{title:{display:true, text:\"${graph.info.xAxisLbl}\"}} }")
        }
        return buildString {
            append("\n<div><canvas id=\"$name\" width=\"700\" height=\"300\"></canvas></div>")
            append("\n<script>")
            append("\n const values = [$data];")
            append("\n const point_sizes = [$points];")
            append("\n const colors = [$colors];")
            append("\n const data = { \n datasets:[{ data:values, pointRadius:point_sizes, pointBackgroundColor:colors }] };")
            append("\n const config = { type:'scatter', data:data, options:{${options}} };")
            append("\nlet ctx = document.getElementById('$name').getContext('2d');")
            append("\nlet $name = new Chart(ctx, config);")
            append("\n</script>")
        }
    }
    private fun writeLines(graph: Graph, i:Int): String {
        val xValues = StringBuilder()
        val datasets = StringBuilder()
        for (elm in graph.lines[0].points){
            xValues.append("\""+elm.x+"\", ")
        }
        for (line in graph.lines){
            val y = StringBuilder()
            for (el in line.points){  y.append(el.y.toString()+", ")  }
            datasets.append("\n{")
            datasets.append("label: '${line.name}', data:[$y], borderColor: \"${line.color}\"")
            if (line.lineStyle == "dashed") datasets.append(", borderDash: [5, 5],")
            datasets.append("}, ")
        }
        val name = "lines_$i"
        val options = buildString {
            append("title:{display:true, text:\"${graph.info.title}\"},")
            append("scales:{ \n y:{title:{display:true, text:\"${graph.info.yAxisLbl}\"}}, x:{title:{display:true, text:\"${graph.info.xAxisLbl}\"}} }")
        }
        return buildString {
            append("\n<div><canvas id=\"$name\" width=\"700\" height=\"300\"></canvas></div>")
            append("\n<script>")
            append("\n const xValues = [$xValues];")
            append("\n const config = { type:'line', \ndata:{labels: xValues, datasets: [$datasets]}, options:{${options}} };")
            append("\nlet ctx = document.getElementById('$name').getContext('2d');")
            append("\nlet $name = new Chart(ctx, config);")
            append("\n</script>")
        }
    }
    private fun writeCard(graph: Graph, i:Int): String {
        val name = "card_$i"
        return buildString {
            append("\n<div class=\"card text-center\">")
            append("\n<div class=\"card-header\">${graph.element.x}</div>")
            append("\n<div class=\"card-body\">")
            append("\n<h5 class=\"card-title\">${graph.element.y}</h5>")
            append("\n <p class=\"card-text\">${graph.element.label}</p>")
        }
    }
}