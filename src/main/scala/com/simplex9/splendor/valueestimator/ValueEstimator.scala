package com.simplex9.splendor.valueestimator

import com.simplex9.splendor.{Player, State}

/**
  * Created by hongbo on 6/18/17.
  */
class ValueEstimator(state : State, player: Player) {
  val nobleEstimator = new NobleValueEstimator(state, player)
  val cardEstimator = new CardValueEstimator(state, player, nobleEstimator)
  val coinEstimator = new CoinValueEstimator(state, player, cardEstimator)
}

