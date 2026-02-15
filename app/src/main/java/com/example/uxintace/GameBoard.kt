package com.example.uxintace
import kotlin.random.Random

class GameBoard(val rows: Int = 60, val cols: Int = 60) {
    var grid = Array(rows) { Array(cols) { Cell(0) } }
    var cursorRow = rows / 2
    var cursorCol = cols / 2
    var grabbedRow = -1
    var grabbedCol = -1
    var isGrabbing = false
    var score = 0
    var level = 1
    var isPaused = false
    private var moveTimer = 0f
    private val random = Random(System.currentTimeMillis())

    fun moveCursor(dx: Int, dy: Int) {
        if (isPaused) return
        cursorRow = (cursorRow + dy).coerceIn(0, rows - 1)
        cursorCol = (cursorCol + dx).coerceIn(0, cols - 1)
    }

    fun toggleGrab() {
        if (isPaused) return
        if (!isGrabbing) {
            if (!grid[cursorRow][cursorCol].isEmpty) {
                grabbedRow = cursorRow
                grabbedCol = cursorCol
                isGrabbing = true
            }
        } else {
            val grabbedCell = grid[grabbedRow][grabbedCol]
            grid[grabbedRow][grabbedCol] = grid[cursorRow][cursorCol]
            grid[cursorRow][cursorCol] = grabbedCell
            isGrabbing = false
            checkMatches()
        }
    }

    fun shoot(): Boolean {
        if (isPaused || score < 20) return false
        grid[cursorRow][cursorCol] = Cell(0)
        score -= 20
        return true
    }

    fun update(delta: Float) {
        if (isPaused) return
        moveTimer += delta
        if (moveTimer > 2.0f) {
            moveTimer = 0f
            shiftAndAdd()
        }
    }

    private fun shiftAndAdd() {
        for (r in 0 until rows) {
            for (c in cols - 1 downTo 1) grid[r][c] = grid[r][c-1]
            grid[r][0] = Cell(random.nextInt(1, 6))
        }
    }

    private fun checkMatches() { /* Match logic here */ }
    fun togglePause() { isPaused = !isPaused }
}

