package com.example.marvelworld.authentication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.marvelworld.R
import com.example.marvelworld.util.Constant
import com.example.marvelworld.util.LoadingDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class ResetPasswordFragment : Fragment() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tieEmail: TextInputEditText
    private lateinit var email: String
    private lateinit var resetPasswordButton: Button
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var authenticationController: AuthenticationController
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog =
            LoadingDialog(requireContext(), layoutInflater, getString(R.string.sending_email))

        tilEmail = view.findViewById(R.id.email_input_layout)
        tieEmail = view.findViewById(R.id.email_input)
        resetPasswordButton = view.findViewById((R.id.reset_password_button))

        authenticationController = requireActivity() as AuthenticationController

        tieEmail.setText(requireArguments().getString(Constant.EMAIL))

        resetPasswordButton.setOnClickListener {
            email = tieEmail.text.toString().trim()

            if (validateFields()) {
                loadingDialog.startLoadingDialog()
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                getString(R.string.email_sent),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            auth.signOut()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.failed_send_email),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            auth.signOut()

                        }
                        loadingDialog.dismissDialog()
                        authenticationController.showSignInFragment()
                    }
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