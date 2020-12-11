package com.example.marvelworld.signup.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marvelworld.R
import com.example.marvelworld.home.views.MainActivity
import com.example.marvelworld.signin.view.SignInActivity
import com.google.android.material.button.MaterialButton

class SignUpActivity : AppCompatActivity() {

    private lateinit var buttonSignUp: MaterialButton
    private lateinit var buttonSignIn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        buttonSignUp = findViewById(R.id.btnSignUpSignUp)
        buttonSignIn = findViewById(R.id.btnSignInSignup)

        buttonSignUp.setOnClickListener{
            val intent = Intent(this,
                SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonSignIn.setOnClickListener{
            val intent = Intent(this,
                SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}