package com.simplex9.splendor

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created by hongbo on 6/10/17.
  */
class Game (val numPlayers: Int, autoMode: Boolean) {

  var turn : Int = 0

  var actionList : List[Action] = Nil

  var cardPile : CardPile = initPile()

  var state : State = initState()

  def initPile() : CardPile = {
    if (!autoMode) return null
    CardPile.shuffle()
  }

  def initCards() = {
    val cards = new ArrayBuffer[VisibleCard]()
    for (i <- 0 until Param.NUM_CARD_LEVEL) {
      for (j <- 0 until Param.NUM_CARD_EACH_LEVEL) {
        var card : VisibleCard = null
        if (autoMode) {
          val (nextCard, newPile) = cardPile.take(i, j)
          cardPile = newPile
          card = nextCard
        }
        cards += card
      }
    }
    cards.toArray
  }

  def initNoble() : Array[Noble] = {
    val numNoble = numPlayers + 1
    if (!autoMode) return new Array[Noble](numNoble)
    Random.shuffle(Deck.nobles.toSeq).take(numNoble).toArray
  }

  def initState() : State = {
    val cards = initCards()
    val nobles = initNoble()
    val numCoins = Param.NUM_COIN(numPlayers)
    val newPlayer = Player(
      Seq.fill(Color.size)(0.toShort).toArray,
      Seq.fill(Color.size)(0.toShort).toArray,
      0,
      0,
      Array()
    )

    State(
      cards, nobles,
        Seq.fill(Color.size)(numCoins.toShort).toArray,
      Param.NUM_GOLD,
      Seq.fill(numPlayers)(newPlayer).toArray
    )
  }

  def pass() = {
    this.turn = (this.turn + 1) % this.numPlayers
  }

  def takeAction(action: Action) = {
    var adjustedAction = action
    if (autoMode && action.card.isDefined && action.card.get.isInPile) {
      val oldCard = action.card.get
      val (card, newPile) = cardPile.take(oldCard.group, oldCard.pos)
      cardPile = newPile
      adjustedAction = Action(action.playerIndex, action.coins, action.gold,
        Option(card), action.reserve, action.noble)
    }
    state = state.transform(adjustedAction)
    if (autoMode && action.card.isDefined) {
      val card = action.card.get
      if (!(card.isInPile || card.isReserved)) {
        val (newCard, newPile) = cardPile.take(card.group, card.pos)
        cardPile = newPile
        state = state.setCard(newCard)
      }
    }
    actionList = action :: actionList
  }


}

object Game{
  var game : Game = _
}