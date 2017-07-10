package com.simplex9.splendor.actiongenerator

import com.simplex9.splendor.valueestimator.{CoinValueEstimator, StateEvaluator, ValueEstimator}
import com.simplex9.splendor.{Action, Param, State, VisibleCard}

import scala.collection.mutable

/**
  * Created by hongbo on 6/18/17.
  */
class ReserveCardGenerator(state: State, playerIndex: Int, estimators: Array[ValueEstimator]) {
  def generate(): List[Action] = {
    val player = state.players(playerIndex)
    if (player.reserve.length >= Param.MAX_RESERVE) return Nil
    val gameStage = StateEvaluator.checkPlayerStage(player)

    val gold = if (state.golds > 0) 1 else 0
    //don't reserve too many card if we don't have enough card yet, avoid deadlock
    if(player.reserve.length >= Param.MAX_SAFE_RESERVE
      && player.cards.sum <= Param.MIN_SAFE_CARD_NUM_OVER_SAFE_RESERVED) return Nil

    val topCards = mutable.HashMap[VisibleCard, Int]()
    for (i <- state.players.indices) {
      val cardValues = estimators(i).cardEstimator.values
      val iter = cardValues.iterator
      var count = 0
      while (count < Param.TOP_CARDS_FOR_RESERVE && iter.hasNext) {
        val cardInfo = iter.next()
        if (!cardInfo.card.isReserved) {
          val value =
            if (i == playerIndex) cardInfo.value
            else  (cardInfo.value * Param.OPPONENT_VALUE_RATE).toInt
          val dominateColorsInReservedCard = player.getDominateReserveColor
          var dominateColor = 0
          var dominateColorCoin = 0
          var maxLack = -1
          var lack = 0
          for (k <- cardInfo.card.price.indices) {
            val diff = cardInfo.card.price(k) - player.cards(k) - player.coins(k)
            if(diff >= Param.MAX_DOMINATE_COLOR_LACK_FOR_RESERVE && diff > maxLack){
              maxLack = diff
              dominateColor = k
              dominateColorCoin =cardInfo.card.price(k)
            }
            if(cardInfo.card.price(k) - player.cards(k) > 0) {
              lack += cardInfo.card.price(k) - player.cards(k)
            }
          }
          val dareToReserveOpponentCard = gold > 0 && (i != playerIndex && gameStage >= Param.MIN_DEFENSE_GAME_STAGE)
          var dareToReserveSelfCard = gold > 0 && (i == playerIndex && cardInfo.lack <= Param.MAX_SAFE_COIN_LACK_FOR_RESERVE(gameStage))
          if(dareToReserveSelfCard && dominateColor >= 0 && dominateColorsInReservedCard(dominateColor) > 0){
            dareToReserveSelfCard = false
          }
          val dareToReserve = dareToReserveSelfCard || dareToReserveOpponentCard
          val prevValue = topCards.get(cardInfo.card)
          if (dareToReserve && (prevValue.isEmpty || prevValue.get < value)) {
            topCards.put(cardInfo.card, value)
          }
          count += 1
        }
      }
    }

    val coins =
      if (player.coinCount + gold <= Param.MAX_COIN) None
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

    topCards.toList.sortWith(_._2> _._2).take(Param.TOP_CARDS_FOR_RESERVE).map(cardInfo =>
      Action(playerIndex, coins, gold.toShort, Some(cardInfo._1), reserve = true, None)
    )
  }

}
