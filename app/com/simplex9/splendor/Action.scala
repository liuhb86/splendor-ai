package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class Action(
            playerIndex : Int,
            coins: Option[Array[Short]],
            gold: Short,
            card : Option[VisibleCard],
            reserve: Boolean,
            noble : Option[Noble]
            ) {
  override def toString: String = {
    val buffer = new StringBuilder
    buffer.append(s"$playerIndex:")
    if (card.isDefined) {
      if (reserve) {
        buffer.append(" RSV:")
      } else {
        buffer.append(" BUY:")
      }
      buffer.append(card.get.toString)
    }
    if (coins.isDefined) {
      buffer.append(" COIN:")
      buffer.append(Util.colorArrayToString(coins.get))
    }
    if (gold !=0 ) {
      buffer.append(s" GOLD:$gold")
    }
    if (noble.isDefined) {
      buffer.append(" ")
      buffer.append(noble.get.toString)
    }
    buffer.toString()
  }
}
