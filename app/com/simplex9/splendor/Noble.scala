package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class Noble(
  cards : Array[Byte]
                 )
{
  val totalCards = cards.sum
  val cardScore = cards.map (c => Param.NOBLE_POINT * Param.POINT_VALUE * c / totalCards )
}

object Noble {
  def apply(white:Byte, byte:Byte, green:Byte, red:Byte, onyx: Byte) = Noble(Array(white, byte, green, red, onyx))
}
