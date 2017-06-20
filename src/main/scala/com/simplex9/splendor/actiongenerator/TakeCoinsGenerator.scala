package com.simplex9.splendor.actiongenerator

import com.simplex9.splendor.valueestimator.{CoinValueEstimator, ValueEstimator}
import com.simplex9.splendor._

import scala.collection.mutable.ArrayBuffer

/**
  * Created by hongbo on 6/18/17.
  */
class TakeCoinsGenerator(state: State, playerIndex: Int, estimators: Array[ValueEstimator]) {
  val player = state.players(playerIndex)
  val zeroArray =  Seq.fill(Color.size)(0.toByte).toArray
  val current = player.coins.sum
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
    if (topActions.last._2 > 0) result
    else {
      val nop = Action(playerIndex, None, 0, None, reserve = false, None)
      nop :: result
    }
  }

  def calculateValue(coins : Array[Byte]) : Int = {
    var result = 0
    for (color <- coins.indices) {
      result += coinValues(color) * coins(color)
    }
    result
  }

  def takeThree() : List[Array[Byte]] = {
    if (kinds < 3) return Nil
    var result : List[Array[Byte]] = Nil
    var colors = ArrayBuffer[Int]()
    for (color <- state.coins.indices) {
      if (state.coins(color) > 0) colors += color
    }
    for (color1 <- 0 until colors.size -2) {
      for (color2 <- color1 until colors.size -1) {
        for (color3 <- color2 until colors.size) {
          val t = Util.updateArray(zeroArray, colors(color1), 1.toByte)
          t(colors(color2)) = 1
          t(colors(color3)) = 1
          result = t :: result
        }
      }
    }
    result
  }

  def takeTwo : List[Array[Byte]] = {
    val result = takeTwoSame ++ takeTwoDiff
    if (current == Param.MAX_COIN)
      result.flatMap(returnTwo)
    else if (current == Param.MAX_COIN - 1)
      result.flatMap(returnOne)
    else
      result
  }

  def takeTwoDiff : List[Array[Byte]] = {
    if (current < Param.MAX_COIN - 2 && kinds != 2) return Nil
    if (kinds < 2) return Nil
    var result : List[Array[Byte]] = Nil
    var colors = ArrayBuffer[Int]()
    for (color <- state.coins.indices) {
      if (state.coins(color) > 0) colors += color
    }
    for (color1 <- 0 until colors.size -1) {
      for (color2 <- color1 until colors.size) {
        val t = Util.updateArray(zeroArray, colors(color1), 1.toByte)
        t(colors(color2)) = 1
        result = t :: result
      }
    }
    result
  }

  def takeTwoSame : List[Array[Byte]] = {
    var result : List[Array[Byte]] = Nil
    for (color <- state.coins.indices) {
      if (state.coins(color) >= Param.MIN_COIN_FOR_TAKE_TWO)
        result = Util.updateArray(zeroArray, color, 2.toByte) :: result
    }
    result
  }

  def returnTwo(coins: Array[Byte]) : List[Array[Byte]] = {
    var result : List[Array[Byte]] = Nil
    // return two same
    for (color <- coins.indices) {
      if (player.coins(color) >=2 && coins(color) == 0)
        result = Util.updateArray(coins, color, (-2).toByte) :: result
    }
    // return two different
    var colors = ArrayBuffer[Int]()
    for (color <- coins.indices) {
      if (player.coins(color) > 0 && coins(color) == 0) colors += color
    }
    for (color1 <- 0 until colors.size -1) {
      for (color2 <- color1 until colors.size) {
        val t = Util.updateArray(coins, colors(color1), (-1).toByte)
        t(colors(color2)) = -1
        result = t :: result
      }
    }
    result
  }

  def takeOne : List[Array[Byte]] = {
    if (current < Param.MAX_COIN - 1 && kinds != 1) return Nil
    if (kinds < 1) return Nil
    var result : List[Array[Byte]] = Nil
    for (color <- state.coins.indices) {
      if (state.coins(color) > 0)
        result = Util.updateArray(zeroArray, color, 1.toByte) :: result
    }
    if (current == Param.MAX_COIN) result.flatMap(returnOne) else result
  }

  def returnOne(coins : Array[Byte]) : List[Array[Byte]] = {
    var result : List[Array[Byte]] = Nil
    for (color <- coins.indices) {
      if (player.coins(color) > 0 && coins(color) == 0)
        result = Util.updateArray(coins, color, (-1).toByte) :: result
    }
    result
  }
}
