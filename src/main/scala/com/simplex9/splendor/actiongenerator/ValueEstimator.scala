package com.simplex9.splendor.actiongenerator

import com.simplex9.splendor.{Param, Player, State}

/**
  * Created by hongbo on 6/18/17.
  */
class ValueEstimator(state : State, player: Player) {
  val nobleValueForCard = calcNobleValueForCard()
  val normalCardValue = calcNormalCardValue()

  def calcNobleValueForCard() = {
    val values = new Array[Int](player.coins.length)
    for (noble <- state.nobles) {
      val lacks = new Array[Int](values.length)
      for (i <- noble.cards.indices) {
        lacks(i) = Math.max( noble.cards(i) - player.cards(i), 0)
      }

      val totalLacks = lacks.sum
      if (totalLacks > 0) {
        val cardValue = Param.NOBLE_POINT * Param.POINT_VALUE / totalLacks
        for (i<-values.length) {
          if (lacks(i) > 0) values(i) += cardValue
        }
      }
    }
    values
  }

  def calcNormalCardValue() = {
    state.cards.map(_.map(card => {

    }))
  }

}

