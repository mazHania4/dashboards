package com.compi1.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.compi1.R

class PopUp: AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pop_up)

        val webView: WebView = findViewById(R.id.webview)
        val closeButton: Button = findViewById(R.id.close_button)

        val fileUri = intent.getParcelableExtra<Uri>("file_uri")
        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.loadUrl(fileUri.toString())
        closeButton.setOnClickListener {
            finish()
        }
    }
}