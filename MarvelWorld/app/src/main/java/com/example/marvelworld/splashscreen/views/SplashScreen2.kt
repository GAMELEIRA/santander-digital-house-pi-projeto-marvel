package com.example.marvelworld.splashscreen.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.marvelworld.R
import com.example.marvelworld.home.views.MainActivity
import com.example.marvelworld.authentication.view.AuthenticationActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen2 : AppCompatActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var nextIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)

        Handler(Looper.getMainLooper()).postDelayed({
            initNextIntent()
        }, 2000)
    }

    private fun initNextIntent() {
        nextIntent = if (auth.currentUser == null) {
            Intent(this, AuthenticationActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(nextIntent)
        finish()
    }
}