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
    val playerCardsNum = player.cards.sum
    var gameStage = 0
    for(i <- (0 to Param.GAME_STAGE_CARD_NUM.length-1).reverse){
      if(Param.GAME_STAGE_CARD_NUM(i) <= playerCardsNum){
        gameStage = i
      }
    }
    if (player.points >= Param.WINNING_POINTS) {
      return Param.WINNING_SCORE + player.points * Param.POINT_VALUE - playerCardsNum
    }

    val valueEstimator = new ValueEstimator(state, player)
    val topCards = valueEstimator.cardEstimator.values.take(Param.TOP_CARDS_FOR_STATE_ESTIMATE)
    var topCardsVal = 0
    for (cardInfo <- topCards) {
      if(cardInfo.card.isReserved) {
        topCardsVal += (cardInfo.value * Param.RESERVED_CARD_POINT_RATIO).toInt
      }else{
        topCardsVal += (cardInfo.value * Param.VISIBLE_PUBLIC_CARD_POINT_RATIO).toInt
      }
    }
    var tooFewCardPenalty = 0
   // if(playerCardsNum < Param.PREFERRED_FREE_CARD_NUM){
      //tooFewCardPenalty = Param.TOO_FEW_CARD_PENALTY
      if(player.coins.sum - playerCardsNum >= Param.MAX_ALLOW_COIN_NUM_MORE_THAN_CARD){
        tooFewCardPenalty += (player.coins.sum - playerCardsNum - Param.MAX_ALLOW_COIN_NUM_MORE_THAN_CARD) * Param.COIN_NUM_MORE_THAN_CARD_PENALTY
      }
   // }

    val score = player.points * Param.POINT_VALUE +
      player.coins.sum * Param.COIN_VALUE(gameStage) +
      player.cards.sum * Param.CARD_VALUE(gameStage) +
      player.golds * Param.GOLD_VALUE(gameStage)  +
      topCardsVal - Param.RESERVE_COST(player.reserve.length)  - tooFewCardPenalty
    score
  }

}
