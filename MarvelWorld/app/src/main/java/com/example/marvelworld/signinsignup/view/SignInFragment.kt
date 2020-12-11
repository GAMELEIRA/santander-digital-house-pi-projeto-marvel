package com.example.marvelworld.signinsignup.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.example.marvelworld.R

class SignInFragment : Fragment() {

    private lateinit var signInButton: Button
    private lateinit var signUpButton: RelativeLayout
    private lateinit var facebookButton: ImageButton
    private lateinit var googleButton: ImageButton
    private lateinit var activity: FragmentController

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
        activity = requireActivity() as FragmentController

        signInButton.setOnClickListener {
            activity.startMainActivity()
        }

        signUpButton.setOnClickListener {
            activity.showSignUpFragment()
        }

        facebookButton.setOnClickListener {
            activity.startMainActivity()
        }

        googleButton.setOnClickListener {
            activity.startMainActivity()
        }
    }
}