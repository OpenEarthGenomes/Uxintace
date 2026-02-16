package com.example.uxintace
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    val board = GameBoard(60, 60)
    private var thread: GameThread? = null
    init { holder.addCallback(this) }
    override fun surfaceCreated(h: SurfaceHolder) { thread = GameThread(h).apply { running = true; start() } }
    override fun surfaceDestroyed(h: SurfaceHolder) { thread?.running = false; try { thread?.join() } catch(e: Exception) {} }
    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hi: Int) {}

    inner class GameThread(private val holder: SurfaceHolder) : Thread() {
        var running = false
        private var lastTime = System.currentTimeMillis()
        private val p = Paint()
        private val gridPaint = Paint().apply { color = Color.parseColor("#333333"); strokeWidth = 1f }

        override fun run() {
            while (running) {
                val canvas = holder.lockCanvas() ?: continue
                val now = System.currentTimeMillis()
                board.update((now - lastTime) / 1000f)
                lastTime = now

                canvas.drawColor(Color.BLACK)
                val cw = width / 60f
                val ch = height / 60f

                // RÁCS
                for (i in 0..60) {
                    canvas.drawLine(i * cw, 0f, i * cw, height.toFloat(), gridPaint)
                    canvas.drawLine(0f, i * ch, width.toFloat(), i * ch, gridPaint)
                }

                // KOCKÁK
                for (r in 0 until 60) {
                    for (c in 0 until 60) {
                        val color = board.grid[r][c].color
                        if (color != 0) {
                            p.color = when(color) {
                                1 -> Color.RED; 2 -> Color.BLUE; 3 -> Color.GREEN; 4 -> Color.YELLOW; else -> Color.GRAY
                            }
                            p.style = Paint.Style.FILL
                            canvas.drawRect(c*cw+1, r*ch+1, (c+1)*cw-1, (r+1)*ch-1, p)
                        }
                    }
                }
                
                // FEHÉR KURZOR KERET
                p.color = Color.WHITE
                p.style = Paint.Style.STROKE
                p.strokeWidth = 4f
                canvas.drawRect(board.cursorCol*cw, board.cursorRow*ch, (board.cursorCol+1)*cw, (board.cursorRow+1)*ch, p)

                // GAME OVER FELIRAT
                if (board.isGameOver) {
                    val textP = Paint().apply { color = Color.RED; textSize = 120f; textAlign = Paint.Align.CENTER; isFakeBoldText = true }
                    canvas.drawText("GAME OVER", width / 2f, height / 2f, textP)
                }

                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
