package com.example.android_oving8_alexander_carlsen.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android_oving8_alexander_carlsen.game.SudokuGame
import java.io.*

//Stores the game-state
class PlayViewModel() : ViewModel() {

    val sudokuGame = SudokuGame()
}