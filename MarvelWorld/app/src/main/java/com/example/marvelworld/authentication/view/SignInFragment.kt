package com.example.marvelworld.authentication.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.marvelworld.R
import com.example.marvelworld.util.Constant
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignInFragment : Fragment() {

    private lateinit var email: String
    private lateinit var password: String

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tieEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout

    private lateinit var signInButton: Button
    private lateinit var signUpButton: RelativeLayout
    private lateinit var forgotPasswordButton: TextView
    private lateinit var facebookButton: ImageButton
    private lateinit var googleButton: ImageButton
    private lateinit var authenticationController: AuthenticationController

    private val callbackManager by lazy { CallbackManager.Factory.create() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tilEmail = view.findViewById(R.id.email_input_layout)
        tieEmail = view.findViewById(R.id.email_input)
        tilPassword = view.findViewById(R.id.password_input_layout)

        signInButton = view.findViewById(R.id.sign_in_button)
        signUpButton = view.findViewById(R.id.sign_up_button)
        forgotPasswordButton = view.findViewById(R.id.forgot_password_button)
        facebookButton = view.findViewById(R.id.facebook_button)
        googleButton = view.findViewById(R.id.google_button)

        authenticationController = requireActivity() as AuthenticationController

        googleButton.setOnClickListener {
            signInWithGoogle()
        }

        facebookButton.setOnClickListener {
            loginWithFacebook()
        }

        signInButton.setOnClickListener {
            email = tieEmail.text.toString().trim()
            password =
                view.findViewById<TextInputEditText>(R.id.password_input).text.toString().trim()

            if (validateFields()) {
                signInWithEmailAndPassword(email, password)
            }
        }

        signUpButton.setOnClickListener {
            authenticationController.showSignUpFragment()
        }

        forgotPasswordButton.setOnClickListener {
            email = tieEmail.text.toString().trim()
            authenticationController.showResetPasswordFragment(email)
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constant.RC_SIGN_IN_WITH_GOOGLE)
    }

    private fun loginWithFacebook() {
        val loginManager = LoginManager.getInstance()

        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val credential: AuthCredential =
                    FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                firebaseAuth(credential, Constant.SING_IN_WITH_FACEBOOK)
            }

            override fun onCancel() {
                Toast.makeText(context, getString(R.string.sign_in_canceled), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        loginManager.logInWithReadPermissions(
            this,
            listOf(Constant.EMAIL, Constant.PROFILE_PICTURE)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.RC_SIGN_IN_WITH_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                    firebaseAuth(credential, Constant.SING_IN_WITH_GOOGLE)
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()

            }
        } else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuth(credential: AuthCredential, signInMode: Int) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser != null) {
                        saveSignInMode(signInMode)
                        authenticationController.startMainActivity()
                    }
                } else {
                    Toast.makeText(context, getString(R.string.sing_in_failed), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user!!.isEmailVerified) {
                        saveSignInMode(Constant.SING_IN_WITH_PASSWORD)
                        authenticationController.startMainActivity()
                    } else {
                        authenticationController.showVerificationEmailFragment(email)
                    }
                } else {
                    Toast.makeText(context, R.string.auth_failed, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        isValid = validateField(tilEmail, email) && isValid
        isValid = validateField(tilPassword, password) && isValid

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

    private fun saveSignInMode(signInMode: Int) {
        val sharedPref = requireActivity().getSharedPreferences(
            Constant.SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

        with(sharedPref.edit()) {
            putInt(Constant.SING_IN_MODE, signInMode).apply()
        }
    }
}