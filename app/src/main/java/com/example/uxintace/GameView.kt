package com.example.uxintace
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    var thread: GameThread? = null
    val board = GameBoard(60, 60)
    
    // Színek beállítása
    private val blockPaints = mapOf(
        1 to Paint().apply { color = Color.RED },
        2 to Paint().apply { color = Color.BLUE },
        3 to Paint().apply { color = Color.GREEN },
        4 to Paint().apply { color = Color.YELLOW }
    )
    private val gridPaint = Paint().apply { color = Color.DKGRAY; strokeWidth = 1f; style = Paint.Style.STROKE }
    private val cursorPaint = Paint().apply { color = Color.WHITE; style = Paint.Style.STROKE; strokeWidth = 4f }
    private val grabbedPaint = Paint().apply { color = Color.CYAN; style = Paint.Style.STROKE; strokeWidth = 6f }

    init { holder.addCallback(this) }
    fun stopThread() { thread?.running = false; try { thread?.join() } catch (e: Exception) {}; thread = null }
    override fun surfaceCreated(h: SurfaceHolder) { thread = GameThread(h).apply { running = true; start() } }
    override fun surfaceDestroyed(h: SurfaceHolder) { stopThread() }
    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hi: Int) {}

    inner class GameThread(private val holder: SurfaceHolder) : Thread() {
        var running = false
        override fun run() {
            while (running) {
                val canvas = holder.lockCanvas() ?: continue
                if (!board.isPaused) board.update(0.016f)
                
                canvas.drawColor(Color.parseColor("#121212")) // Mélyfekete háttér
                
                val cw = width / board.cols.toFloat()
                val ch = height / board.rows.toFloat()

                // 1. RÁCS RAJZOLÁSA (Hogy lásd a pályát)
                for (i in 0..board.cols) canvas.drawLine(i * cw, 0f, i * cw, height.toFloat(), gridPaint)
                for (i in 0..board.rows) canvas.drawLine(0f, i * ch, width.toFloat(), i * ch, gridPaint)

                // 2. KOCKÁK RAJZOLÁSA
                for (r in 0 until board.rows) {
                    for (c in 0 until board.cols) {
                        val cell = board.grid[r][c]
                        if (!cell.isEmpty) {
                            canvas.drawRect(c * cw + 1, r * ch + 1, (c + 1) * cw - 1, (r + 1) * ch - 1, blockPaints[cell.color]!!)
                        }
                    }
                }

                // 3. FOGOTT KOCKA KIJELÖLÉSE
                if (board.isGrabbing) {
                    canvas.drawRect(board.grabbedCol * cw, board.grabbedRow * ch, (board.grabbedCol + 1) * cw, (board.grabbedRow + 1) * ch, grabbedPaint)
                }

                // 4. KURZOR (Mindig legfelül)
                canvas.drawRect(board.cursorCol * cw, board.cursorRow * ch, (board.cursorCol + 1) * cw, (board.cursorRow + 1) * ch, cursorPaint)

                if (board.isGameOver) {
                    val p = Paint().apply { color = Color.WHITE; textSize = 60f; textAlign = Paint.Align.CENTER; isFakeBoldText = true }
                    canvas.drawText("GAME OVER - RESTART (🔄)", width / 2f, height / 2f, p)
                }
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
