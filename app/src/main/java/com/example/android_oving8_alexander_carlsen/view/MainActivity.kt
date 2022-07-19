package com.example.android_oving8_alexander_carlsen.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.android_oving8_alexander_carlsen.R
import java.io.File

class MainActivity : Activity() {

    lateinit var dir:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         dir = this.filesDir.toString()
    }

    fun changeLang(view: View) {
        startActivityForResult(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS), 0)
    }

    fun helpButtonOnClick(view: View) {
        startActivity(Intent("com.example.android_oving8_alexander_carlsen.view.HelpActivity"))
    }

    fun newGameButtonOnClick(view: View) {
        intent.putExtra("dir", dir)
        startActivity(Intent("com.example.android_oving8_alexander_carlsen.view.GameActivity"))
    }

    fun addGameButtonOnClick(view: View){
        val newBoardString = findViewById<EditText>(R.id.editTextTextMultiLine).text

        val file = File( this.filesDir,"boards.text")
        val boardListRaw = file.readLines()

        val newBoardList: MutableList<String> = mutableListOf()
        for (i in boardListRaw){
            newBoardList.add(i)
        }
        newBoardList.add(newBoardString.toString())

        this.openFileOutput("boards.text", Context.MODE_PRIVATE).use {
            it.write(newBoardList.joinToString { "\n" }.toByteArray())
        }
    }
}