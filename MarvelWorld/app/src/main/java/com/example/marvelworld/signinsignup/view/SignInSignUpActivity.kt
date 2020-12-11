package com.example.marvelworld.signinsignup.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvelworld.R
import com.example.marvelworld.home.views.MainActivity

class SignInSignUpActivity : AppCompatActivity(), FragmentController {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_sign_up)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SignInFragment())
            .commit()
    }

    override fun showSignUpFragment() {
        val fragment = SignUpFragment()

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.no_slide,
                R.anim.slide_down,
                R.anim.slide_up,
                R.anim.fade_out
            )
            .addToBackStack(fragment.tag)
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showSignInFragment() {
        super.onBackPressed()
    }
}