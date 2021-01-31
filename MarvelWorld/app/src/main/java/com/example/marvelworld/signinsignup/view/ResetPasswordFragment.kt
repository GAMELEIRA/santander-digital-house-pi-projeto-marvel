package com.example.marvelworld.signinsignup.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.marvelworld.R
import com.example.marvelworld.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class ResetPasswordFragment : Fragment() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tieEmail: TextInputEditText
    private lateinit var email: String
    private lateinit var resetPasswordButton: Button

    private lateinit var singInSignUpController: SingInSignUpController
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tilEmail = view.findViewById(R.id.email_input_layout)
        tieEmail = view.findViewById(R.id.email_input)
        resetPasswordButton = view.findViewById((R.id.reset_password_button))

        singInSignUpController = requireActivity() as SingInSignUpController

        tieEmail.setText(requireArguments().getString(Constant.EMAIL))

        resetPasswordButton.setOnClickListener {
            email = tieEmail.text.toString().trim()

            if (validateFields()) {
                auth.sendPasswordResetEmail(email)
                Toast.makeText(
                    context,
                    getString(R.string.email_sent),
                    Toast.LENGTH_SHORT
                ).show()

                singInSignUpController.showSignInFragment()
            }
        }
    }

    private fun validateFields() = validateField(tilEmail, email)

    private fun validateField(textInputLayout: TextInputLayout, text: String): Boolean {
        var isValid = true
        textInputLayout.error = null

        if (text.isEmpty()) {
            textInputLayout.error = getString(R.string.required_field)
            isValid = false
        }

        return isValid
    }
}