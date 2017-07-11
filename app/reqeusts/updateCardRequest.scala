package reqeusts

import com.simplex9.splendor.{Card, Noble}

/**
  * Created by hongbo on 7/10/17.
  */
case class updateCardRequest(
                 cardGroup: Int,
                 cardIndex: Int,
                 card: Card,
                 noble: Noble,
                 playerIndex: Int
                 ) {
}
