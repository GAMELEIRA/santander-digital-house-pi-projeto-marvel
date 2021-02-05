package com.example.marvelworld.util

import com.example.marvelworld.BuildConfig

object Constant {
    const val EMAIL = "email"
    const val PROFILE_PICTURE = "public_profile"
    const val RC_SIGN_IN_WITH_GOOGLE = 1
    const val PASSWORD_PATTERN = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"""
    const val UPLOAD_IMAGE_CODE = 50
    const val SING_IN_WITH_PASSWORD = 1
    const val SING_IN_WITH_GOOGLE = 2
    const val SING_IN_WITH_FACEBOOK = 3
    const val SING_IN_MODE_DEFAULT = -1
    const val SING_IN_MODE = "signInMode"
    const val SHARED_PREFERENCES = BuildConfig.APPLICATION_ID

}