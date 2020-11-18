package com.example.marvelworld

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marvelworld.mainviewpager.views.MainViewPagerFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainViewPagerFragment())
            .commit()

    }
}