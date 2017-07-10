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

  def checkPlayerStage(player: Player) : Int = {
    val playerCardsNum = player.cards.sum
    for (i <- 1 until Param.GAME_STAGE_CARD_NUM.length) {
      if (playerCardsNum < Param.GAME_STAGE_CARD_NUM(i)) return i - 1
    }
    0
  }

  def evaluatePlayer(player: Player, state: State): Int = {
    val playerCardsNum = player.cards.sum
    if (player.points >= Param.WINNING_POINTS) {
      return Param.WINNING_SCORE + player.points * Param.POINT_VALUE - playerCardsNum
    }
    val gameStage = checkPlayerStage(player)

    val valueEstimator = new ValueEstimator(state, player)
    val topCards = valueEstimator.cardEstimator.values.take(Param.TOP_CARDS_FOR_STATE_ESTIMATE)
    var topCardsVal = 0
    for (cardInfo <- topCards) {
      if(cardInfo.card.isReserved) {
        topCardsVal += cardInfo.value * Param.RESERVED_CARD_POINT_RATIO
      }else{
        topCardsVal += (cardInfo.value * Param.VISIBLE_PUBLIC_CARD_POINT_RATIO).toInt
      }
    }
    val tooFewCardPenalty =
      Math.max(player.coins.sum - playerCardsNum - Param.MAX_ALLOW_COIN_NUM_MORE_THAN_CARD, 0) *
        Param.COIN_NUM_MORE_THAN_CARD_PENALTY

    val score = player.points * Param.POINT_VALUE +
      player.coins.sum * Param.COIN_VALUE(gameStage) +
      player.cards.sum * Param.CARD_VALUE(gameStage) +
      player.golds * Param.GOLD_VALUE(gameStage)  +
      topCardsVal - Param.RESERVE_COST(player.reserve.length)  - tooFewCardPenalty
    score
  }

}
