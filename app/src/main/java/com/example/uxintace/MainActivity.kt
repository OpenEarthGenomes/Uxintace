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

        // Mentés betöltése
        val prefs = getSharedPreferences("UxintaceSave", Context.MODE_PRIVATE)
        gv.board.score = prefs.getInt("savedScore", 0)

        // UI Frissítő szál a Score-hoz
        val uiThread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    runOnUiThread {
                        scoreTv.text = "SCORE: ${gv.board.score}"
                        scoreTv.bringToFront()
                    }
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    break
                }
            }
        }
        uiThread.start()

        // Gombok beállítása findViewById-val (így nem lesz Unresolved reference hiba)
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
            saveData()
            finishAffinity()
            exitProcess(0)
        }
    }

    private fun saveData() {
        val prefs = getSharedPreferences("UxintaceSave", Context.MODE_PRIVATE)
        prefs.edit().putInt("savedScore", gv.board.score).apply()
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }
}
