package com.example.marvelworld.splashscreen.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.marvelworld.R
import com.example.marvelworld.signinsignup.view.SignInSignUpActivity

class SplashScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,SignInSignUpActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}