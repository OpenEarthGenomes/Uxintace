package com.example.uxintace

import android.content.Context
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var gv: GameView
    private var uiTimer: Timer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var moveRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Teljes képernyő kényszerítése
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.setDecorFitsSystemWindows(false)

        setContentView(R.layout.activity_main)
        gv = findViewById(R.id.gameView)

        val prefs = getSharedPreferences("UxintaceSave", Context.MODE_PRIVATE)
        gv.board.score = prefs.getInt("savedScore", 0)

        // Gyorsmozgás beállítása
        fun setupBtn(id: Int, dx: Int, dy: Int) {
            val b = findViewById<Button>(id)
            b.setOnTouchListener { _, event ->
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        moveRunnable = object : Runnable {
                            override fun run() {
                                gv.board.moveCursor(dx, dy)
                                handler.postDelayed(this, 60)
                            }
                        }
                        handler.post(moveRunnable!!)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        handler.removeCallbacks(moveRunnable!!)
                    }
                }
                false
            }
        }

        setupBtn(R.id.btnUp, 0, -1); setupBtn(R.id.btnDown, 0, 1)
        setupBtn(R.id.btnLeft, -1, 0); setupBtn(R.id.btnRight, 1, 0)

        findViewById<Button>(R.id.btnGrab).setOnClickListener { gv.board.toggleGrab() }
        findViewById<Button>(R.id.btnShoot).setOnClickListener { gv.board.shoot() }
        findViewById<Button>(R.id.btnPause).setOnClickListener { gv.board.isPaused = !gv.board.isPaused }
        findViewById<Button>(R.id.btnExit).setOnClickListener {
            getSharedPreferences("UxintaceSave", Context.MODE_PRIVATE).edit().putInt("savedScore", gv.board.score).apply()
            finishAffinity()
            exitProcess(0)
        }
    }

    override fun onResume() {
        super.onResume()
        uiTimer = Timer()
        uiTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread { findViewById<TextView>(R.id.scoreText).text = "SCORE: ${gv.board.score}" }
            }
        }, 0, 200)
    }

    override fun onPause() {
        super.onPause()
        uiTimer?.cancel()
    }
}
