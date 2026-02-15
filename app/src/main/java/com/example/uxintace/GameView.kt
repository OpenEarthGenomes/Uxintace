package com.example.uxintace

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    var thread: GameThread? = null
    val board = GameBoard(60, 60)
    
    private val blockPaints = mapOf(
        1 to Paint().apply { color = Color.parseColor("#FF5252") },
        2 to Paint().apply { color = Color.parseColor("#448AFF") },
        3 to Paint().apply { color = Color.parseColor("#66BB6A") },
        4 to Paint().apply { color = Color.parseColor("#FFD740") }
    )

    init { holder.addCallback(this) }

    fun stopThread() {
        thread?.running = false
        try { thread?.join() } catch (e: Exception) {}
        thread = null
    }

    override fun surfaceCreated(h: SurfaceHolder) {
        thread = GameThread(h).apply { running = true; start() }
    }

    override fun surfaceDestroyed(h: SurfaceHolder) { stopThread() }
    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hi: Int) {}

    inner class GameThread(private val holder: SurfaceHolder) : Thread() {
        var running = false
        override fun run() {
            while (running) {
                if (board.isPaused) { sleep(100); continue }
                val canvas = holder.lockCanvas() ?: continue
                board.update(0.016f)
                canvas.drawColor(Color.BLACK)
                
                val cw = width / board.cols.toFloat()
                val ch = height / board.rows.toFloat()

                // Rács és blokkok rajzolása (optimalizált)
                for (r in 0 until board.rows) {
                    for (c in 0 until board.cols) {
                        val cell = board.grid[r][c]
                        if (!cell.isEmpty) {
                            canvas.drawRect(c*cw, r*ch, (c+1)*cw, (r+1)*ch, blockPaints[cell.color]!!)
                        }
                    }
                }

                val cursorPaint = Paint().apply { color = Color.WHITE; style = Paint.Style.STROKE; strokeWidth = 3f }
                canvas.drawRect(board.cursorCol*cw, board.cursorRow*ch, (board.cursorCol+1)*cw, (board.cursorRow+1)*ch, cursorPaint)

                if (board.isGameOver) {
                    val p = Paint().apply { color = Color.RED; textSize = 70f; textAlign = Paint.Align.CENTER }
                    canvas.drawText("GAME OVER - RESTART (🔄)", width/2f, height/2f, p)
                }
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
