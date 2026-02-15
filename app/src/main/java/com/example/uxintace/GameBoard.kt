package com.example.uxintace
import kotlin.random.Random

class GameBoard(val rows: Int = 60, val cols: Int = 60) {
    var grid = Array(rows) { Array(cols) { Cell(0) } }
    var cursorRow = 30; var cursorCol = 30
    var grabbedRow = -1; var grabbedCol = -1
    var isGrabbing = false
    var score = 0; var isPaused = false; var isGameOver = false
    private var pushTimer = 0f

    fun reset() {
        grid = Array(rows) { Array(cols) { Cell(0) } }
        score = 0; isGameOver = false; isPaused = false; isGrabbing = false; pushTimer = 0f
    }

    fun moveCursor(dx: Int, dy: Int) {
        if (!isGameOver) {
            cursorRow = (cursorRow + dy).coerceIn(0, rows - 1)
            cursorCol = (cursorCol + dx).coerceIn(0, cols - 1)
        }
    }

    fun toggleGrab() {
        if (isGameOver || isPaused) return
        if (!isGrabbing) {
            // Megpróbáljuk megfogni a kockát
            if (grid[cursorRow][cursorCol].color != 0) {
                grabbedRow = cursorRow
                grabbedCol = cursorCol
                isGrabbing = true
            }
        } else {
            // Csere végrehajtása
            val temp = grid[cursorRow][cursorCol]
            grid[cursorRow][cursorCol] = grid[grabbedRow][grabbedCol]
            grid[grabbedRow][grabbedCol] = temp
            isGrabbing = false
        }
    }

    fun update(delta: Float) {
        if (isPaused || isGameOver) return
        pushTimer += delta
        if (pushTimer > 2.5f) {
            pushTimer = 0f
            // Game Over ellenőrzés a jobb szélen
            for (r in 0 until rows) {
                if (grid[r][cols - 1].color != 0) {
                    isGameOver = true
                    return
                }
            }
            // Tolás jobbra
            for (r in 0 until rows) {
                for (c in cols - 1 downTo 1) grid[r][c] = grid[r][c - 1]
                grid[r][0] = if (Random.nextInt(100) > 97) Cell(Random.nextInt(1, 5)) else Cell(0)
            }
            // Ha fogunk valamit, az is tolódik!
            if (isGrabbing) grabbedCol = (grabbedCol + 1).coerceAtMost(cols - 1)
        }
    }

    fun shoot() {
        if (!isPaused && grid[cursorRow][cursorCol].color != 0) {
            grid[cursorRow][cursorCol] = Cell(0)
            score += 10
        }
    }
}
