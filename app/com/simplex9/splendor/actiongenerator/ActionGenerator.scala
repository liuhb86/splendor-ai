package com.simplex9.splendor.actiongenerator

import com.simplex9.splendor._
import com.simplex9.splendor.valueestimator.ValueEstimator

import scala.collection.mutable.ArrayBuffer

/**
  * Created by hongbo on 6/17/17.
  */
object ActionGenerator {

  def generate(state: State, playerIndex: Int, startPlayer: Int) : List[Action] = {
    if (state.players.exists(_.points >= Param.WINNING_POINTS) && playerIndex == startPlayer) {
      return Nil
    }
    val estimators = state.players.map(new ValueEstimator(state, _))
    val buyCardActions = new BuyCardGenerator(state, playerIndex, estimators(playerIndex)).generate()
    val reserveActions = new ReserveCardGenerator(state, playerIndex, estimators).generate()
    val takeCoinsActions =  new TakeCoinsGenerator(state, playerIndex, estimators).generate()
    val actions =
      if (state.players(playerIndex).coinCount <= Param.MAX_COIN - 3) {
        buyCardActions ++ takeCoinsActions ++ reserveActions
      } else {
        buyCardActions ++ reserveActions ++ takeCoinsActions
      }

    checkFreeNoble(state, playerIndex, estimators, actions)
  }

  def checkFreeNoble(state: State, playerIndex: Int, estimators: Array[ValueEstimator], actions: List[Action]): List[Action] = {
    val freeNobles = ArrayBuffer[Int]()
    for (i<- state.nobles.indices) {
      if (estimators(playerIndex).nobleEstimator.satisfy(i)) freeNobles += i
    }
    if (freeNobles.isEmpty) return actions

    var inDanger = false
    var noble = Some(state.nobles(freeNobles.head))
    for (nobleIndex <- freeNobles) {
      for (i <- estimators.indices) {
        if (i != playerIndex) {
          val estimator = estimators(i).nobleEstimator
          if (estimator.satisfy(nobleIndex) || estimator.lastColor(nobleIndex).isDefined) {
            inDanger = true
            noble = Some(state.nobles(nobleIndex))
          }
        }
      }
    }

    actions.map(action =>
      if (action.noble.isDefined && !inDanger) action
      else Action(action.playerIndex, action.coins, action.gold, action.card, action.reserve, noble)
    )
  }


}
