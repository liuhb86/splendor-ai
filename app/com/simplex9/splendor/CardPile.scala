package com.simplex9.splendor

import scala.util.Random

/**
  * Created by hongbo on 6/24/17.
  */
class CardPile(cards : Array[Array[Card]]) {
  def getSize = cards.map(_.length)
  def take(group: Int, index: Int) : (VisibleCard, CardPile) = {
    if (cards(group).isEmpty) return (null, this)
    val card = new VisibleCard(cards(group).last, group, index)
    val newCards = Util.updateArray(cards, group, (t : Array[Card])=> Util.deleteLastFromArray(t))
    (card, new CardPile(newCards))
  }
}

object CardPile {
  def shuffle() = {
    val cards = Deck.cards.map(x => Random.shuffle(x.toSeq).toArray)
    new CardPile(cards)
  }
}