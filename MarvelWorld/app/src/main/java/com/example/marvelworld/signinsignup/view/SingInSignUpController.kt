package com.example.marvelworld.signinsignup.view

interface SingInSignUpController {
    fun showSignUpFragment()
    fun showResetPasswordFragment(email: String)
    fun showVerificationEmailFragment(email: String)
    fun startMainActivity()
    fun showSignInFragment()
}