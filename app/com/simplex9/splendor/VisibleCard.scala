package com.simplex9.splendor

/**
  * Created by hongbo on 6/18/17.
  */
class VisibleCard (card: Card,
                   val group: Int,
                  val pos: Int) extends Card(card.color, card.point, card.price) {
  def isReserved = group < 0
  def isInPile = pos < 0
  def reserve(pos: Int) = new VisibleCard(card, -1, pos)
  def getOffset = group * Param.NUM_CARD_EACH_LEVEL + pos
}

object VisibleCard {
  def newCardInPile(card: Card, group: Int) :VisibleCard = new VisibleCard(card, group, -1)
  def newCardInPile(group: Int) : VisibleCard= newCardInPile(Card.secretCard, group)
}
