package com.example.android_oving8_alexander_carlsen.view.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.android_oving8_alexander_carlsen.game.Cell

class BoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet){

    private var sqrtSize = 3
    private var size = 9

    private var cellSizeP = 0F

    private var selectedRow = 1
    private var selectedCol = 1

    private var listener: OnTouchListener? = null
    private var cells: List<Cell>? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 5F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#f5a662")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efefef")
    }

    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#acacac")
    }

    private val textPaint = Paint().apply{
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 50F
    }

    private val noteTextPaint = Paint().apply{
        style = Paint.Style.FILL_AND_STROKE
        color = Color.DKGRAY
        textSize = 45F
    }

    private val startingCellTextPaint = Paint().apply{
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 55F
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizeP = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizeP, sizeP)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizeP = (width/ size).toFloat()
        fillCells(canvas)
        drawLine(canvas)
        drawText(canvas)
    }

    private fun fillCells(canvas: Canvas){

        cells?.forEach{
            val r = it.row
            val c = it.col

            if (it.isStartingCell) {
                fillCell(canvas, r, c, startingCellPaint)
            }
            else if (r == selectedRow && c == selectedCol){
                fillCell(canvas, r, c, selectedCellPaint)
            }
            else if(r == selectedRow || c == selectedCol){
                fillCell(canvas, r, c, conflictingCellPaint)
            }
            else if( r/sqrtSize == selectedRow/sqrtSize && c/sqrtSize == selectedCol/sqrtSize){
                fillCell(canvas, r, c, conflictingCellPaint)
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint){
        canvas.drawRect(c* cellSizeP, r* cellSizeP, (c+1)*cellSizeP, (r+1)*cellSizeP, paint )
    }

    private fun drawLine(canvas: Canvas){
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for(i in 1 until size){
            val paintToUse = when(i % sqrtSize){
                0 -> thickLinePaint
                else -> thinLinePaint
            }

            canvas.drawLine(i * cellSizeP, 0F, i*cellSizeP, height.toFloat(), paintToUse)
            canvas.drawLine(0F, i*cellSizeP, width.toFloat(), i*cellSizeP, paintToUse)
        }
    }

    private fun drawText(canvas:Canvas){
        cells?.forEach{
            val row = it.row
            val col = it.col
            var valueString = ""

            if(it.value != -2) {
                valueString = it.value.toString()
            }

            val paintToUse = if(it.isStartingCell) startingCellTextPaint else if(it.isNote) noteTextPaint else textPaint
            val textBounds = Rect()
            textPaint.getTextBounds(valueString, 0, valueString.length, textBounds)

            val textWidth = textPaint.measureText(valueString)
            val textHeight = textBounds.height()

            canvas.drawText(valueString, (col*cellSizeP + cellSizeP/2 - textWidth/2), (row*cellSizeP + cellSizeP/2 - textHeight/2), paintToUse)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float){
        val possibleSelectedRow = (y/cellSizeP).toInt()
        val possibleSelectedCol = (x/cellSizeP).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    fun updateSelectedCellUI(row: Int, col: Int){
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun updateCells(cells: List<Cell>){
        this.cells = cells
        invalidate()
    }

    fun registerListener(listener: OnTouchListener){
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}