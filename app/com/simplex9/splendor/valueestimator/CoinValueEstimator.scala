package com.simplex9.splendor.valueestimator

import com.simplex9.splendor.{Color, Param, Player, State}

/**
  * Created by hongbo on 6/18/17.
  */
class CoinValueEstimator(state: State, player: Player, cardEstimator: CardValueEstimator) {
  val values = estimate()

  def estimate() = {
    var curPlayerID = 0
    for (i <- state.players.indices) {
      if (state.players(i) == player) {
        curPlayerID = i;
      }
    }
    val nextPlayerID = (curPlayerID + 1) % state.players.length
    val curPlayerDominateColorsInReservedCard = state.players(curPlayerID).getDominateReserveColor
    val nextPlayerDominateColorsInReservedCard = state.players(nextPlayerID).getDominateReserveColor

    val values = new Array[Int] (Color.size)
    val topCards = cardEstimator.values.take(Param.TOP_CARDS_FOR_COIN_ESTIMATE)
    for (cardInfo <- topCards) {
      if (cardInfo.lack > 0) {
        val required = cardInfo.lack > player.golds
        for (color <- player.coins.indices) {
          var diff = cardInfo.card.price(color) - player.cards(color) - player.coins(color)
          var value = 0
          if (diff > 0) {
            if(cardInfo.card.isReserved && curPlayerDominateColorsInReservedCard(color) > 0){
              //reserved by me and is dominate color
              diff = (diff * Param.SELF_DOMINATE_COIN_VALUE_RATE).toInt
            }
            value = cardInfo.value * diff / cardInfo.lack
            if (!required) value = value / 2
          }
          values(color) += value
        }
      }
    }
    for (color <- nextPlayerDominateColorsInReservedCard.indices) {
      if(nextPlayerDominateColorsInReservedCard(color) > 0) {
        values(color) = (values(color) * Param.NEXT_PLAYER_DOMINATE_COIN_VALUE_RATE).toInt
      }
    }

    values
  }
}

object CoinValueEstimator {
  def coinValueForPlayer(colorIndex : Int, playerIndex: Int,
                         coinValueEstimators : Array[CoinValueEstimator]) : Int = {
    var maxValue : Double = coinValueEstimators(playerIndex).values(colorIndex)
    for (i <- coinValueEstimators.indices) {
      if (i != playerIndex) {
        val value = coinValueEstimators(i).values(colorIndex) * Param.OPPONENT_VALUE_RATE
        if (value > maxValue) maxValue = value
      }
    }
    maxValue.toInt
  }
}