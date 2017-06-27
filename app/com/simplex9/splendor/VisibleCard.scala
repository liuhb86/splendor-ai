package com.simplex9.splendor

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonIgnoreProperties}

/**
  * Created by hongbo on 6/18/17.
  */
@JsonIgnoreProperties(Array("group", "pos"))
class VisibleCard (card: Card,
                   val group: Int,
                  val pos: Int) extends Card(card.color, card.point, card.price) {
  @JsonIgnore
  def isReserved = group < 0
  @JsonIgnore
  def isInPile = pos < 0
  def reserve(pos: Int) = new VisibleCard(card, -1, pos)
  @JsonIgnore
  def getOffset = VisibleCard.getOffset(group, pos)
}

object VisibleCard {
  def newCardInPile(card: Card, group: Int) :VisibleCard = new VisibleCard(card, group, -1)
  def newCardInPile(group: Int) : VisibleCard= newCardInPile(Card.secretCard, group)
  def getOffset(group : Int, pos : Int) : Int = group * Param.NUM_CARD_EACH_LEVEL + pos
}
