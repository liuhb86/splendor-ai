package com.simplex9.splendor.actiongenerator

import com.simplex9.splendor.valueestimator.{CoinValueEstimator, ValueEstimator}
import com.simplex9.splendor._

import scala.collection.mutable.ArrayBuffer

/**
  * Created by hongbo on 6/18/17.
  */
class TakeCoinsGenerator(state: State, playerIndex: Int, estimators: Array[ValueEstimator]) {
  val player = state.players(playerIndex)
  val zeroArray =  Seq.fill(Color.size)(0.toShort).toArray
  val current = player.coinCount
  val kinds = state.coins.count(_ > 0)
  val coinValueEstimators = estimators.map(_.coinEstimator)
  val coinValues = state.coins.indices.map(CoinValueEstimator.coinValueForPlayer(_, playerIndex, coinValueEstimators))

  def generate(): List[Action] = {
    val enumerations = takeThree ++ takeTwo  ++ takeOne
    val topActions = enumerations
      .map(c => (c, calculateValue(c)))
      .sortWith(_._2 > _._2)
      .take(Param.TOP_ACTIONS_FOR_TAKE_COIN)
    val result = topActions.map(t => Action(playerIndex, Some(t._1), 0, None, reserve = false, None))
    if (topActions.nonEmpty && topActions.last._2 > 0) result
    else {
      val nop = Action(playerIndex, None, 0, None, reserve = false, None)
      result :+ nop
    }
  }

  def calculateValue(coins : Array[Short]) : Int = {
    var result = 0
    for (color <- coins.indices) {
      result += coinValues(color) * coins(color)
    }
    result
  }

  def takeThree() : List[Array[Short]] = {
    if (current == Param.MAX_COIN) return Nil // if take 3 and return 3, one of them will be the same
    if (kinds < 3) return Nil
    var result : List[Array[Short]] = Nil
    var colors = ArrayBuffer[Int]()
    for (color <- state.coins.indices) {
      if (state.coins(color) > 0) colors += color
    }
    for (color1 <- 0 until colors.size -2) {
      for (color2 <- color1 + 1 until colors.size -1) {
        for (color3 <- color2 + 1 until colors.size) {
          val t = Util.updateArray(zeroArray, colors(color1), 1.toShort)
          t(colors(color2)) = 1
          t(colors(color3)) = 1
          result = t :: result
        }
      }
    }
    if (current == Param.MAX_COIN - 1)
      result.flatMap(returnTwo)
    else if (current == Param.MAX_COIN - 2)
      result.flatMap(returnOne)
    else
      result
  }

  def takeTwo : List[Array[Short]] = {
    val result = takeTwoSame ++ takeTwoDiff
    if (current == Param.MAX_COIN)
      result.flatMap(returnTwo)
    else if (current == Param.MAX_COIN - 1)
      result.flatMap(returnOne)
    else
      result
  }

  def takeTwoDiff : List[Array[Short]] = {
    if (current < Param.MAX_COIN - 2 && kinds != 2) return Nil
    if (kinds < 2) return Nil
    var result : List[Array[Short]] = Nil
    var colors = ArrayBuffer[Int]()
    for (color <- state.coins.indices) {
      if (state.coins(color) > 0) colors += color
    }
    for (color1 <- 0 until colors.size -1) {
      for (color2 <- color1 + 1 until colors.size) {
        val t = Util.updateArray(zeroArray, colors(color1), 1.toShort)
        t(colors(color2)) = 1
        result = t :: result
      }
    }
    result
  }

  def takeTwoSame : List[Array[Short]] = {
    var result : List[Array[Short]] = Nil
    for (color <- state.coins.indices) {
      if (state.coins(color) >= Param.MIN_COIN_FOR_TAKE_TWO)
        result = Util.updateArray(zeroArray, color, 2.toShort) :: result
    }
    result
  }

  def returnTwo(coins: Array[Short]) : List[Array[Short]] = {
    var result : List[Array[Short]] = Nil
    // return two same
    for (color <- coins.indices) {
      if (player.coins(color) >=2 && coins(color) == 0)
        result = Util.updateArray(coins, color, (-2).toShort) :: result
    }
    // return two different
    var colors = ArrayBuffer[Int]()
    for (color <- coins.indices) {
      if (player.coins(color) > 0 && coins(color) == 0) colors += color
    }
    for (color1 <- 0 until colors.size -1) {
      for (color2 <- color1 + 1 until colors.size) {
        val t = Util.updateArray(coins, colors(color1), (-1).toShort)
        t(colors(color2)) = -1
        result = t :: result
      }
    }
    result
  }

  def takeOne : List[Array[Short]] = {
    if (current < Param.MAX_COIN - 1 && kinds != 1) return Nil
    if (kinds < 1) return Nil
    var result : List[Array[Short]] = Nil
    for (color <- state.coins.indices) {
      if (state.coins(color) > 0)
        result = Util.updateArray(zeroArray, color, 1.toShort) :: result
    }
    if (current == Param.MAX_COIN) result.flatMap(returnOne) else result
  }

  def returnOne(coins : Array[Short]) : List[Array[Short]] = {
    var result : List[Array[Short]] = Nil
    for (color <- coins.indices) {
      if (player.coins(color) > 0 && coins(color) == 0)
        result = Util.updateArray(coins, color, (-1).toShort) :: result
    }
    result
  }
}
