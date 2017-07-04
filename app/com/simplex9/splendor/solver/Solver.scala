package com.simplex9.splendor.solver

import com.simplex9.splendor.actiongenerator.ActionGenerator
import com.simplex9.splendor.valueestimator.StateEvaluator
import com.simplex9.splendor.{Action, Param, State}
import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID

/**
  * Created by hongbo on 6/21/17.
  */
class Solver(state: State, playerIndex: Int, startPlayer: Int) {
  var totalStates = 0
  var pruned = 0

  def solve() : Option[Action] = {
    // TODO: stub
    var bestAction : Option[Action] = None
    var bestScore : Int = -Param.INF
    var maxLevel = 1
    while (true) {
      try {
        val (score, action) = search(state, playerIndex, 0, maxLevel, -Param.INF, Param.INF)
        bestAction = action
        bestScore = score
        maxLevel += 1
      } catch {
        case e : LimitReachedException =>
          System.out.println(s"Level=${maxLevel -1} Score=$bestScore $bestAction")
          return bestAction
      }
    }
    bestAction
  }

  def search(state: State, currentPlayerIndex: Int,
             level: Int, maxLevel: Int,
             _alpha: Int, _beta: Int): (Int, Option[Action]) = {
    totalStates += 1
    if (totalStates > Param.MAX_SEARCH_STATES) throw LimitReachedException()
    val isMinNode = currentPlayerIndex != playerIndex
    var isLeaf = level == maxLevel
    val actions = ActionGenerator.generate(state, playerIndex, startPlayer)
    if (actions == Nil) isLeaf = true

    if (isLeaf) {
      var score = StateEvaluator.evaluate(state, playerIndex)
      if (isMinNode) score = -score
      return (score, None)
    }

    var i = 1
    var bestAction : Action = null

    if (isMinNode) {
      var minVal = Param.INF
      var beta = _beta
      for (action <- actions) {
        val newState = state.transform(action)
        val (score, _a) = search(newState, nextPlayer(currentPlayerIndex), level + 1, maxLevel,
          _alpha, beta)
        if (score < minVal) {
          minVal = score
          bestAction = action
        }
        if (score < beta)  beta = score
        if (beta <= _alpha) {
          pruned += (actions.size - i)
          return (minVal, Option(bestAction))
        }
        i += 1
      }
      (minVal, Option(bestAction))
    } else {
      var maxVal = -Param.INF
      var alpha = _alpha
      for (action <- actions) {
        val newState = state.transform(action)
        val (score, _a) = search(newState, nextPlayer(currentPlayerIndex), level + 1, maxLevel,
          alpha, _beta)
        if (score > maxVal) {
          maxVal = score
          bestAction = action
        }
        if (score > alpha) alpha = score
        if (_beta <= alpha) {
          pruned += (actions.size -i)
          return (maxVal, Option(bestAction))
        }
        i += 1
      }
      (maxVal, Option(bestAction))
    }
  }

  def nextPlayer(currentPlayer: Int) = (currentPlayer + 1) % state.players.length
}

case class LimitReachedException() extends Exception {
}
