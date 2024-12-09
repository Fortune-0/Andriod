package com.example.efficilog

import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.content.Intent
import android.os.Bundle
//import android.os.Looper

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Simulate a delay before starting the main activity
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)

//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish() // Finish the splash screen activity
//        }, 3000))
    }
}