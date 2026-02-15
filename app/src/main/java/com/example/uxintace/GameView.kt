package com.example.uxintace

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    private var thread: GameThread? = null
    val board = GameBoard()

    init { holder.addCallback(this) }

    override fun surfaceCreated(h: SurfaceHolder) {
        thread = GameThread(h)
        thread?.running = true
        thread?.start()
    }

    override fun surfaceDestroyed(h: SurfaceHolder) {
        thread?.running = false
        thread?.join()
    }

    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hi: Int) {}

    fun moveCursor(dx: Int, dy: Int) = board.moveCursor(dx, dy)
    fun toggleGrab() = board.toggleGrab()
    fun shoot() = board.shoot()
    fun togglePause() = board.togglePause()
    fun getScore() = board.score
    fun getLevel() = board.level
    fun canShoot() = board.score >= 20

    inner class GameThread(private val holder: SurfaceHolder) : Thread() {
        var running = false
        override fun run() {
            while (running) {
                val canvas = holder.lockCanvas() ?: continue
                board.update(0.016f)
                canvas.drawColor(Color.BLACK)
                // Draw logic...
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}

