package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
case class Card(
               level : Int,
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
  def apply(level: Int, color:Color, point: Short,
            white:Short, byte:Short, green:Short, red:Short, onyx: Short) : Card =
    Card(level, color, point, Array(white, byte, green, red, onyx))
}
