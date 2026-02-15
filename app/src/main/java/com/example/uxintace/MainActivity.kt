package com.example.uxintace
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gv = findViewById<GameView>(R.id.gameView)
        val scoreTv = findViewById<TextView>(R.id.scoreText)
        val levelTv = findViewById<TextView>(R.id.levelText)

        Timer().scheduleAtFixedRate(timerTask {
            runOnUiThread {
                scoreTv.text = "Score: ${gv.board.score}"
                levelTv.text = "Level: ${gv.board.level}"
            }
        }, 0, 300)

        findViewById<Button>(R.id.btnUp).setOnClickListener { gv.board.moveCursor(0, -1) }
        findViewById<Button>(R.id.btnDown).setOnClickListener { gv.board.moveCursor(0, 1) }
        findViewById<Button>(R.id.btnLeft).setOnClickListener { gv.board.moveCursor(-1, 0) }
        findViewById<Button>(R.id.btnRight).setOnClickListener { gv.board.moveCursor(1, 0) }
        findViewById<Button>(R.id.btnGrab).setOnClickListener { gv.board.toggleGrab() }
        findViewById<Button>(R.id.btnShoot).setOnClickListener { gv.board.shoot() }
        findViewById<Button>(R.id.btnPause).setOnClickListener { gv.board.isPaused = !gv.board.isPaused }
    }
}

