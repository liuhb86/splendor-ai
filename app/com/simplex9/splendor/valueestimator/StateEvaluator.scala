package com.simplex9.splendor.valueestimator

import com.simplex9.splendor.{Param, Player, State}

/**
  * Created by hongbo on 7/3/17.
  */
object StateEvaluator {
  def evaluate(state : State, iPlayer : Int): Int = {
    var maxOpponent = -Param.INF
    for (i <- state.players.indices) {
      if (i != iPlayer) {
        val score = evaluatePlayer(state.players(i), state)
        if (score > maxOpponent) maxOpponent = score
      }
    }
    evaluatePlayer(state.players(iPlayer), state) - maxOpponent
  }

  def evaluatePlayer(player: Player, state: State): Int = {
    if (player.points >= Param.WINNING_POINTS) {
      return Param.WINNING_SCORE + player.points * Param.POINT_VALUE - player.cards.sum
    }

    val valueEstimator = new ValueEstimator(state, player)
    player.points * Param.POINT_VALUE +
      player.coins.sum * Param.COIN_VALUE +
      player.cards.sum * Param.CARD_VALUE +
      player.golds * Param.GOLD_VALUE +
      valueEstimator.cardEstimator.values.head.value
  }

}
