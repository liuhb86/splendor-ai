package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
class Game (val numPlayers: Int) {
  var state : State = _

  var turn : Int = 0

  def init(cards: Array[Array[Card]], nobles : Array[Noble]) = {
    val numCoins = Param.NUM_COIN(numPlayers)
    val newPlayer = Player(
      Seq.fill(Color.size)(0.toByte).toArray,
      Seq.fill(Color.size)(0.toByte).toArray,
      0,
      0,
      Array()
    )
    state = State(
      cards, nobles,
        Seq.fill(Color.size)(numCoins.toByte).toArray,
      Param.NUM_GOLD,
      Seq.fill(numPlayers)(newPlayer).toArray
    )
  }
}
