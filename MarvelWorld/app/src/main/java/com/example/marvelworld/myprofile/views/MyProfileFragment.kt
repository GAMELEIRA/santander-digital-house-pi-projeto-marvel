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
import com.example.marvelworld.authentication.view.AuthenticationActivity
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.home.views.OnUpdateProfile
import com.example.marvelworld.myprofile.repository.MyProfileRepository
import com.example.marvelworld.myprofile.viewmodel.MyProfileViewModel
import com.example.marvelworld.util.Constant
import com.example.marvelworld.util.LoadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MyProfileFragment : Fragment() {
    private val auth by lazy { FirebaseAuth.getInstance() }

    private lateinit var user: FirebaseUser
    private lateinit var userPhoto: ImageView
    private lateinit var uploadPhotoButton: ImageButton
    private lateinit var tieName: TextInputEditText
    private lateinit var tilName: TextInputLayout
    private lateinit var updateButton: Button
    private lateinit var resetPasswordButton: Button
    private lateinit var deleteAccount: Button
    private lateinit var myProfileViewModel: MyProfileViewModel
    private lateinit var loadingDialog: LoadingDialog
    private var photoUri: Uri? = null

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
        updateButton = view.findViewById(R.id.update_button)
        resetPasswordButton = view.findViewById(R.id.reset_password_button)
        deleteAccount = view.findViewById(R.id.delete_account_button)

        myProfileViewModel = ViewModelProvider(
            this,
            MyProfileViewModel.MyProfileViewModelFactory(
                MyProfileRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(MyProfileViewModel::class.java)

        user.photoUrl?.run{ Picasso.get().load(this).into(userPhoto) }
        tieName.setText(user.displayName)

        uploadPhotoButton.setOnClickListener {
            searchFile()
        }

        updateButton.setOnClickListener {
            handleProfileUpdate()
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
                        resetPassword()
                    }
                }
            }
        }

        deleteAccount.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun resetPassword() {
        startLoadingDialog(getString(R.string.sending_email))
        auth.sendPasswordResetEmail(user.email!!)
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
                    ).show()
                    auth.signOut()
                }
                loadingDialog.dismissDialog()
            }
    }

    private fun handleProfileUpdate() {
        startLoadingDialog(getString(R.string.updating))

        val name = tieName.text.toString().trim()
        if (validateName(name)) {
            val profileUpdates = UserProfileChangeRequest.Builder()
            profileUpdates.displayName = name

            if (photoUri != null) {
                uploadImage(profileUpdates)
            } else {
                updateProfile(profileUpdates)
            }
        }
    }

    private fun validateName(name: String): Boolean {
        return if (name.isBlank()) {
            tilName.error = getString(R.string.required_field)
            false
        } else {
            true
        }
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_account))
            .setMessage(getString(R.string.delete_account_warning))
            .setNegativeButton(getString(R.string.decline)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.accept)) { dialog, _ ->
                dialog.dismiss()
                deleteUserAccount()
            }.show()
    }

    private fun deleteUserAccount() {
        startLoadingDialog(getString(R.string.deleting_account))
        val userId = user.uid

        FirebaseStorage.getInstance().reference
            .child("profileImages")
            .child(userId + ".jpg")
            .delete()
            .addOnCompleteListener {
                user.delete()
                    .addOnSuccessListener {
                        deleteUserFavorites(userId)
                        Toast.makeText(
                            context,
                            getString(R.string.account_deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        showFailedToast(getString(R.string.delete_account_failed) + ": " + it.localizedMessage)
                    }
                    .addOnCompleteListener {
                        loadingDialog.dismissDialog()
                    }
            }
    }

    private fun deleteUserFavorites(userId: String) {
        myProfileViewModel.removeFavorites(userId)
            .observe(viewLifecycleOwner, {
                Toast.makeText(
                    context,
                    getString(R.string.account_deleted),
                    Toast.LENGTH_SHORT
                ).show()
                val intent =
                    Intent(context, AuthenticationActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                auth.signOut()
            })
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

    private fun startLoadingDialog(text: String) {
        loadingDialog = LoadingDialog(requireContext(), layoutInflater, text)
        loadingDialog.startLoadingDialog()
    }

    private fun uploadImage(
        profileUpdates: UserProfileChangeRequest.Builder
    ) {
        val reference = FirebaseStorage.getInstance().reference
            .child("profileImages")
            .child(user.uid + ".jpg")
        reference.putFile(photoUri!!)
            .addOnSuccessListener {
                getDownloadUrl(reference, profileUpdates)
            }
            .addOnFailureListener {
                showFailedToast(getString(R.string.profile_update_failed))
            }
    }

    private fun getDownloadUrl(
        reference: StorageReference,
        profileUpdates: UserProfileChangeRequest.Builder
    ) {
        reference.downloadUrl
            .addOnSuccessListener { uri ->
                profileUpdates.photoUri = uri
                updateProfile(profileUpdates)
            }
            .addOnFailureListener {
                showFailedToast(getString(R.string.profile_update_failed))
            }
    }

    private fun updateProfile(profileUpdates: UserProfileChangeRequest.Builder) {
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
                showFailedToast(getString(R.string.profile_update_failed))
            }
            .addOnCompleteListener {
                loadingDialog.dismissDialog()
            }
    }

    private fun showFailedToast(text: String) {
        loadingDialog.dismissDialog()
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}