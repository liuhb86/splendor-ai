package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class Player (
                  cards : Array[Byte],
                  coins : Array[Byte],
                  golds : Byte,
                  points : Byte,
                  reserve : Array[Card]
                  )
{
  def calcScore(state : State): Int = {
    if (points >= 15) {
      return Param.WINNING_SCORE + points * Param.POINT_VALUE - cards.sum
    }

    points * Param.POINT_VALUE +
      (coins.sum + cards.sum) * Param.COIN_VALUE +
    golds * Param.GOLD_VALUE
  }
}
