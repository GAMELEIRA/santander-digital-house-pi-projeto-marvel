package com.example.marvelworld.home.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.marvelworld.R
import com.example.marvelworld.authentication.view.AuthenticationActivity
import com.example.marvelworld.util.Constant
import com.example.marvelworld.util.LoadingDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), OnUpdateProfile {
    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }
    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val navigationView by lazy { findViewById<NavigationView>(R.id.navigationView) }
    private val logOutButton by lazy { findViewById<LinearLayout>(R.id.log_out_button) }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var username: TextView
    private lateinit var userPhoto: ImageView
    private lateinit var uploadPhotoButton: ImageButton
    private lateinit var loadingDialog: LoadingDialog
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadingDialog = LoadingDialog(this, layoutInflater, getString(R.string.updating))

        navigationView.setupWithNavController(navController)
        navigationView.itemIconTintList = null

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val header = navigationView.getHeaderView(0)

        username = header.findViewById(R.id.username)
        userPhoto = header.findViewById(R.id.user_photo)
        uploadPhotoButton = header.findViewById(R.id.upload_photo_button)

        updateHeader()

        uploadPhotoButton.setOnClickListener {
            searchFile()
        }

        logOutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView)
                    return true
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun searchFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Constant.UPLOAD_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.UPLOAD_IMAGE_CODE && resultCode == RESULT_OK) {
            loadingDialog.startLoadingDialog()

            val imageUri = data?.data!!

            val reference = FirebaseStorage.getInstance().reference
                .child("profileImages")
                .child(auth.currentUser!!.uid + ".jpg")
            reference.putFile(imageUri)
                .addOnSuccessListener {
                    reference.downloadUrl
                        .addOnSuccessListener { uri ->
                            val profileUpdates =
                                UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
                            updateProfile(profileUpdates)
                        }
                        .addOnFailureListener {
                            loadingDialog.dismissDialog()
                            Toast.makeText(
                                this,
                                getString(R.string.photo_upload_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener {
                    loadingDialog.dismissDialog()
                    Toast.makeText(
                        this,
                        getString(R.string.photo_upload_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun updateProfile(profileUpdates: UserProfileChangeRequest) {
        auth.currentUser!!.updateProfile(profileUpdates)
            .addOnSuccessListener {
                Picasso.get().load(auth.currentUser!!.photoUrl).into(userPhoto)
                Toast.makeText(
                    this,
                    getString(R.string.profile_updated),
                    Toast.LENGTH_SHORT
                ).show()
                updateHeader()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.photo_upload_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnCompleteListener {
                loadingDialog.dismissDialog()
            }
    }

    override fun updateHeader() {
        Picasso.get().load(auth.currentUser!!.photoUrl).into(userPhoto)
        username.text = auth.currentUser!!.displayName
    }
}