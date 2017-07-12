package com.simplex9.splendor

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonIgnoreProperties}

/**
  * Created by hongbo on 6/18/17.
  */
@JsonIgnoreProperties(Array("pos"))
class VisibleCard (card: Card,
                  val pos: Int,
                  val reservedBy: Int) extends Card(card.level, card.color, card.point, card.price) {
  @JsonIgnore
  def isReserved = reservedBy >= 0
  @JsonIgnore
  def isInPile = pos < 0
  def reserve(playerIndex: Int, pos: Int) = new VisibleCard(card, pos, playerIndex)
  @JsonIgnore
  def getOffset = VisibleCard.getOffset(level, pos)
}

object VisibleCard {
  def newCardInPile(card: Card) :VisibleCard = new VisibleCard(card, -1, -1)
  def newCardInPile(group: Int) : VisibleCard= newCardInPile(Card(group, null, -1, null))
  def getOffset(group : Int, pos : Int) : Int = group * Param.NUM_CARD_EACH_LEVEL + pos
}
