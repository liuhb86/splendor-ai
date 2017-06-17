package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
class Action(
            val playerIndex : Int,
            val coins: Option[Array[Byte]],
            val gold: Byte,
            val cardPosition : Option[(Byte, Byte,)],
            val reserve: Boolean,
            val noble : Option[Noble]
            ) {
}
