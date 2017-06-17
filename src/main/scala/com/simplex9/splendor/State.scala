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


  def transform(action: Action) : State = {
    val newCoions =
      if (action.coins.isDefined)
        coins.zip(action.coins.get).map{case (count, delta) => (count - delta).toByte}
      else coins
    val newGold = (golds - action.gold).toByte
    val newNobles =
      if (action.noble.isDefined)
        nobles.filter(_ != action.noble.get)
      else nobles
    val newCards =
      if (action.cardPosition.isDefined &&
        action.cardPosition.get._1 >= 0 &&
        action.cardPosition.get._2 >=0) {
        val pos = action.cardPosition.get
        Util.updateArray(cards, pos._1, (c : Array[Card]) => Util.updateArray(c, pos._2, null))
      } else cards
    val newPlayers = Util.updateArray(players, action.playerIndex, (p : Player) => p.transform(action, this))
    State(newCards, newNobles, newCoions, newGold, newPlayers)
  }
}
