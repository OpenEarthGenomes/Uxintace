package com.example.uxintace
import android.content.Context
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var gv: GameView
    private val handler = Handler(Looper.getMainLooper())
    private var moveRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gv = findViewById(R.id.gameView)

        // MENTÉS BEOLVASÁSA
        val prefs = getSharedPreferences("UxintaceSave", Context.MODE_PRIVATE)
        val savedScore = prefs.getInt("savedScore", 0)
        gv.board.score = savedScore
        findViewById<TextView>(R.id.scoreText).text = "SCORE: $savedScore"

        // AUTOMATIKUS MENTÉS COMMIT-AL
        gv.board.onScoreChanged = { newScore ->
            prefs.edit().putInt("savedScore", newScore).commit()
            runOnUiThread { findViewById<TextView>(R.id.scoreText).text = "SCORE: $newScore" }
        }

        fun setupBtn(id: Int, dx: Int, dy: Int) {
            findViewById<Button>(id).setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    moveRunnable = object : Runnable {
                        override fun run() { gv.board.moveCursor(dx, dy); handler.postDelayed(this, 80) }
                    }
                    handler.post(moveRunnable!!)
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    moveRunnable?.let { handler.removeCallbacks(it) }
                }
                false
            }
        }

        setupBtn(R.id.btnUp, 0, -1); setupBtn(R.id.btnDown, 0, 1)
        setupBtn(R.id.btnLeft, -1, 0); setupBtn(R.id.btnRight, 1, 0)
        findViewById<Button>(R.id.btnGrab).setOnClickListener { gv.board.toggleGrab() }
        findViewById<Button>(R.id.btnShoot).setOnClickListener { gv.board.shoot() }
        findViewById<Button>(R.id.btnPause).setOnClickListener { gv.board.isPaused = !gv.board.isPaused }
        findViewById<Button>(R.id.btnExit).setOnClickListener { finishAffinity(); exitProcess(0) }
    }
}
