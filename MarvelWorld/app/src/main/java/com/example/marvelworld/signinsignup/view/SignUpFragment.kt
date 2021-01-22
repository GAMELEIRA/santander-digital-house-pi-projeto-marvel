package com.example.marvelworld.signinsignup.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.marvelworld.R
import com.example.marvelworld.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignUpFragment : Fragment() {

    private lateinit var signUpButton: Button
    private lateinit var signInButton: RelativeLayout

    private lateinit var tilName: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilRepeatPassword: TextInputLayout

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var repeatPassword: String

    private lateinit var singInSignUpController: SingInSignUpController

    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tilName = view.findViewById(R.id.name_input_layout)
        tilEmail = view.findViewById(R.id.email_input_layout)
        tilPassword = view.findViewById(R.id.password_input_layout)
        tilRepeatPassword = view.findViewById(R.id.repeat_password_input_layout)

        signUpButton = view.findViewById(R.id.sign_up_button)
        signInButton = view.findViewById(R.id.sign_in)

        singInSignUpController = requireActivity() as SingInSignUpController

        signUpButton.setOnClickListener {
            name = view.findViewById<TextInputEditText>(R.id.name_input).text.toString().trim()
            email = view.findViewById<TextInputEditText>(R.id.email_input).text.toString().trim()
            password =
                view.findViewById<TextInputEditText>(R.id.password_input).text.toString().trim()
            repeatPassword =
                view.findViewById<TextInputEditText>(R.id.repeat_password_input).text.toString()
                    .trim()

            if (validateFields()) {
                createUserWithEmailAndPassword(email, password)
            }
        }

        signInButton.setOnClickListener {
            singInSignUpController.showSignInFragment()
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        isValid = validateField(tilName, name) && isValid
        isValid = validateField(tilEmail, email) && isValid

        val passwordIsFiled = validateField(tilPassword, password)
        val repeatPasswordIsFiled = validateField(tilRepeatPassword, repeatPassword)

        isValid = passwordIsFiled && isValid
        isValid = repeatPasswordIsFiled && isValid

        if (passwordIsFiled && repeatPasswordIsFiled) {
            isValid = validatePassword() && isValid
        }

        return isValid
    }

    private fun validateField(textInputLayout: TextInputLayout, text: String): Boolean {
        var isValid = true
        textInputLayout.error = null

        if (text.isEmpty()) {
            textInputLayout.error = getString(R.string.required_field)
            isValid = false
        }

        return isValid
    }

    private fun validatePassword(): Boolean {
        var isValid = true
        val pattern = Constant.PASSWORD_PATTERN.toRegex()

        if (password != repeatPassword) {
            tilRepeatPassword.error = getString(R.string.password_match)
            isValid = false
        }

        if (!pattern.matches(password)) {
            tilPassword.error = getString(R.string.password_pattern_error)
            isValid = false
        }

        return isValid
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    sendEmailVerification(user)
                } else {
                    Toast.makeText(context, getString(R.string.auth_failed), Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        getString(R.string.email_sent, user.email),
                        Toast.LENGTH_SHORT
                    ).show()
                    auth.signOut()
                    singInSignUpController.showSignInFragment()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.email_verification_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}