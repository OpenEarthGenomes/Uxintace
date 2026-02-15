package com.example.uxintace
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var moveRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gv = findViewById<GameView>(R.id.gameView)
        val scoreTv = findViewById<TextView>(R.id.scoreText)
        val pauseBtn = findViewById<Button>(R.id.btnPause)

        // UI Frissítés 100ms-enként
        Timer().scheduleAtFixedRate(timerTask {
            runOnUiThread {
                scoreTv.text = "Score: ${gv.board.score}"
                if (gv.board.isGameOver) pauseBtn.text = "🔄" else pauseBtn.text = if (gv.board.isPaused) "▶️" else "⏸️"
            }
        }, 0, 100)

        // Gyors mozgás beállítása
        fun setupBtn(btn: Button, dx: Int, dy: Int) {
            btn.setOnLongClickListener {
                moveRunnable = object : Runnable {
                    override fun run() {
                        gv.board.moveCursor(dx, dy)
                        handler.postDelayed(this, 70)
                    }
                }
                handler.post(moveRunnable!!)
                true
            }
            btn.setOnClickListener { gv.board.moveCursor(dx, dy) }
        }

        setupBtn(findViewById(R.id.btnUp), 0, -1)
        setupBtn(findViewById(R.id.btnDown), 0, 1)
        setupBtn(findViewById(R.id.btnLeft), -1, 0)
        setupBtn(findViewById(R.id.btnRight), 1, 0)

        val stop = View.OnTouchListener { _, ev -> 
            if (ev.action == android.view.MotionEvent.ACTION_UP) moveRunnable?.let { handler.removeCallbacks(it) }
            false 
        }
        findViewById<Button>(R.id.btnUp).setOnTouchListener(stop)
        findViewById<Button>(R.id.btnDown).setOnTouchListener(stop)
        findViewById<Button>(R.id.btnLeft).setOnTouchListener(stop)
        findViewById<Button>(R.id.btnRight).setOnTouchListener(stop)

        findViewById<Button>(R.id.btnGrab).setOnClickListener { gv.board.toggleGrab() }
        findViewById<Button>(R.id.btnShoot).setOnClickListener { gv.board.shoot() }
        pauseBtn.setOnClickListener { if (gv.board.isGameOver) gv.board.reset() else gv.board.isPaused = !gv.board.isPaused }
    }
}
