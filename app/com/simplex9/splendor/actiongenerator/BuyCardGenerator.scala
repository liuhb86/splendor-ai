package com.simplex9.splendor.actiongenerator

import com.simplex9.splendor._
import com.simplex9.splendor.valueestimator.{CardValueEstimator, ValueEstimator}

/**
  * Created by hongbo on 6/17/17.
  */
class BuyCardGenerator(state : State, playerIndex: Int, estimator: ValueEstimator) {
  val player = state.players(playerIndex)

  def generate() : List[Action] = {
    estimator.cardEstimator.values.flatMap(checkCard(_)).toList
  }

  def checkNobles(nobles : Array[Noble], color : Color.Color) : Option[Noble] = {
    val lastColor = estimator.nobleEstimator.lastColor
    for (i <- nobles.indices) {
      if (lastColor(i).isDefined && lastColor(i).get == color) return Some(nobles(i))
    }
    None
  }

  def checkCard(cardValue : CardValueEstimator.CardValue) : Option[Action] = {
    val card = cardValue.card
    if (card == null || card.isSecret) return None // an empty space or a secret reserve card

    // cannot afford
    if (cardValue.lack >= player.golds) return None

    val coins = new Array[Byte](card.price.length)
    for (color <- card.price.indices) {
      coins(color) =
        (-Math.min(player.coins(color), Math.max(card.price(color) - player.cards(color), 0))).toByte
    }
    val noble = checkNobles(state.nobles, card.color)
    Some(Action(playerIndex, Some(coins), (-cardValue.lack).toByte, Some(card), reserve = false, noble))
  }
}
