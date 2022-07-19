package com.example.android_oving8_alexander_carlsen.game

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.android_oving8_alexander_carlsen.view.GameActivity
import java.io.File

class SudokuGame() {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    var winLiveData = MutableLiveData<Boolean>()

    private var selectedRow = -1
    private var selectedCol = -1

    private var board: Board

    init {
        val cells = List(9*9) {i -> Cell(i/9, i%9, -2)}
        board = Board(9, cells, 0)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
        winLiveData.postValue(false)
    }

    fun handleInput(number: Int, isNote: Boolean){
        if(selectedRow == -1 || selectedCol == -1) return
        if(board.getCell(selectedRow,selectedCol).isStartingCell) return

        board.getCell(selectedRow, selectedCol).value = number
        board.getCell(selectedRow, selectedCol).isNote = isNote
        cellsLiveData.postValue(board.cells)

        if(!hasMismatches(board.cells)){
            Log.i("status", "win!")
            winLiveData.postValue(true)
        }
    }

    fun updateSelectedCel(row: Int, col: Int){
        if(!board.getCell(row,col).isStartingCell) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(row, col))
        }
    }

    fun newBoard(board: Board){
        cellsLiveData.postValue(board.cells)
        this.board = board
    }

    fun hasMismatches(cells: List<Cell>) = cells.any { cell ->
        cells
            .filter { it.row == cell.row
                || it.col == cell.col
                || (it.row / 3 == cell.row / 3 && it.col / 3 == cell.col / 3 ) }
            .filter { !(it.row == cell.row && it.col == cell.col) }
            .any { it.value == cell.value || it.value == 0 }
    }
}