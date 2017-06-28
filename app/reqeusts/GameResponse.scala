package reqeusts

import com.simplex9.splendor.Game

/**
  * Created by hongbo on 6/28/17.
  */
case class GameResponse(
                    game : Game,
                    message : String = null)
{
  def serialize(): String = {
    Serializer.toString(this)
  }
}