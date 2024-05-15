package com.compi1.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.compi1.MainActivity
import com.compi1.R
import com.compi1.graphs.Dashboard
import com.compi1.graphs.HTMLGenerator
import com.compi1.graphs.Graph
import com.compi1.parsers.Lexer
import com.compi1.parsers.Parser
import java.io.StringReader
import java.util.LinkedList

class Panel(private val context: MainActivity, var file: File, private val panelLayout: LinearLayout)  {

    val view: View = LayoutInflater.from(context).inflate(R.layout.panel, null)
    private val btnSave: Button = view.findViewById(R.id.btnSave)
    private val console: TextView = view.findViewById(R.id.console)
    private var dashboard: Dashboard? = null
    private val generator = HTMLGenerator()

    init {
        view.findViewById<Button>(R.id.btnAnalyze).setOnClickListener { analyze() }
        view.findViewById<Button>(R.id.btnGenerate).setOnClickListener { generate() }
        btnSave.setOnClickListener { saveFileContent() }
    }

    private fun generate(){
        dashboard?.let{
            val html = generator.write(it) // genera el html como string
            context.setContent(html) //manda el html a la actividad que lo va a escribir
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/html"
                putExtra(Intent.EXTRA_TITLE, "dashboard.html")
            }
            context.startActivityForResult(intent, MainActivity.PICK_FOLDER_WRITE_CODE) //levanta el intent para que el usuario escoja donde se va a guardar
        }
    }

    private fun analyze(){
        try {
            val parser = Parser(Lexer(StringReader(file.content)))
            parser.parse()
            if (parser.errors.isEmpty()){
                console.setText("Todo bien :)")
                dashboard = parser.dashboard
            }else{
                showErrors(parser.errors)
            }
        } catch (e: Exception){
            console.setText(e.message)
            console.append(e.stackTraceToString())
        }
    }

    private fun showErrors(errors: LinkedList<com.compi1.lang.Error>){
        console.setText("")
        for (error in errors){
            console.append("\n(!) " + error.line.toString()+ ": ")
            console.append(error.message)
            //pintar errores en el editText
        }
        console.append("\n\n\n\n")
    }

    private fun saveFileContent() {
        context.setContent(file.content)
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "archivo.gh")
        }
        context.startActivityForResult(intent, MainActivity.PICK_FILE_WRITE_CODE)
    }



}