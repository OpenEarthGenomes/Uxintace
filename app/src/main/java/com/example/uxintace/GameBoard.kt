package com.example.uxintace
import kotlin.random.Random

class Cell(var color: Int)

class GameBoard(val rows: Int = 60, val cols: Int = 60) {
    var grid = Array(rows) { Array(cols) { Cell(0) } }
    var cursorRow = 30; var cursorCol = 30
    var grabbedRow = -1; var grabbedCol = -1
    var isGrabbing = false
    var score = 0; var isPaused = false; var isGameOver = false
    private var pushTimer = 0f

    init { spawnInitialBlocks() }

    fun spawnInitialBlocks() {
        for (r in 0 until rows) {
            for (c in 0 until 10) {
                if (Random.nextInt(100) > 70) grid[r][c].color = Random.nextInt(1, 5)
            }
        }
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
            if (grid[cursorRow][cursorCol].color != 0) {
                grabbedRow = cursorRow; grabbedCol = cursorCol; isGrabbing = true
            }
        } else {
            val temp = grid[cursorRow][cursorCol].color
            grid[cursorRow][cursorCol].color = grid[grabbedRow][grabbedCol].color
            grid[grabbedRow][grabbedCol].color = temp
            isGrabbing = false
            checkMatches(cursorRow, cursorCol)
        }
    }

    private fun checkMatches(r: Int, c: Int) {
        val color = grid[r][c].color
        if (color == 0) return
        val hList = mutableListOf(Pair(r, c))
        var i = c - 1; while (i >= 0 && grid[r][i].color == color) { hList.add(Pair(r, i)); i-- }
        i = c + 1; while (i < cols && grid[r][i].color == color) { hList.add(Pair(r, i)); i++ }
        if (hList.size >= 5) {
            score += hList.size * 20
            hList.forEach { grid[it.first][it.second].color = 0 }
        }
    }

    fun update(delta: Float) {
        if (isPaused || isGameOver) return
        pushTimer += delta
        if (pushTimer > 1.2f) { // 1.2 másodpercenként lépnek
            pushTimer = 0f
            for (r in 0 until rows) {
                for (c in cols - 1 downTo 1) {
                    grid[r][c].color = grid[r][c - 1].color
                }
                grid[r][0].color = if (Random.nextInt(100) > 85) Random.nextInt(1, 5) else 0
            }
        }
    }

    fun shoot() {
        if (!isPaused && grid[cursorRow][cursorCol].color != 0) {
            grid[cursorRow][cursorCol].color = 0
            score += 10
        }
    }
}
