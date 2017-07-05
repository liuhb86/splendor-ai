package com.simplex9.splendor

import com.fasterxml.jackson.annotation.JsonIgnore

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created by hongbo on 6/10/17.
  */
class Game (val numPlayers: Int, val autoMode: Boolean) {

  var turn : Int = 0

  var actionList : List[Action] = Nil

  var cardPile : CardPile = initPile()

  var state : State = initState()

  @JsonIgnore
  var undoStates : List[(State, CardPile)] = (state, cardPile) :: Nil


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

  def checkNop() = {
    if (actionList == Nil || actionList.head.playerIndex != turn) {
      addAction(Action(turn, None, 0, None, reserve = false, None))
    }
  }

  def pass() = {
    checkNop
    this.turn = (this.turn + 1) % this.numPlayers
  }

  def checkCollapsable(action : Action): Boolean = {
    if (actionList == Nil || actionList.head.playerIndex != action.playerIndex) return true
    val head = actionList.head
    if (action.noble.isDefined && head.noble.isDefined) return false
    if (action.card.isDefined && head.card.isDefined) return false
    if (action.card.isDefined) {
      if (head.gold > 0) return false
      if (head.coins.isDefined && head.coins.get.exists(_ > 0)) return false
    }
    true
  }

  def takeAction(action: Action) : Unit = {
    if (!checkCollapsable(action)) throw new IllegalAccessException("invalid action")
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
    addAction(adjustedAction)
  }

  def collpaseAction(action: Action, head: Action) = {
    val coins =
      if (action.coins.isEmpty) head.coins
      else if (head.coins.isEmpty) action.coins
      else Some(action.coins.get.zip(head.coins.get).map(t => (t._1 + t._2).toShort))
    val card = if (action.card.isDefined) action.card else head.card
    val noble = if (action.noble.isDefined) action.noble else head.noble
    Action(action.playerIndex, coins, (action.gold + head.gold).toShort,
      card, action.reserve || head.reserve, noble)
  }

  def addAction(action: Action) : Unit = {
    if (actionList == Nil || actionList.head.playerIndex != action.playerIndex) {
      actionList = action :: actionList
      undoStates = (state, cardPile) :: undoStates
    } else {
      val collapsedAction= collpaseAction(action, actionList.head)
      actionList = collapsedAction :: actionList.tail
      undoStates = (state, cardPile) :: undoStates.tail
    }
  }

  def undo() : Unit = {
    if (actionList.isEmpty) return
    turn = actionList.head.playerIndex
    actionList = actionList.tail
    undoStates = undoStates.tail
    state = undoStates.head._1
    cardPile = undoStates.head._2
  }

}

object Game{
  var game : Game = _
}