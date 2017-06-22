package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class Action(
            playerIndex : Int,
            coins: Option[Array[Byte]],
            gold: Byte,
            card : Option[VisibleCard],
            reserve: Boolean,
            noble : Option[Noble]
            ) {
}
