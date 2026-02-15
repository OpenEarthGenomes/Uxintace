package com.example.uxintace
import kotlin.random.Random

class GameBoard(val rows: Int = 60, val cols: Int = 60) {
    var grid = Array(rows) { Array(cols) { Cell(0) } }
    var cursorRow = 30; var cursorCol = 30
    var grabbedRow = -1; var grabbedCol = -1
    var isGrabbing = false
    var score = 0; var level = 1; var isPaused = false; var isGameOver = false
    private var pushTimer = 0f
    private val pushThreshold = 2.5f

    fun reset() {
        grid = Array(rows) { Array(cols) { Cell(0) } }
        score = 0; isGameOver = false; isGrabbing = false; pushTimer = 0f
    }

    fun moveCursor(dx: Int, dy: Int) {
        if (!isPaused && !isGameOver) {
            cursorRow = (cursorRow + dy).coerceIn(0, rows - 1)
            cursorCol = (cursorCol + dx).coerceIn(0, cols - 1)
        }
    }

    fun toggleGrab() {
        if (isPaused || isGameOver) return
        if (!isGrabbing) {
            if (!grid[cursorRow][cursorCol].isEmpty) {
                grabbedRow = cursorRow; grabbedCol = cursorCol; isGrabbing = true
            }
        } else {
            val temp = grid[cursorRow][cursorCol]
            grid[cursorRow][cursorCol] = grid[grabbedRow][grabbedCol]
            grid[grabbedRow][grabbedCol] = temp
            isGrabbing = false
            grabbedRow = -1; grabbedCol = -1
        }
    }

    fun update(delta: Float) {
        if (isPaused || isGameOver) return
        pushTimer += delta
        if (pushTimer > pushThreshold) {
            pushTimer = 0f
            for (r in 0 until rows) if (!grid[r][cols - 1].isEmpty) { isGameOver = true; return }
            for (r in 0 until rows) {
                for (c in cols - 1 downTo 1) grid[r][c] = grid[r][c - 1]
                grid[r][0] = if (Random.nextInt(100) > 97) Cell(Random.nextInt(1, 5)) else Cell(0)
            }
            if (isGrabbing) grabbedCol = (grabbedCol + 1).coerceAtMost(cols - 1)
        }
    }
    
    fun shoot() {
        if (!isPaused && !grid[cursorRow][cursorCol].isEmpty) {
            grid[cursorRow][cursorCol] = Cell(0)
            score += 10
        }
    }
}

