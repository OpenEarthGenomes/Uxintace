package com.example.uxintace

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private lateinit var scoreText: TextView
    private lateinit var levelText: TextView
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Belógás kezelése (Android 15/16 edge-to-edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gameView = findViewById(R.id.gameView)
        scoreText = findViewById(R.id.scoreText)
        levelText = findViewById(R.id.levelText)

        findViewById<Button>(R.id.btnUp).setOnClickListener { gameView.moveCursor(0, -1) }
        findViewById<Button>(R.id.btnDown).setOnClickListener { gameView.moveCursor(0, 1) }
        findViewById<Button>(R.id.btnLeft).setOnClickListener { gameView.moveCursor(-1, 0) }
        findViewById<Button>(R.id.btnRight).setOnClickListener { gameView.moveCursor(1, 0) }
        findViewById<Button>(R.id.btnGrab).setOnClickListener { gameView.toggleGrab() }
        findViewById<Button>(R.id.btnShoot).setOnClickListener { gameView.shoot() }
        findViewById<Button>(R.id.btnPause).setOnClickListener { gameView.togglePause() }

        startUiUpdater()
    }

    private fun startUiUpdater() {
        uiScope.launch {
            while (isActive) {
                scoreText.text = "Score: ${gameView.getScore()}"
                levelText.text = "Level: ${gameView.getLevel()}"
                findViewById<Button>(R.id.btnShoot).isEnabled = gameView.canShoot()
                delay(100)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiScope.cancel()
    }
}

