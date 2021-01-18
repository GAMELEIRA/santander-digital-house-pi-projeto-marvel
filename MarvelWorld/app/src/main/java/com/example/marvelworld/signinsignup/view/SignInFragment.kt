package com.example.marvelworld.signinsignup.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.marvelworld.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInFragment : Fragment() {

    private lateinit var signInButton: Button
    private lateinit var signUpButton: RelativeLayout
    private lateinit var facebookButton: ImageButton
    private lateinit var googleButton: ImageButton
    private lateinit var singInSignUpController: SingInSignUpController

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInButton = view.findViewById(R.id.sign_in_button)
        signUpButton = view.findViewById(R.id.sign_up_button)
        facebookButton = view.findViewById(R.id.facebook_button)
        googleButton = view.findViewById(R.id.google_button)
        singInSignUpController = requireActivity() as SingInSignUpController

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        signInButton.setOnClickListener {
            singInSignUpController.startMainActivity()
        }

        signUpButton.setOnClickListener {
            singInSignUpController.showSignUpFragment()
        }

        facebookButton.setOnClickListener {
            singInSignUpController.startMainActivity()
        }

        googleButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN_WITH_GOOGLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN_WITH_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("MainActivity", "Google sign in failed", e)
                    // ...
                }
            } else {
                Log.w("MainActivity", exception.toString())

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser != null) singInSignUpController.startMainActivity()
                } else {
                    Toast.makeText(context, getString(R.string.sing_in_failed), Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    companion object {
        const val RC_SIGN_IN_WITH_GOOGLE = 1
    }
}