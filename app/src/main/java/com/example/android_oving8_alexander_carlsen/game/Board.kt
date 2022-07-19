package com.example.android_oving8_alexander_carlsen.game

class Board(val size:Int, val cells: List<Cell>, val difficulty:Int = 0) {
    fun getCell(row:Int, col:Int) = cells[row*size + col]
}