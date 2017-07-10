package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
object Param {
  val WINNING_POINTS : Short = 15
  val NOBLE_POINT : Short = 3
  val NUM_COIN = Array[Short](0,0,4,5,7)
  val NUM_GOLD : Short = 5
  val NUM_CARD_LEVEL = 3
  val NUM_CARD_EACH_LEVEL = 4
  val MAX_COIN = 10
  val MAX_RESERVE = 3
  val MIN_COIN_FOR_TAKE_TWO = 4

  val INF = 1000000

  val WINNING_SCORE = 100000
  val POINT_VALUE = 200
  val COIN_VALUE = Array[Short](25,35,60)
  val GOLD_VALUE = Array[Short](40,60,100)
  val CARD_VALUE = Array[Short](100,70,60)
  val GAME_STAGE_CARD_NUM = Array[Short](0,8,12)
  val MIN_DEFENSE_GAME_STAGE = 1
  val MAX_DOMINATE_COLOR_LACK_FOR_RESERVE = 3
  val MAX_ALLOW_COIN_NUM_MORE_THAN_CARD = 4
  val COIN_NUM_MORE_THAN_CARD_PENALTY = 85//so that we will not favor get more coins,

  val MAX_SAFE_RESERVE = 2
  val MAX_SAFE_COIN_LACK_FOR_RESERVE = Array[Short](3,4,2)
  val RESERVE_COST = Array[Short](0,200,500,800)
  val MIN_SAFE_CARD_NUM_OVER_SAFE_RESERVED = 10

  val TOP_CARDS_FOR_COIN_ESTIMATE = 3
  val TOP_CARDS_FOR_STATE_ESTIMATE = 3
  val TOP_CARDS_FOR_RESERVE = 6
  val TOP_ACTIONS_FOR_TAKE_COIN = 6
  val OPPONENT_VALUE_RATE = 0.7
  val VALUE_RATE = Array(.7, .5, .3, .2, .1)
  val RESERVE_VALUE_RATE = Array(.9, .9, .6, .5, .3, .1)

  val MAX_SEARCH_STATES = 100000
  var VISIBLE_PUBLIC_CARD_POINT_RATIO = 0.5
  var RESERVED_CARD_POINT_RATIO = 1

}
