package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class Card(
               color : Color,
               point : Short,
               price : Array[Short]
               ) {
  def isSecret = point < 0

  override def toString: String = {
    s"(${Util.colorInitial(color.id)}$point) ${Util.colorArrayToString(price)}"
  }
}

object Card {
  def apply(color:Color, point: Short,
            white:Short, byte:Short, green:Short, red:Short, onyx: Short) : Card =
    Card(color, point, Array(white, byte, green, red, onyx))
  final val secretCard = Card(null, -1, null)
}
