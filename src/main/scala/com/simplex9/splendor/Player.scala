package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class Player (
                  cards : Array[Byte],
                  coins : Array[Byte],
                  golds : Byte,
                  points : Byte,
                  reserve : Array[VisibleCard]
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

  def transform(action: Action, state: State): Player = {
    var newPoints = points
    val newCoions =
      if (action.coins.isDefined)
        coins.zip(action.coins.get).map{case (count, delta) => (count + delta).toByte}
      else coins
    val newGold = (golds + action.gold).toByte
    if (action.noble.isDefined) newPoints += Param.NOBLE_POINT
    val newCards =
      if (action.card.isDefined && !action.reserve) {
        val card = action.card.get
        newPoints += card.point
        Util.updateArray(cards, card.color.id, (c : Byte) => (c + 1).toByte)
      } else cards
    val newReserve =
      if (action.card.isEmpty ||
        (!action.reserve && !action.card.get.isReserved)) reserve
      else {
        val card = action.card.get
        if (action.reserve) {
          reserve :+ card.reserve(reserve.length)
        } else {
          // buy reserved card
          Util.deleteFromArray(reserve, card.pos)
        }
      }
    Player(newCards, newCoions, newGold, newPoints, newReserve)
  }
}
