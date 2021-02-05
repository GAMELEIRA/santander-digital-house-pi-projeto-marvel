package com.example.marvelworld.authentication.view

interface AuthenticationController {
    fun showSignUpFragment()
    fun showResetPasswordFragment(email: String)
    fun showVerificationEmailFragment(email: String)
    fun startMainActivity()
    fun showSignInFragment()
}