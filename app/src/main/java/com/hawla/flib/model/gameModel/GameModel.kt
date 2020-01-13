package com.hawla.flib.model.gameModel

import com.hawla.flib.model.GameType
import com.hawla.flib.model.playPatternModel.PlayPattern

class GameModel {

    //TODO
    constructor(gameType: GameType, sizeX: Int, sizeY: Int, hearts: Int, rounds: Int, roundsIncrease: Boolean, playPattern: PlayPattern)
    constructor(gameType: GameType, sizeX: Int, sizeY: Int, hearts: Int, rounds: Int, roundsIncrease: Boolean, time: Long)
}