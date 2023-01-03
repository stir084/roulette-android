package com.example.roulette_together

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myWebView: WebView = findViewById(R.id.webView)
        myWebView.webViewClient = MyWebViewClient() //하단 앱 공유하기 기능 연결
        myWebView.settings.javaScriptEnabled = true // 자바 스크립트 허용
        /* 웹뷰에서 새 창이 뜨지 않도록 방지하는 구문 */
        myWebView.webViewClient = WebViewClient()
        myWebView.webChromeClient = WebChromeClient()
        myWebView.loadUrl("http://116.37.136.233:5000")


    }

    override fun onBackPressed() {

        val myWebView: WebView = findViewById(R.id.webView)
        if(myWebView.canGoBack()){
            myWebView.goBack()
        }else{
            super.onBackPressed()
        }
    }

    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return if (url.startsWith(INTENT_PROTOCOL_START)) {
                val customUrlStartIndex = INTENT_PROTOCOL_START.length
                val customUrlEndIndex = url.indexOf(INTENT_PROTOCOL_INTENT)
                if (customUrlEndIndex < 0) {
                    false
                } else {
                    val customUrl = url.substring(customUrlStartIndex, customUrlEndIndex)
                    try {
                        view.getContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(customUrl)))
                    } catch (e: ActivityNotFoundException) {
                        val packageStartIndex = customUrlEndIndex + INTENT_PROTOCOL_INTENT.length
                        val packageEndIndex = url.indexOf(INTENT_PROTOCOL_END)
                        val packageName = url.substring(
                            packageStartIndex,
                            if (packageEndIndex < 0) url.length else packageEndIndex
                        )
                        view.getContext().startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    GOOGLE_PLAY_STORE_PREFIX + packageName
                                )
                            )
                        )
                    }
                    true
                }
            } else {
                false
            }
        }

        companion object {
            const val INTENT_PROTOCOL_START = "intent:"
            const val INTENT_PROTOCOL_INTENT = "#Intent;"
            const val INTENT_PROTOCOL_END = ";end;"
            const val GOOGLE_PLAY_STORE_PREFIX = "market://details?id="
        }
    }
}