package reqeusts

import com.simplex9.splendor._

/**
  * Created by hongbo on 6/25/17.
  */
object ActionRequestConverter {
  def toAction(r: ActionRequest, game : Game) : Action = {
    val playerIndex = game.turn
    val card =
      if (r.cardReserved) {
        Option(game.state.players(playerIndex).reserve(r.cardIndex))
      } else if (r.cardGroup < 0) {
        None
      }
      else if (r.cardIndex < 0) {
        Option(VisibleCard.newCardInPile(r.cardGroup))
      } else {
        Option(game.state.cards(VisibleCard.getOffset(r.cardGroup, r.cardIndex)))
      }
    val noble = if (r.nobleIndex >=0) Option(game.state.nobles(r.nobleIndex)) else None
    Action(playerIndex, Option(r.coins), r.gold, card, r.reserve, noble)
  }
}
