package com.example.uxintace

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var activeMoveRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TELJES KÉPERNYŐ BEÁLLÍTÁSA (Android 14 kompatibilis)
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val gv = findViewById<GameView>(R.id.gameView)
        val scoreTv = findViewById<TextView>(R.id.scoreText)
        val pauseBtn = findViewById<Button>(R.id.btnPause)

        // UI Frissítés
        Timer().scheduleAtFixedRate(timerTask {
            runOnUiThread {
                scoreTv.text = "Score: ${gv.board.score}"
                if (gv.board.isGameOver) pauseBtn.text = "🔄" 
                else pauseBtn.text = if (gv.board.isPaused) "▶️" else "⏸️"
            }
        }, 0, 100)

        // GYORS MOZGÁS LOGIKA (Nyomva tartás)
        fun setupRepeatButton(button: Button, dx: Int, dy: Int) {
            button.setOnLongClickListener {
                activeMoveRunnable = object : Runnable {
                    override fun run() {
                        gv.board.moveCursor(dx, dy)
                        handler.postDelayed(this, 80) // Sebesség: 80ms
                    }
                }
                handler.post(activeMoveRunnable!!)
                true
            }
            button.setOnClickListener { gv.board.moveCursor(dx, dy) }
        }

        setupRepeatButton(findViewById(R.id.btnUp), 0, -1)
        setupRepeatButton(findViewById(R.id.btnDown), 0, 1)
        setupRepeatButton(findViewById(R.id.btnLeft), -1, 0)
        setupRepeatButton(findViewById(R.id.btnRight), 1, 0)

        // Stop mozgás elengedéskor
        val stopRunnable = View.OnTouchListener { v, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP || event.action == android.view.MotionEvent.ACTION_CANCEL) {
                activeMoveRunnable?.let { handler.removeCallbacks(it) }
            }
            false
        }
        findViewById<Button>(R.id.btnUp).setOnTouchListener(stopRunnable)
        findViewById<Button>(R.id.btnDown).setOnTouchListener(stopRunnable)
        findViewById<Button>(R.id.btnLeft).setOnTouchListener(stopRunnable)
        findViewById<Button>(R.id.btnRight).setOnTouchListener(stopRunnable)

        findViewById<Button>(R.id.btnGrab).setOnClickListener { gv.board.toggleGrab() }
        findViewById<Button>(R.id.btnShoot).setOnClickListener { gv.board.shoot() }
        pauseBtn.setOnClickListener { 
            if (gv.board.isGameOver) gv.board.reset() else gv.board.isPaused = !gv.board.isPaused 
        }
    }

    // MEMÓRIA VÉDELEM: Ha háttérbe megy, állítsuk le a szálat
    override fun onPause() {
        super.onPause()
        findViewById<GameView>(R.id.gameView).stopThread()
    }
}
