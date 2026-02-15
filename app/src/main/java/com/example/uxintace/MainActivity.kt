package com.example.uxintace
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var gv: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gv = findViewById(R.id.gameView)
        val scoreTv = findViewById<TextView>(R.id.scoreText)
        val pauseBtn = findViewById<Button>(R.id.btnPause)
        val exitBtn = findViewById<Button>(R.id.btnExit)

        val prefs = getSharedPreferences("UxintaceSave", Context.MODE_PRIVATE)
        gv.board.score = prefs.getInt("savedScore", 0)

        Thread {
            while (true) {
                try {
                    runOnUiThread { scoreTv.text = "SCORE: ${gv.board.score}" }
                    Thread.sleep(200)
                } catch (e: Exception) {}
            }
        }.start()

        findViewById<Button>(R.id.btnUp).setOnClickListener { gv.board.moveCursor(0, -1) }
        findViewById<Button>(R.id.btnDown).setOnClickListener { gv.board.moveCursor(0, 1) }
        findViewById<Button>(R.id.btnLeft).setOnClickListener { gv.board.moveCursor(-1, 0) }
        findViewById<Button>(R.id.btnRight).setOnClickListener { gv.board.moveCursor(1, 0) }
        findViewById<Button>(R.id.btnGrab).setOnClickListener { gv.board.toggleGrab() }
        findViewById<Button>(R.id.btnShoot).setOnClickListener { gv.board.shoot() }
        
        pauseBtn.setOnClickListener { 
            if (gv.board.isGameOver) gv.board.reset() else gv.board.isPaused = !gv.board.isPaused 
        }

        exitBtn.setOnClickListener {
            getSharedPreferences("UxintaceSave", Context.MODE_PRIVATE).edit().putInt("savedScore", gv.board.score).apply()
            finishAffinity()
            exitProcess(0)
        }
    }
}

