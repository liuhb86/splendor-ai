package com.simplex9.splendor

/**
  * Created by hongbo on 6/10/17.
  */
object Color extends Enumeration{
  type Color = Value
  val WHITE, BLUE, GREEN, RED, ONYX = Value
  def size = values.size
}
