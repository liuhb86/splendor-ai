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

  def transform(action: Action, state: State): Player = {
    var newPoints = points
    val newCoions =
      if (action.coins.isDefined)
        coins.zip(action.coins.get).map{case (count, delta) => (count + delta).toByte}
      else coins
    val newGold = (golds + action.gold).toByte
    if (action.noble.isDefined) newPoints += Param.NOBLE_POINT
    val newCards =
      if (action.cardPosition.isDefined && !action.reserve) {
        val pos = action.cardPosition.get
        val card =
          if (pos._1 >= 0) state.cards(pos._1)(pos._2)
          else reserve(pos._2)
        newPoints += card.point
        Util.updateArray(cards, card.color.id, (c : Byte) => (c + 1).toByte)
      } else cards
    val newReserve =
      if (action.cardPosition.isEmpty ||
        (!action.reserve && action.cardPosition.get._1 >=0)) reserve
      else {
        val pos = action.cardPosition.get
        if (action.reserve) {
          val card = if (pos._2 >= 0) state.cards(pos._1)(pos._2) else null
          reserve :+ card
        } else {
          // buy reserved card
          Util.deleteFromArray(reserve, pos._2)
        }
      }
    Player(newCards, newCoions, newGold, newPoints, newReserve)
  }
}
