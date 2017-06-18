package com.simplex9.splendor.actiongenerator

import com.simplex9.splendor._

/**
  * Created by hongbo on 6/17/17.
  */
object BuyCardGenerator {

  def generate(state:State, playerIndex: Int) : List[Action] = {
    val player = state.players(playerIndex)
    var result : List[Action] = Nil

    for (cards <- state.cards) {
      for (card <- cards) {
        val action = checkCard(card, player, playerIndex, state)
        if (action.isDefined) result = action.get :: result
      }
    }

    for (card <- player.reserve) {
      val action = checkCard(card, player, playerIndex, state)
      if (action.isDefined) result = action.get :: result
    }
    result
  }

  def checkNobles(nobles : Array[Noble], cards : Array[Byte]) : Option[Noble] = {
    for (noble <- nobles) {
      if (checkNoble(noble, cards)) return Some(noble)
    }
    None
  }

  def checkNoble(noble: Noble, cards : Array[Byte]) : Boolean = {
    for (color <- noble.cards.indices) {
      if (noble.cards(color) > cards(color)) return false
    }
    true
  }

  def checkCard(card: VisibleCard, player: Player, playerIndex: Int, state: State) : Option[Action] = {
    if (card == null) return None   // an empty space or a secret reserve card

    var lack = 0
    for (color <- card.price.indices) {
      val diff = card.price(color) - player.cards(color) - player.coins(color)
      if (diff > 0) {
        lack += diff
      }
    }

    // cannot afford
    if (lack >= player.golds) return None

    val coins = new Array[Byte](card.price.length)
    for (color <- card.price.indices) {
      coins(color) =
        (-Math.min(player.coins(color), Math.max(card.price(color) - player.cards(color), 0))).toByte
    }
    val newCards = Util.updateArray(player.cards, card.color.id, (c:Byte) => (c+1).toByte)
    val noble = checkNobles(state.nobles, newCards)
    Some(Action(playerIndex, Some(coins), (-lack).toByte, Some(card), reserve = false, noble))
  }
}
