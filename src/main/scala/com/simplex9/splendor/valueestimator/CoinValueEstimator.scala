package com.simplex9.splendor.valueestimator

import com.simplex9.splendor.{Color, Param, Player, State}

/**
  * Created by hongbo on 6/18/17.
  */
class CoinValueEstimator(state: State, player: Player, cardEstimator: CardValueEstimator) {
  val values = estimate()

  def estimate() = {
    val values = new Array[Int] (Color.size)
    val topCards = cardEstimator.values.take(Param.TOP_CARDS_FOR_COIN_ESTIMATE)
    for (cardInfo <- topCards) {
      if (cardInfo.lack > 0) {
        val required = cardInfo.lack > player.golds
        for (color <- player.coins.indices) {
          val diff = cardInfo.card.price(color) - player.cards(color) - player.coins(color)
          var value = 0
          if (diff > 0) {
            value = cardInfo.value * diff / cardInfo.lack
            if (!required) value = value / 2
          }
          values(color) += value
        }
      }
    }
    values
  }
}
