package com.compi1.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.compi1.MainActivity
import com.compi1.R

class Panel(private val context: MainActivity, var file: File, private val panelLayout: LinearLayout)  {

    val view: View = LayoutInflater.from(context).inflate(R.layout.panel, null)
    private val btnSave: Button = view.findViewById(R.id.btnSave)

    init {
        view.findViewById<Button>(R.id.btnAnalyze).setOnClickListener {
            TODO()
        }
        view.findViewById<Button>(R.id.btnGenerate).setOnClickListener {
            TODO()
        }
        btnSave.setOnClickListener { saveFileContent() }
    }

    private fun saveFileContent() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "archivo.gh")
        }
        context.setContent(file.content)
        context.startActivityForResult(intent, MainActivity.PICK_FILE_WRITE_CODE)
    }



}