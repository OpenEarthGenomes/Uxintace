package com.example.uxintace
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    private var thread: GameThread? = null
    val board = GameBoard(60, 60)
    
    private val blockPaints = mapOf(
        1 to Paint().apply { color = Color.parseColor("#FF5252") },
        2 to Paint().apply { color = Color.parseColor("#448AFF") },
        3 to Paint().apply { color = Color.parseColor("#66BB6A") },
        4 to Paint().apply { color = Color.parseColor("#FFD740") }
    )
    private val gridPaint = Paint().apply { color = Color.parseColor("#1AFFFFFF"); strokeWidth = 1f }
    private val gameOverPaint = Paint().apply { color = Color.WHITE; textSize = 100f; textAlign = Paint.Align.CENTER; isFakeBoldText = true }

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

                for (r in 0 until board.rows) {
                    for (c in 0 until board.cols) {
                        val cell = board.grid[r][c]
                        if (!cell.isEmpty) {
                            canvas.drawRect(c*cw, r*ch, (c+1)*cw, (r+1)*ch, blockPaints[cell.color]!!)
                        }
                    }
                }

                val cursorPaint = Paint().apply {
                    color = if (board.isGrabbing) Color.WHITE else Color.parseColor("#808080")
                    style = Paint.Style.STROKE; strokeWidth = 5f
                }
                canvas.drawRect(board.cursorCol*cw, board.cursorRow*ch, (board.cursorCol+1)*cw, (board.cursorRow+1)*ch, cursorPaint)

                if (board.isGameOver) canvas.drawText("GAME OVER", width/2f, height/2f, gameOverPaint)
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
