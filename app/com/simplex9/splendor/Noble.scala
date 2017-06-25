package com.simplex9.splendor

import com.fasterxml.jackson.annotation.JsonIgnore

/**
  * Created by hongbo on 6/10/17.
  */
case class Noble(
  cards : Array[Short]
                 )
{
  @JsonIgnore
  val totalCards = cards.sum
  @JsonIgnore
  val cardScore = cards.map (c => Param.NOBLE_POINT * Param.POINT_VALUE * c / totalCards )
}

object Noble {
  def apply(white:Short, byte:Short, green:Short, red:Short, onyx: Short) : Noble =
    Noble(Array(white, byte, green, red, onyx))
}
