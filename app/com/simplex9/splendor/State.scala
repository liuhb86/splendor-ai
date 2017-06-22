package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class State (
                   cards : Array[VisibleCard],
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


  def transform(action: Action) : State = {
    val newCoins =
      if (action.coins.isDefined)
        coins.zip(action.coins.get).map{case (count, delta) => (count - delta).toByte}
      else coins
    val newGold = (golds - action.gold).toByte
    val newNobles =
      if (action.noble.isDefined)
        nobles.filter(_ != action.noble.get)
      else nobles
    val newCards =
      if (action.card.isDefined &&
        !action.card.get.isReserved &&
        !action.card.get.isInPile) {
        val card = action.card.get
        Util.updateArray(cards, card.getOffset, null)
      } else cards
    val newPlayers = Util.updateArray(players, action.playerIndex, (p : Player) => p.transform(action, this))
    State(newCards, newNobles, newCoins, newGold, newPlayers)
  }
}
