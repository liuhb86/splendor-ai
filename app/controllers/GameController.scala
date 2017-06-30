package controllers

import com.simplex9.splendor.Game
import com.simplex9.splendor.solver.Solver
import play.api.mvc.{Action, BodyParsers, Controller}
import reqeusts._

/**
  * Created by hongbo on 6/24/17.
  */
class GameController extends Controller {
  def init() = Action(BodyParsers.parse.tolerantText) { r =>
    val request = Serializer.toObject(r.body, classOf[InitRequest])
    Game.game = new Game(request.players, request.autoMode)
    val response = GameResponse(Game.game).serialize()
    Ok(response)
  }

  def pass() = Action(BodyParsers.parse.tolerantText) { r =>
    Game.game.pass()
    val response = GameResponse(Game.game).serialize()
    Ok(response)
  }

  def action() = Action(BodyParsers.parse.tolerantText) { r =>
    val request = Serializer.toObject(r.body, classOf[ActionRequest])
    val action = ActionRequestConverter.toAction(request, Game.game)
    var msg : String = null
    try {
      Game.game.takeAction(action)
    } catch {
      case e : IllegalAccessException => msg = e.getMessage
    }
    val response = GameResponse(Game.game, msg).serialize()
    Ok(response)
  }

  def solve() = Action(BodyParsers.parse.tolerantText) { r =>
    val request = Serializer.toObject(r.body, classOf[SolveRequest])
    val solver = new Solver(Game.game.state, request.playerIndex, 0)
    val solutions = solver.solve()
    if (solutions.nonEmpty) {
      System.out.println(solutions.take(5))
      val action = solutions.head._1
      Game.game.takeAction(action)
      Ok(GameResponse(Game.game).serialize())
    } else {
      Ok(GameResponse(Game.game, "no valid action").serialize())
    }
  }

}
