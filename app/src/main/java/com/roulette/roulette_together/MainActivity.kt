package com.roulette.roulette_together

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {
    private val myApp: Context = this

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

        myWebView.webChromeClient = object : WebChromeClient(){
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }
        }
        myWebView.webViewClient = object: WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView,request: WebResourceRequest): Boolean {
                Log.d(TAG, request.url.toString())

                if (request.url.scheme == "intent") {
                    try {
                        // Intent 생성
                        val intent = Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME)

                        // 실행 가능한 앱이 있으면 앱 실행 - 실행 가능한 앲이 없으면 Exception 발생
                        packageManager.getPackageInfo("com.kakao.talk", PackageManager.GET_ACTIVITIES)
                        startActivity(intent)
                        Log.d(TAG, "카카오톡 실행")
                        return true

                        Log.d(TAG, "카카오톡 공유하기 실행 못함")



                    } catch (e: URISyntaxException) {
                        Log.e(TAG, "Invalid intent request", e)
                    } catch (e: Exception) {
                        Log.d(TAG, "카카오톡 공유하기 실행 못함")

                        // 실행 못하면 웹뷰는 카카오톡 공유하기 화면으로 이동
                        myWebView.loadUrl("http://www.roulettetogether.site/roulette")

                        // 구글 플레이 카카오톡 마켓으로 이동
                        val intentStore = Intent(Intent.ACTION_VIEW)
                        intentStore.addCategory(Intent.CATEGORY_DEFAULT)
                        intentStore.data = Uri.parse("market://details?id=com.kakao.talk")
                        Log.d(TAG, "구글 플레이 카카오톡 마켓으로 이동")
                        startActivity(intentStore)
                    }
                }
                return false
            }
        }

        myWebView.loadUrl("http://www.roulettetogether.site/roulette")
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