package com.example.marvelworld.myprofile.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marvelworld.R
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.home.views.OnUpdateProfile
import com.example.marvelworld.myprofile.repository.MyProfileRepository
import com.example.marvelworld.myprofile.viewmodel.MyProfileViewModel
import com.example.marvelworld.authentication.view.AuthenticationActivity
import com.example.marvelworld.util.Constant
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.squareup.picasso.Picasso

class MyProfileFragment : Fragment() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private var photoUri: Uri? = null

    private lateinit var user: FirebaseUser
    private lateinit var userPhoto: ImageView
    private lateinit var uploadPhotoButton: ImageButton
    private lateinit var tieName: TextInputEditText
    private lateinit var tilName: TextInputLayout
    private lateinit var saveButton: Button
    private lateinit var resetPasswordButton: Button
    private lateinit var deleteAccount: Button
    private lateinit var myProfileViewModel: MyProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = auth.currentUser!!

        userPhoto = view.findViewById(R.id.user_photo)
        uploadPhotoButton = view.findViewById(R.id.upload_photo_button)
        tieName = view.findViewById(R.id.name_input)
        tilName = view.findViewById(R.id.name_input_layout)
        saveButton = view.findViewById(R.id.save_button)
        resetPasswordButton = view.findViewById(R.id.reset_password_button)
        deleteAccount = view.findViewById(R.id.delete_account_button)

        myProfileViewModel = ViewModelProvider(
            this,
            MyProfileViewModel.MyProfileViewModelFactory(
                MyProfileRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(MyProfileViewModel::class.java)

        Picasso.get().load(user.photoUrl).into(userPhoto)
        tieName.setText(user.displayName)

        uploadPhotoButton.setOnClickListener {
            searchFile()
        }

        saveButton.setOnClickListener {
            val name = tieName.text.toString().trim()
            if (name.isNotBlank()) {
                var profileUpdates = UserProfileChangeRequest.Builder()
                profileUpdates = profileUpdates.setDisplayName(name)
                if (photoUri != null) {
                    profileUpdates = profileUpdates.setPhotoUri(photoUri)
                }

                user.updateProfile(profileUpdates.build())
                    .addOnSuccessListener {
                        Picasso.get().load(user.photoUrl).into(userPhoto)
                        Toast.makeText(
                            context,
                            getString(R.string.profile_updated),
                            Toast.LENGTH_SHORT
                        ).show()
                        val onUpdateProfile = requireActivity() as OnUpdateProfile
                        onUpdateProfile.updateHeader()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            getString(R.string.profile_update_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                tilName.error = getString(R.string.required_field)
            }
        }

        val sharedPref = requireActivity().getSharedPreferences(
            Constant.SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
        with(sharedPref) {
            when (getInt(Constant.SING_IN_MODE, Constant.SING_IN_MODE_DEFAULT)) {
                Constant.SING_IN_WITH_PASSWORD -> {
                    resetPasswordButton.visibility = View.VISIBLE
                    resetPasswordButton.setOnClickListener {
                        auth.sendPasswordResetEmail(user.email!!)
                        Toast.makeText(
                            context,
                            getString(R.string.email_sent),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        deleteAccount.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.delete_account))
                .setMessage(getString(R.string.delete_account_warning))
                .setNegativeButton(getString(R.string.decline)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(getString(R.string.accept)) { dialog, _ ->
                    val userId = user.uid
                    dialog.dismiss()
                    user.delete()
                        .addOnSuccessListener {
                            myProfileViewModel.removeFavorites(userId)
                                .observe(viewLifecycleOwner, {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.account_deleted),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(context, AuthenticationActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                })
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                getString(R.string.delete_account_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
                .show()
        }
    }

    private fun searchFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Constant.UPLOAD_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.UPLOAD_IMAGE_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            photoUri = data?.data
            Picasso.get().load(photoUri).into(userPhoto)
        }
    }
}