package com.example.uxintace
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    val board = GameBoard(60, 60)
    private var thread: GameThread? = null
    
    private val paints = mapOf(
        1 to Paint().apply { color = Color.RED },
        2 to Paint().apply { color = Color.BLUE },
        3 to Paint().apply { color = Color.GREEN },
        4 to Paint().apply { color = Color.YELLOW }
    )
    private val gridPaint = Paint().apply { color = Color.argb(80, 100, 100, 100); strokeWidth = 1f }
    private val cursorPaint = Paint().apply { color = Color.WHITE; style = Paint.Style.STROKE; strokeWidth = 4f }

    init { holder.addCallback(this) }
    override fun surfaceCreated(h: SurfaceHolder) { thread = GameThread(h).apply { running = true; start() } }
    override fun surfaceDestroyed(h: SurfaceHolder) { thread?.running = false; try { thread?.join() } catch(e:Exception){}; thread = null }
    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hi: Int) {}

    inner class GameThread(private val holder: SurfaceHolder) : Thread() {
        var running = false
        override fun run() {
            while (running) {
                val canvas = holder.lockCanvas() ?: continue
                board.update(0.016f)
                canvas.drawColor(Color.BLACK)
                val cw = width / board.cols.toFloat()
                val ch = height / board.rows.toFloat()

                for (r in 0 until board.rows) {
                    for (c in board.cols - 1 downTo 0) {
                        if (gridPaint.strokeWidth > 0) {
                            canvas.drawLine(c * cw, 0f, c * cw, height.toFloat(), gridPaint)
                            canvas.drawLine(0f, r * ch, width.toFloat(), r * ch, gridPaint)
                        }
                        val cell = board.grid[r][c]
                        if (cell.color != 0) {
                            canvas.drawRect(c*cw+1, r*ch+1, (c+1)*cw-1, (r+1)*ch-1, paints[cell.color]!!)
                        }
                    }
                }
                canvas.drawRect(board.cursorCol*cw, board.cursorRow*ch, (board.cursorCol+1)*cw, (board.cursorRow+1)*ch, cursorPaint)
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
