package com.example.uxintace
import kotlin.random.Random

class GameBoard(val rows: Int = 60, val cols: Int = 60) {
    var grid = Array(rows) { Array(cols) { Cell(0) } }
    var cursorRow = 30; var cursorCol = 30
    var score = 0; var level = 1; var isPaused = false
    private var spawnTimer = 0f

    init {
        for (i in 0..20) grid[Random.nextInt(rows)][Random.nextInt(cols)] = Cell(Random.nextInt(1, 5))
    }

    fun moveCursor(dx: Int, dy: Int) {
        if (!isPaused) {
            cursorRow = (cursorRow + dy).coerceIn(0, rows - 1)
            cursorCol = (cursorCol + dx).coerceIn(0, cols - 1)
        }
    }

    fun toggleGrab() { /* Itt lehet a csere logika */ }
    fun shoot() { if(!isPaused && !grid[cursorRow][cursorCol].isEmpty) { grid[cursorRow][cursorCol] = Cell(0); score += 10 } }
    
    fun update(delta: Float) {
        if (isPaused) return
        spawnTimer += delta
        if (spawnTimer > 2f) {
            spawnTimer = 0f
            grid[Random.nextInt(rows)][Random.nextInt(cols)] = Cell(Random.nextInt(1, 5))
        }
    }
}
