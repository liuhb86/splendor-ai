package com.simplex9.splendor.valueestimator

import com.simplex9.splendor.valueestimator.CardValueEstimator.CardValue
import com.simplex9.splendor.{Param, Player, State, VisibleCard}

/**
  * Created by hongbo on 6/18/17.
  */
class CardValueEstimator(
  state : State,
  player: Player,
  nobleValueEstimator: NobleValueEstimator
) {
  val values = estimate()

  def estimate() = {
    val cards = (state.cards ++ player.reserve).filter(c => c != null && !c.isSecret)
    cards.map(card => {
      var lack = 0
      for (color <- card.price.indices) {
        val diff = card.price(color) - player.cards(color) - player.coins(color)
        if (diff > 0) {
          lack += diff
        }
      }
      val diff = if (lack > player.golds) lack - player.golds else 0
      val fullCardValue = card.point * Param.POINT_VALUE + nobleValueEstimator.values(card.color.id)
      val valueRates = if (card.isReserved) Param.RESERVE_VALUE_RATE else Param.VALUE_RATE
      val cardValue = if (diff < valueRates.length) (fullCardValue * valueRates(diff)).toInt else 0
      CardValue(card, cardValue, lack)
    }).sortWith(_.value > _.value)
  }
}

object CardValueEstimator {
  case class CardValue(card: VisibleCard, value: Int, lack: Int)
}
