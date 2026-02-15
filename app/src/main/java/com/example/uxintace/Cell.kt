package com.example.uxintace
data class Cell(val color: Int) {
    val isEmpty: Boolean get() = color == 0
}
