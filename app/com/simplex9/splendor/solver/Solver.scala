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
    while (searchedLevel == maxLevel) {
      try {
        maxLevel += 1
        val (score, action) = search(state, playerIndex, 0, maxLevel, -Param.INF, Param.INF)
        bestAction = action
        bestScore = score
      } catch {
        case e : LimitReachedException =>
          System.out.println(s"* Level=${maxLevel -1} Score=$bestScore pruned=$pruned")
          for (action <- bestAction) {
            System.out.println("  " + action)
          }
          return bestAction.headOption
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
      val score = StateEvaluator.evaluate(state, playerIndex)
      return (score, Nil)
    }

    var i = 1
    var bestAction : List[Action] = Nil

    if (isMinNode) {
      var minVal = Param.INF
      var beta = _beta
      for (action <- actions) {
        val newState = state.transform(action)
        val (score, nextActions) = search(newState, nextPlayer(currentPlayerIndex), level + 1, maxLevel,
          _alpha, beta)
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
        val (score, nextActions) = search(newState, nextPlayer(currentPlayerIndex), level + 1, maxLevel,
          alpha, _beta)
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
