package com.simplex9.splendor

import com.fasterxml.jackson.annotation.JsonIgnore

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


  def coinCount = coins.sum + golds

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

  @JsonIgnore
  def getDominateReserveColor():Array[Short] = {
    val dominateColorsInReservedCard = Array[Short](0,0,0,0,0)
    for(j <- reserve.indices){
      val cardPrice = reserve(j).price
      var maxLack = 0
      var dominateColor = -1
      for (k <- cardPrice.indices) {
        val diff = cardPrice(k) - cards(k) - coins(k)
        if(diff >= Param.MAX_DOMINATE_COLOR_LACK_FOR_RESERVE && diff > maxLack){
          maxLack = diff
          dominateColor = k
        }
      }
      if(dominateColor >= 0) {
        dominateColorsInReservedCard(dominateColor) = (dominateColorsInReservedCard(dominateColor) + 1).toShort
      }
    }
    dominateColorsInReservedCard
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

  def updateReservedCard(newCard: VisibleCard): Player = {
    val newReserve = Util.updateArray(reserve, newCard.pos, newCard)
    Player(cards, coins, golds, points, newReserve)
  }
}
