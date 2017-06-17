package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class State (
                   cards : Array[Array[Card]],
                   nobles : Array[Noble],
                   coins : Array[Byte],
                   golds : Byte,
                   players: Array[Player]
                 )
{
  def calcScore(iPlayer : Int): Int = {
    var maxOpponent = -Param.INF
    for (i <- players.indices) {
      if (i != iPlayer) {
        val score = players(i).calcScore(this)
        if (score > maxOpponent) maxOpponent = score
      }
    }
    players(iPlayer).calcScore(this) - maxOpponent
  }
}
