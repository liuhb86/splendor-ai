package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class Player (
                  cards : Array[Short],
                  coins : Array[Short],
                  golds : Short,
                  points : Short,
                  reserve : Array[VisibleCard]
                  )
{
  def calcScore(state : State): Int = {
    if (points >= 15) {
      return Param.WINNING_SCORE + points * Param.POINT_VALUE - cards.sum
    }

    points * Param.POINT_VALUE +
      coins.sum * Param.COIN_VALUE +
      cards.sum * Param.CARD_VALUE +
      golds * Param.GOLD_VALUE
  }

  def transform(action: Action, state: State): Player = {
    var newPoints = points
    val newCoions =
      if (action.coins.isDefined)
        coins.zip(action.coins.get).map{case (count, delta) => (count + delta).toShort}
      else coins
    val newGold = (golds + action.gold).toShort
    if (action.noble.isDefined) newPoints = (newPoints + Param.NOBLE_POINT).toShort
    val newCards =
      if (action.card.isDefined && !action.reserve) {
        val card = action.card.get
        newPoints = (newPoints + card.point).toShort
        Util.updateArray(cards, card.color.id, (c : Short) => (c + 1).toShort)
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
          removeReservedCard(card.pos)
        }
      }
    Player(newCards, newCoions, newGold, newPoints, newReserve)
  }

  def removeReservedCard(pos : Int) : Array[VisibleCard] = {
    val newReserve = new Array[VisibleCard](reserve.length - 1)
    for (i <- 0 until pos) {
      newReserve(i) = reserve(i)
    }
    for (i<- pos + 1 until reserve.length) {
      newReserve(i - 1) = reserve(i).reserve(i - 1)
    }
    newReserve
  }
}
