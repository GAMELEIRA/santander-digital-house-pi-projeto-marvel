package com.example.marvelworld.signinsignup.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.example.marvelworld.R

class SignUpFragment : Fragment() {

    private lateinit var signUpButton: Button
    private lateinit var signInButton: RelativeLayout
    private lateinit var activity: FragmentController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpButton = view.findViewById(R.id.sign_up_button)
        signInButton = view.findViewById(R.id.sign_in)
        activity = requireActivity() as FragmentController

        signUpButton.setOnClickListener {
            activity.showSignInFragment()
        }

        signInButton.setOnClickListener {
            activity.showSignInFragment()
        }
    }
}