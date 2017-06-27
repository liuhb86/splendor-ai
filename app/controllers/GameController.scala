package controllers

import com.simplex9.splendor.Game
import play.api.mvc.{Action, BodyParsers, Controller}
import reqeusts.{ActionRequest, ActionRequestConverter, InitRequest, Serializer}

/**
  * Created by hongbo on 6/24/17.
  */
class GameController extends Controller {
  def init() = Action(BodyParsers.parse.tolerantText) { r =>
    val request = Serializer.toObject(r.body, classOf[InitRequest])
    Game.game = new Game(request.players, request.autoMode)
    val response = Serializer.toString(Game.game)
    Ok(response)
  }

  def pass() = Action(BodyParsers.parse.tolerantText) { r =>
    Game.game.pass()
    val response = Serializer.toString(Game.game)
    Ok(response)
  }

  def action() = Action(BodyParsers.parse.tolerantText) { r =>
    val request = Serializer.toObject(r.body, classOf[ActionRequest])
    val action = ActionRequestConverter.toAction(request, Game.game)
    Game.game.takeAction(action)
    val response = Serializer.toString(Game.game)
    Ok(response)
  }
}
