package com.example.android_oving8_alexander_carlsen.game

class Cell(
    val row:Int,
    val col:Int,
    var value:Int,
    var isStartingCell: Boolean = false,
    var isNote: Boolean = false
){
}