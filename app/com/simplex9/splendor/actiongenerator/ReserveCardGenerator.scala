package com.simplex9.splendor.actiongenerator

import com.simplex9.splendor.valueestimator.{CardValueEstimator, CoinValueEstimator, ValueEstimator}
import com.simplex9.splendor.{Action, Param, State}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by hongbo on 6/18/17.
  */
class ReserveCardGenerator(state: State, playerIndex: Int, estimators: Array[ValueEstimator]) {
  def generate(): List[Action] = {
    val player = state.players(playerIndex)
    if (player.reserve.length >= Param.MAX_RESERVE) return Nil
    var topCards = ArrayBuffer[CardValueEstimator.CardValue]()
    for (i <- state.players.indices) {
      val cardValues = estimators(i).cardEstimator.values
      if (i == playerIndex) {
        topCards ++= cardValues.take(Param.TOP_CARDS_FOR_RESERVE)
      } else {
        val iter = cardValues.iterator
        var count = 0
        while (count < Param.TOP_CARDS_FOR_RESERVE && iter.hasNext) {
          val cardInfo = iter.next()
          if (!cardInfo.card.isReserved) {
            topCards += CardValueEstimator.CardValue(
              cardInfo.card,
              (cardInfo.value * Param.OPPONENT_VALUE_RATE).toInt,
              cardInfo.lack)
            count += 1
          }
        }
      }
    }

    val gold = if (state.golds > 0) 1 else 0
    val coins =
      if (player.coins.sum + gold <= Param.MAX_COIN) None
      else {
        var dropColor = 0
        var leastValue = Param.INF
        val coinValueEstimators = estimators.map(_.coinEstimator)
        for (color <- player.coins.indices) {
          if (player.coins(color) > 0) {
            val value = CoinValueEstimator.coinValueForPlayer(color, playerIndex, coinValueEstimators)
            if (value < leastValue) {
              leastValue = value
              dropColor = color
            }
          }
        }
        val coins = new Array[Short](player.coins.length)
        coins(dropColor) = -1
        Some(coins)
      }

    topCards.sortWith(_.value > _.value).take(Param.TOP_CARDS_FOR_RESERVE).map(cardInfo =>
      Action(playerIndex, coins, gold.toShort, Some(cardInfo.card), reserve = true, None)
    ).toList
  }

}
