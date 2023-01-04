package com.example.roulette_together

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myWebView: WebView = findViewById(R.id.webView)

        myWebView.settings.run {
            // 웹뷰 자바스크립트 허용
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
        }

        myWebView.webViewClient = object: WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView,request: WebResourceRequest): Boolean {
                Log.d(TAG, request.url.toString())

                if (request.url.scheme == "intent") {
                    try {
                        // Intent 생성
                        val intent = Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME)

                        // 실행 가능한 앱이 있으면 앱 실행
                      //  if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                            Log.d(TAG, "ACTIVITY: ${intent.`package`}")
                            Log.d(TAG, "카카오톡 실행")
                            return true
                      //  }
                        // 실행 못하는 경우 카카오톡 마켓으로 이동해야 하지만 resolveActivity가 제대로 작동하지 않아서
                        // 기능 빼버림.
                        Log.d(TAG, "카카오톡 공유하기 실행 못함")

                        // 실행 못하면 웹뷰는 카카오톡 공유하기 화면으로 이동
                        // myWebView.loadUrl("http://kakao-share.s3-website.ap-northeast-2.amazonaws.com/")

                        // 구글 플레이 카카오톡 마켓으로 이동
                      /*  val intentStore = Intent(Intent.ACTION_VIEW)
                        intentStore.addCategory(Intent.CATEGORY_DEFAULT)
                        intentStore.data = Uri.parse("market://details?id=com.kakao.talk")
                        Log.d(TAG, "구글 플레이 카카오톡 마켓으로 이동")
                        startActivity(intentStore)*/

                    } catch (e: URISyntaxException) {
                        Log.e(TAG, "!!! 에러 Invalid intent request", e)
                    }
                }
                // 나머지 서비스 로직 구현
                return false
            }
        } //하단 앱 공유하기 기능 연결
        ///myWebView.settings.javaScriptEnabled = true // 자바 스크립트 허용
        /* 웹뷰에서 새 창이 뜨지 않도록 방지하는 구문 */
       // myWebView.webViewClient = WebViewClient()
       // myWebView.webChromeClient = WebChromeClient()
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
}