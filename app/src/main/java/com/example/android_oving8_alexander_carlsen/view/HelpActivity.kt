package com.example.android_oving8_alexander_carlsen.view

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.example.android_oving8_alexander_carlsen.R


class HelpActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_layout)
    }

    fun backButtonOnClick(view: View) {
        finish()
    }

}