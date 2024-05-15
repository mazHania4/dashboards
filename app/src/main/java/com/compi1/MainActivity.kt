package com.compi1

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.compi1.ui.FilePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : Activity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var filePagerAdapter: FilePagerAdapter
    private lateinit var btnOpenFile: com.google.android.material.floatingactionbutton.FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        btnOpenFile = findViewById(R.id.btnOpenFile)
        filePagerAdapter = FilePagerAdapter(this)
        viewPager.adapter = filePagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val fileName = filePagerAdapter.files[position].name
            tab.text = fileName
        }.attach()

        btnOpenFile.setOnClickListener {
            openFilePicker()
        }
    }

    private fun openFilePicker() {
        val options = arrayOf("Crear nuevo archivo", "Importar archivo existente")
        AlertDialog.Builder(this)
            .setTitle("Seleccionar opción")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> filePagerAdapter.addNewFile()
                    1 -> pickExistingFile()
                }
            }
            .show()
    }

    private var content: String = " "
    fun setContent(c:String){
        content = c
    }
    companion object {
        val PICK_FOLDER_WRITE_CODE = 100
        val PICK_FILE_WRITE_CODE = 101
        val PICK_FILE_READ_CODE = 102
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_READ_CODE && resultCode == RESULT_OK) {
            data?.data?.let { fileUri ->
                filePagerAdapter.addExistingFile(fileUri)
            }
        }
        if (requestCode == PICK_FILE_WRITE_CODE && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            if (uri.scheme == "content") {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(content.toByteArray())
                }
                Toast.makeText(this, "Archivo guardado correctamente", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == PICK_FOLDER_WRITE_CODE && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            if (uri.scheme == "content") {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(content.toByteArray())
                }
                Toast.makeText(this, "html guardado correctamente", Toast.LENGTH_SHORT).show()
                //exportPdf()
            }
        }
    }
     private fun exportPdf(){
         val webView = WebView(this)
         webView.settings.javaScriptEnabled = true
         webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
         webView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
         webView.layout(0, 0, webView.measuredWidth, webView.measuredHeight)
         val bitmap = Bitmap.createBitmap(webView.measuredWidth, webView.measuredHeight, Bitmap.Config.ARGB_8888)
         val canvas = Canvas(bitmap)
         webView.draw(canvas)
         val document = PdfDocument()
         val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
         val page = document.startPage(pageInfo)
         val pdfCanvas = page.canvas
         pdfCanvas.drawBitmap(bitmap, 0f, 0f, null)
         document.finishPage(page)
         val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "dashboard.pdf")
         try {
             document.writeTo(FileOutputStream(file))
         } catch (e: IOException) {
             e.printStackTrace()
         }
         document.close()
         Toast.makeText(this, "pdf guardado correctamente", Toast.LENGTH_SHORT).show()
     }

    private fun pickExistingFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        val mimeTypes = arrayOf("text/plain", "text/gh")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, PICK_FILE_READ_CODE)
    }

}