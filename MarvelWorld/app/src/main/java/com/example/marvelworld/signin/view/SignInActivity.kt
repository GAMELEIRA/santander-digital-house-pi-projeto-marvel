package com.example.marvelworld.signin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.marvelworld.R
import com.example.marvelworld.home.views.MainActivity
import com.example.marvelworld.signup.view.SignUpActivity
import com.google.android.material.button.MaterialButton

class SignInActivity : AppCompatActivity() {

    private lateinit var buttonSignIn: MaterialButton
    private lateinit var buttonSignUp: MaterialButton
    private lateinit var buttonFacebook: ImageButton
    private lateinit var buttonGoogle: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        buttonSignIn = findViewById(R.id.btnSignInSignin)
        buttonSignUp = findViewById(R.id.btnSignUpSignIp)
        buttonFacebook = findViewById(R.id.imgBtnFacebook)
        buttonGoogle = findViewById(R.id.imgBtnGoogle)

        buttonSignIn.setOnClickListener{
            val intent = Intent(this,
                MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonSignUp.setOnClickListener{
            val intent = Intent(this,
                SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonFacebook.setOnClickListener{
            val intent = Intent(this,
                MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonGoogle.setOnClickListener{
            val intent = Intent(this,
                MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}