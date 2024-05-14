package com.compi1.ui
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class LineNumberTextWatcher(private val editText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        // Get the text from the EditText
        val text = editText.text.toString()
        // Split the text into lines
        val lines = text.split("\n")
        // Create a StringBuilder to build the enumerated text
        val enumeratedText = StringBuilder()
        // Enumerate each line and append it to the StringBuilder
        for (i in lines.indices) {
            enumeratedText.append("${i + 1}. ${lines[i]}\n")
        }
        // Set the enumerated text back to the EditText
        editText.removeTextChangedListener(this) // to avoid infinite loop
        editText.setText(enumeratedText.toString().trimEnd())
        editText.addTextChangedListener(this)
    }
}
