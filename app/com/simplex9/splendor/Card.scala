package com.simplex9.splendor

import com.simplex9.splendor.Color.Color

/**
  * Created by hongbo on 6/10/17.
  */
case class Card(
               color : Color,
               point : Byte,
               price : Array[Byte]
               ) {
  def isSecret = point < 0
}

object Card {
  def apply(color:Color, point: Byte,
            white:Byte, byte:Byte, green:Byte, red:Byte, onyx: Byte) =
    Card(color, point, Array(white, byte, green, red, onyx))
  final val secretCard = Card(null, -1, null)
}
