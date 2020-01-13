package com.hawla.flib.model.playPatternModel

import java.lang.IllegalArgumentException

/**
 * SquareThreePlayPattern represents the Pattern in which the Tiles of the Game flip on touch.
 *
 */
class SquareThreePlayPattern : PlayPattern {

    // 3x3 pattern, true means the tile will flip
    val pattern: Array<Array<Boolean>>
    val PATTERN_SIZE: Int = 3

    constructor(){
        pattern = arrayOf(
            arrayOf(true,true,true),
            arrayOf(true,true,true),
            arrayOf(true,true,true)
        )
    }

    override fun flip(x: Int, y: Int): Boolean{
        if (x < 0 || x >= PATTERN_SIZE) throw IllegalArgumentException("Illegal x value for 3x3 pattern: x={$x}")
        if (y < 0 || y >= PATTERN_SIZE) throw IllegalArgumentException("Illegal y value for 3x3 pattern: y={$y}")
        pattern[x][y] = !pattern[x][y]
        return pattern[x][y]
    }

    override fun toString(): String{
        var string = "|"
        for(yi in 0 until PATTERN_SIZE){
            for (xi in 0 until  PATTERN_SIZE){
                string += " " + pattern[xi][yi] + " |"
            }
            string += "\n|"
        }
        return string
    }
}