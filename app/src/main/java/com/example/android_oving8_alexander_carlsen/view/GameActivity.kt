package com.example.android_oving8_alexander_carlsen.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android_oving8_alexander_carlsen.R
import com.example.android_oving8_alexander_carlsen.game.Board
import com.example.android_oving8_alexander_carlsen.game.Cell
import com.example.android_oving8_alexander_carlsen.view.custom.BoardView
import com.example.android_oving8_alexander_carlsen.viewmodel.PlayViewModel
import java.io.File

class GameActivity : AppCompatActivity(), BoardView.OnTouchListener  {
    private lateinit var viewModel: PlayViewModel
    private val boardView: BoardView by lazy { findViewById<BoardView>(R.id.boardView) }
    var noteMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Premade boards to be loaded into file
        val message = "3-0,0,0,1,0,0,0,0,0-0,4,0,0,0,0,0,0,0-0,2,0,0,0,0,0,0,0-0,0,0,0,8,0,0,0,0-0,0,0,0,9,0,0,0,0-0,0,0,3,0,0,0,0,0-0,0,2,0,0,0,0,0,0-0,7,0,0,0,0,0,0,0-0,0,8,0,0,0,0,0,0\n" +
                "1-7,9,0,5,6,8,1,4,3-4,0,3,2,1,0,8,6,7-0,6,1,3,7,4,9,5,0-6,2,5,8,9,3,0,1,4-3,7,9,1,4,0,6,8,5-1,4,8,7,5,6,2,0,9-0,8,4,9,3,1,5,7,6-9,3,7,0,8,5,4,2,1-5,1,6,4,2,7,3,0,8\n" +
                "1-0,9,2,5,6,8,1,0,3-4,5,0,2,1,0,8,6,7-8,6,1,3,0,4,9,5,2-6,2,5,0,9,3,7,1,4-0,7,9,1,4,2,6,8,5-1,4,8,0,5,6,2,3,9-2,8,4,9,3,1,5,7,0-9,3,7,6,8,5,0,2,1-5,1,6,4,2,0,3,9,8\n" +
                "2-7,0,0,0,0,0,1,4,0-0,5,3,0,1,0,0,6,0-0,6,1,0,7,0,0,0,0-6,2,0,0,0,0,7,0,4-0,0,0,1,4,0,0,0,5-0,0,0,7,0,6,0,3,0-2,0,4,9,0,1,0,7,0-0,3,0,6,0,0,0,2,1-0,1,0,4,0,7,0,9,0\n" +
                "3-7,0,0,0,6,0,0,4,0-4,0,0,2,0,0,0,6,0-0,0,0,0,0,0,0,0,0-0,0,5,0,0,3,0,0,4-0,0,0,0,4,0,0,0,0-0,4,0,7,0,0,2,0,0-0,8,0,0,0,0,0,0,6-0,0,7,0,0,0,0,0,1-0,0,0,0,0,0,0,0,0\n"


        this.openFileOutput("boards.text", Context.MODE_PRIVATE).use {
            it.write(message.toByteArray())
        }

        val file = File(this.filesDir,"boards.text")
        val boardRawInput = file.readLines() // Read file

        var boardList:MutableList<Board> = mutableListOf()
        for(board in boardRawInput){
            val splitData = board.split("-")
            val cells: MutableList<Cell> = mutableListOf()
            for(i in 1..9){
                val splitRow = splitData[i].split(",")
                for(u in 0..8){
                    if(splitRow[u].toInt() == 0) cells.add(Cell(i-1, u, -2)) else cells.add(Cell(i-1, u, splitRow[u].toInt(), true))
                }
            }
            boardList.add(Board(9, cells.toList(), splitData[0].toInt()))
        }



        boardView.registerListener(this)

        viewModel = ViewModelProviders.of(this).get(PlayViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer{ updateSelectedCellUI(it)})
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })
        viewModel.sudokuGame.winLiveData.observe(this, Observer{winMessage(it) })

        val switchNote = findViewById<Switch>(R.id.switchNote)

        val clearText = findViewById<Button>(R.id.buttonClear)
        clearText.setOnClickListener{viewModel.sudokuGame.handleInput(-2, false)}

        findViewById<Switch>(R.id.switchNote).setOnCheckedChangeListener { _, isChecked ->
            noteMode = isChecked
        }

        val buttonList = listOf(
            findViewById<Button>(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9)
        )

        buttonList.forEachIndexed { index, button ->
            button.setOnClickListener{
                viewModel.sudokuGame.handleInput(index + 1, noteMode)
            }
        }


        val buttonDifficulty1 = findViewById<Button>(R.id.buttonDifficulty1)
        val buttonDifficulty2 = findViewById<Button>(R.id.buttonDifficulty2)
        val buttonDifficulty3 = findViewById<Button>(R.id.buttonDifficulty3)


        buttonDifficulty1.setOnClickListener{
            this.newBoardClick(boardList.toList().filter { it.difficulty == 1 }.shuffled().take(1)[0])
        }
        buttonDifficulty2.setOnClickListener{
            this.newBoardClick(boardList.toList().filter { it.difficulty == 2 }.shuffled().take(1)[0])
        }
        buttonDifficulty3.setOnClickListener{
            this.newBoardClick(boardList.toList().filter { it.difficulty == 3 }.shuffled().take(1)[0])
        }


    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        boardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let{
        boardView.updateSelectedCellUI(cell.first, cell.second)
    }

    private fun winMessage(winBool: Boolean){
        if(winBool) {
            Toast.makeText(this, "You won!", Toast.LENGTH_LONG)
        }
    }

    override fun onCellTouched(row:Int, col:Int){
        viewModel.sudokuGame.updateSelectedCel(row, col)
    }

    fun backButtonOnClick(view: View) {
        finish()
    }

    fun newBoardClick(board: Board){
        viewModel.sudokuGame.newBoard(board)
        boardView.updateCells(board.cells)
    }
}