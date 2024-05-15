package com.compi1.ui
import android.graphics.Color
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
        s?.let {
            val text = it.toString()
            val wordsToColor = listOf("azul", "rojo", "verde") // Palabras reservadas
            val quotationColor = Color.BLUE // Color de las comillas dobles
            colorText2(editText, text, wordsToColor, quotationColor)
        }
        /*s?.let {
            val text = it.toString()
            val wordsToColor =
                listOf("if", "else", "do", "while", "for")
            val logialOperators =
                listOf("==", "!=", "<", ">", "<=", ">=")
            val agrupationOperators =
                listOf("(", ")", "{", "}", "[", "]")
            val allWords =
                text.split(" ", "\n", "\t", ";", ",", ":")
            for (word in allWords) {
                if (!word.matches(Regex("\"([^\"]*)\""))) {
                    colorText( editText, word, Color.BLACK, Color.parseColor("#FF5153"))
                } else {
                    colorText(editText, word, Color.BLACK, Color.parseColor("#CEC6C6"))
                }
            }
            for (word in wordsToColor) {
                colorText(editText, word, Color.YELLOW, Color.WHITE)
            }
            for (word in logialOperators) {
                colorText(editText, word, Color.BLUE, Color.WHITE)
            }
            for (word in agrupationOperators) {
                colorText(editText, word, Color.MAGENTA, Color.WHITE)
            }
        }*/
    }
    private fun colorText2(
        editText: EditText,
        text: String,
        wordsToColor: List<String>,
        quotationColor: Int
    ) {
        editText.text.clearSpans()
        val tokens = text.split("\\s+".toRegex())
        var startIndex = 0
        for (token in tokens) {
            val endIndex = startIndex + token.length
            if (wordsToColor.contains(token.toLowerCase())) {
                editText.text.setSpan(
                    android.text.style.ForegroundColorSpan(Color.BLUE),
                    startIndex,
                    endIndex,
                    android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                if (!(token.startsWith("\"") && token.endsWith("\""))) {
                    editText.text.setSpan(
                        android.text.style.ForegroundColorSpan(Color.RED),
                        startIndex,
                        endIndex,
                        android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            startIndex = endIndex + 1
        }
    }
    private fun colorText(editText: EditText, word: String, color: Int, backC: Int) {
        val text = editText.text.toString()
        val start = text.indexOf(word)
        if (start != -1) {
            val end = start + word.length
            editText.text.setSpan(
                android.text.style.ForegroundColorSpan(color),
                start,
                end,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            editText.text.setSpan(
                android.text.style.BackgroundColorSpan(backC),
                start,
                end,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }


}
