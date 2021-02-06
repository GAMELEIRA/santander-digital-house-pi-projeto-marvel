package com.example.marvelworld.authentication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.marvelworld.R
import com.example.marvelworld.util.LoadingDialog
import com.google.firebase.auth.FirebaseAuth

class VerificationEmailFragment : Fragment() {

    private lateinit var resendEmailButton: Button
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var authenticationController: AuthenticationController
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verification_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog =
            LoadingDialog(requireContext(), layoutInflater, getString(R.string.sending_email))

        resendEmailButton = view.findViewById((R.id.resend_email_button))

        authenticationController = requireActivity() as AuthenticationController

        val user = auth.currentUser!!
        auth.signOut()

        resendEmailButton.setOnClickListener {
            loadingDialog.startLoadingDialog()

            user.sendEmailVerification()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            getString(R.string.verification_email_sent, user.email),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.failed_send_email),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    loadingDialog.dismissDialog()
                    authenticationController.showSignInFragment()
                }
        }
    }
}