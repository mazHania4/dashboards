package com.compi1.ui
import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.compi1.MainActivity
import com.compi1.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class FilePagerAdapter(private val context: MainActivity) : RecyclerView.Adapter<FilePagerAdapter.FileViewHolder>() {
    val files = mutableListOf<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_page, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount(): Int {
        return files.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNewFile() {
        val newFile = File("file_"+ files.size, "")
        files.add(newFile)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addExistingFile(uri: Uri) {
        val content = readFileContent(uri)
        content?.let {
            val newFile = File(getFileNameFromUri(uri), it)
            files.add(newFile)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("Range")
    fun getFileNameFromUri(uri: Uri): String {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                cursor.close()
                return fileName
            }
        }
        return "file"
    }

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val editText: EditText = itemView.findViewById(R.id.editText)
        private val lineNumberTextView: TextView = itemView.findViewById(R.id.lineNumberTextView)
        private val panelLayout: LinearLayout = itemView.findViewById(R.id.panel)
        private val panel: Panel
        init {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    updateChangedText()
                    matchText(editText, editText.text.toString())
                }
            })
            panel = Panel(context, File("File", ""), panelLayout)
            panelLayout.removeAllViews()
            panelLayout.addView(panel.view)
        }

        private fun matchText(editText: EditText, text: String){
            val keywords = listOf("if", "else", "do", "while", "for")
            val logicOperators = listOf("==", "!=", "<", ">", "<=", ">=")
            val grouping = listOf("(", ")", "{", "}", "[", "]")
            val graphs = listOf("\"title\"", "\"description\"", "\"keywords\"", "\"header\"", "\"footer\"", "\"lineStyle\"",
                "\"copyright\"", "\"backgroundColor\"", "\"fontFamily\"", "\"fontSize\"", "\"data\"", "\"category\"",
                "\"value\"", "\"label\"", "\"x\"", "\"y\"", "\"name\"", "\"points\"", "\"color\"", "\"size\"",
                "\"icon\"", "\"link\"", "\"chart\"", "\"xAxisLabel\"", "\"yAxisLabel\"", "\"legendPosition\""  )
            colorText( editText, text, keywords, Color.YELLOW)
            colorText( editText, text, logicOperators, Color.CYAN)
            colorText( editText, text, grouping, Color.MAGENTA)
            colorText( editText, text, graphs, Color.rgb(222, 147, 55))
        }

        private fun colorText(editText: EditText, text: String, wordsToColor: List<String>, color:Int) {
            val tokens = text.split("\\s+".toRegex())
            var startIndex = 0
            for (token in tokens) {
                val endIndex = startIndex + token.length
                if (wordsToColor.contains(token.toLowerCase())) {
                    editText.text.setSpan(
                        android.text.style.ForegroundColorSpan(color),
                        startIndex,
                        endIndex,
                        android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                startIndex = endIndex + 1
            }
        }

        fun bind(file: File) {
            editText.setText(file.content)
            panel.file = file
            updateChangedText()
        }

        private fun updateChangedText() {
            val text = editText.text.toString()
            panel.file.content = text
            val lineCount = text.split("\n").size
            val enumeratedText = StringBuilder()
            for (i in 1..lineCount) {
                enumeratedText.append("$i\n")
            }
            lineNumberTextView.text = enumeratedText.toString().trimEnd()
        }
    }

    // Function to read content from a file URI
    private fun readFileContent(uri: Uri): String? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
                stringBuilder.append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return stringBuilder.toString()
    }

}

class File(val name: String, var content: String)
