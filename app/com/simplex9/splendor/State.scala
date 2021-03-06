package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class State (
                   cards : Array[VisibleCard],
                   nobles : Array[Noble],
                   coins : Array[Short],
                   golds : Short,
                   players: Array[Player]
                 )
{

  def transform(action: Action) : State = {
    val newCoins =
      if (action.coins.isDefined)
        coins.zip(action.coins.get).map{case (count, delta) => (count - delta).toShort}
      else coins
    val newGold = (golds - action.gold).toShort
    val newNobles =
      if (action.noble.isDefined)
        nobles.filter(_ != action.noble.get)
      else nobles
    val newCards =
      if (action.card.isDefined &&
        !action.card.get.isReserved &&
        !action.card.get.isInPile) {
        val card = action.card.get
        Util.updateArray(cards, card.getOffset, null.asInstanceOf[VisibleCard])
      } else cards
    val newPlayers = Util.updateArray(players, action.playerIndex, (p : Player) => p.transform(action, this))
    State(newCards, newNobles, newCoins, newGold, newPlayers)
  }

  def setCard(newCard: VisibleCard): State = {
      if (!newCard.isReserved) {
        val newCards = Util.updateArray(cards, newCard.getOffset, newCard)
        State(newCards, nobles, coins, golds, players)
      } else {
        val newPlayers = Util.updateArray(players, newCard.reservedBy, (p : Player) => p.updateReservedCard(newCard))
        State(cards, nobles, coins, golds, newPlayers)
      }
  }

  def setNoble(index: Int, noble: Noble): State = {
    val newNobles = Util.updateArray(nobles, index, noble)
    State(cards, newNobles, coins, golds, players)
  }

}
