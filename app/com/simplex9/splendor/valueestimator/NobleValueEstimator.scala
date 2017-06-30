package com.simplex9.splendor.valueestimator

import com.simplex9.splendor.Color
import com.simplex9.splendor.{Param, Player, State}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by hongbo on 6/18/17.
  */
class NobleValueEstimator(state: State, player: Player) {
  val lastColor = new Array[Option[Color.Color]](state.nobles.length)
  val satisfy = new Array[Boolean](state.nobles.length)
  val values = estimate()

  def estimate() = {
    val lastColorBuf = ArrayBuffer[Option[Color.Color]]()
    val satisfyBuf = ArrayBuffer[Boolean]()
    val values = new Array[Int](player.coins.length)
    for (noble <- state.nobles) {
      val lacks = new Array[Int](values.length)
      for (i <- noble.cards.indices) {
        lacks(i) = Math.max( noble.cards(i) - player.cards(i), 0)
      }

      val totalLacks = lacks.sum
      if (totalLacks > 0) {
        val cardValue = Param.NOBLE_POINT * Param.POINT_VALUE / totalLacks
        for (i<-values.indices) {
          if (lacks(i) > 0) values(i) = values(i) + cardValue
        }
      }

      val color =
        if (totalLacks == 1) Some(Color(lacks.indexOf(1)))
        else None
      lastColorBuf += color
      satisfyBuf += (totalLacks == 0)
    }

    for (i <- state.nobles.indices) {
      lastColor(i) = lastColorBuf(i)
      satisfy(i) = satisfyBuf(i)
    }
    values
  }

}
