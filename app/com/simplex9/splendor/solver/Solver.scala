package com.simplex9.splendor.solver

import com.simplex9.splendor.actiongenerator.ActionGenerator
import com.simplex9.splendor.valueestimator.StateEvaluator
import com.simplex9.splendor.{Action, Param, State}

/**
  * Created by hongbo on 6/21/17.
  */
class Solver(state: State, playerIndex: Int, startPlayer: Int) {
  var totalStates = 0
  var pruned = 0
  var searchedLevel = 0

  def solve() : Option[Action] = {
    var bestAction : List[Action] = Nil
    var bestScore : Int = -Param.INF
    var maxLevel = 0
    var bestLevel = 0
    while (searchedLevel == maxLevel) {
      try {
        maxLevel += 1
        val (score, action) = search(state, playerIndex, 0, maxLevel, -Param.INF, Param.INF)
      //  if(score > bestScore) {
          bestAction = action
          bestScore = score
          bestLevel = searchedLevel
      //  }
      } catch {
        case e : LimitReachedException =>
          System.out.println(s"* Max Level=${maxLevel -1} Score=$bestScore pruned=$pruned")
          for (action <- bestAction) {
            System.out.println("  " + action)
          }
          return bestAction.headOption
        case e: Exception => System.out.print(e.getStackTrace.mkString("\n"))
      }
    }
    bestAction.headOption
  }

  def search(state: State, currentPlayerIndex: Int,
             level: Int, maxLevel: Int,
             _alpha: Int, _beta: Int): (Int, List[Action]) = {
    if (level > searchedLevel) searchedLevel = level
    totalStates += 1
    if (totalStates > Param.MAX_SEARCH_STATES) throw LimitReachedException()
    val isMinNode = currentPlayerIndex != playerIndex
    var isLeaf = level == maxLevel
    val actions = ActionGenerator.generate(state, currentPlayerIndex, startPlayer)
    if (actions == Nil) isLeaf = true

    if (isLeaf) {
      var score = StateEvaluator.evaluate(state, playerIndex)
      //if (isMinNode && score > 0) score = -score
      return (score, Nil)
    }

    var i = 1
    var bestAction : List[Action] = Nil

    if (isMinNode) {
      var minVal = Param.INF
      var beta = _beta
      for (action <- actions) {
        val newState = state.transform(action)
        var (score, nextActions) = search(newState, nextPlayer(currentPlayerIndex), level + 1, maxLevel,
          _alpha, beta)
        if(action.coins.isDefined && !action.card.isDefined){
          val coinNum = action.coins.get.sum
          if(coinNum >= 0) {
            //get less coin from bank, punish it
            //we punish reserve in the state evaluator
            score += Param.TAKE_COIN_PENALTY(coinNum)
          }
        }
        if (!action.noble.isDefined && !action.coins.isDefined && !action.reserve) {
          score += Param.NON_OP_PUNISH_SCORE
        }
        if (score < minVal) {
          minVal = score
          bestAction = action :: nextActions
        }
        if (score < beta)  beta = score
        if (beta <= _alpha) {
          pruned += (actions.size - i)
          return (minVal, bestAction)
        }
        i += 1
      }
      (minVal, bestAction)
    } else {
      var maxVal = -Param.INF
      var alpha = _alpha
      for (action <- actions) {
        val newState = state.transform(action)
        var (score, nextActions) = search(newState, nextPlayer(currentPlayerIndex), level + 1, maxLevel,
          alpha, _beta)
        if(action.coins.isDefined && !action.card.isDefined){
          val coinNum = action.coins.get.sum
          if(coinNum >= 0) {
            score -= Param.TAKE_COIN_PENALTY(coinNum)
          }
        }
        if (!action.noble.isDefined && !action.coins.isDefined && !action.reserve) {
          score -= Param.NON_OP_PUNISH_SCORE
        }
        if (score > maxVal) {
          maxVal = score
          bestAction = action :: nextActions
        }
        if (score > alpha) alpha = score
        if (_beta <= alpha) {
          pruned += (actions.size -i)
          return (maxVal, bestAction)
        }
        i += 1
      }
      (maxVal, bestAction)
    }
  }

  def nextPlayer(currentPlayer: Int) = (currentPlayer + 1) % state.players.length
}

case class LimitReachedException() extends Exception {
}
