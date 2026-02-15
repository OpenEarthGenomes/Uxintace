package com.example.uxintace

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    private var thread: GameThread? = null
    val board = GameBoard(60, 60)
    
    private val gridPaint = Paint().apply { color = Color.parseColor("#33FFEB3B"); strokeWidth = 1f }
    private val cursorPaint = Paint().apply { color = Color.parseColor("#808080"); style = Paint.Style.STROKE; strokeWidth = 5f }
    private val blockPaints = mapOf(
        1 to Paint().apply { color = Color.RED },
        2 to Paint().apply { color = Color.BLUE },
        3 to Paint().apply { color = Color.GREEN },
        4 to Paint().apply { color = Color.YELLOW }
    )

    init { holder.addCallback(this) }
    override fun surfaceCreated(h: SurfaceHolder) { thread = GameThread(h).apply { running = true; start() } }
    override fun surfaceDestroyed(h: SurfaceHolder) { thread?.running = false; thread?.join() }
    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hi: Int) {}

    inner class GameThread(private val holder: SurfaceHolder) : Thread() {
        var running = false
        override fun run() {
            while (running) {
                val canvas = holder.lockCanvas() ?: continue
                board.update(0.016f)
                canvas.drawColor(Color.parseColor("#1E1E1E"))
                
                val cw = width / board.cols.toFloat()
                val ch = height / board.rows.toFloat()

                for (i in 0..board.cols) canvas.drawLine(i*cw, 0f, i*cw, height.toFloat(), gridPaint)
                for (i in 0..board.rows) canvas.drawLine(0f, i*ch, width.toFloat(), i*ch, gridPaint)

                for (r in 0 until board.rows) {
                    for (c in 0 until board.cols) {
                        val cell = board.grid[r][c]
                        if (!cell.isEmpty) {
                            canvas.drawRect(c*cw+1, r*ch+1, (c+1)*cw-1, (r+1)*ch-1, blockPaints[cell.color]!!)
                        }
                    }
                }
                canvas.drawRect(board.cursorCol*cw+2, board.cursorRow*ch+2, (board.cursorCol+1)*cw-2, (board.cursorRow+1)*ch-2, cursorPaint)
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
