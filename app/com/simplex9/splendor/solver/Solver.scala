package com.simplex9.splendor.solver

import com.simplex9.splendor.actiongenerator.ActionGenerator
import com.simplex9.splendor.{Action, State}

/**
  * Created by hongbo on 6/21/17.
  */
class Solver(state: State, playerIndex: Int, startPlayer: Int) {
  def solve() : List[(Action, Int)] = {
    // TODO: stub
    ActionGenerator.generate(state, playerIndex, startPlayer)
      .map(x => (x, state.transform(x).calcScore(playerIndex)))
      .sortWith(_._2 > _._2)
  }
}
