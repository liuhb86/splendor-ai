import React from 'react'
import {sum, canAfford} from '../util'
import classNames from 'classnames'
import LittleCardCount from './LittleCardCount.jsx'
import LittleCoins from './LittleCoins.jsx'
import LittleCard from './LittleCard.jsx'
import * as Request from '../request'

class ReservedCard extends React.Component {
  render() {
    return (
      <div>
        <span>R{this.props.index}&nbsp;</span>
        { (this.props.card) ?
          (<LittleCard card={this.props.card} />) :
          (<div>???</div>)
        }
        {
          canAfford(this.props.card, this.props.player) &&
          <button onClick={this.buy}>Buy</button>
        }
      </div>
    )
  }

  buy = (e) => {
    Request.sendBuyAction(this.props.card, -1, this.props.index, this.props.player)
  }
}

export default class Player extends React.Component {

  render() {
    let player = this.props.player
    let totalCoins = sum(player.coins) + player.golds
    return (
      <div className={classNames("player", {"active-player" : this.props.active})} >
        <div> Player&nbsp;{this.props.index} 
          { this.props.active &&
            <span>&nbsp;&nbsp;
              <button onClick={this.pass}>Pass</button>
            </span>
          }
        </div>
        <div>
          <b>VP:{player.points}&nbsp;</b>
          <span>
            {
              player.cards.map((count, index) =>(
                <LittleCardCount key={index} color={index} count={count} />
              ))
            }
          </span>
        </div>
        <div>
          <span>#C:{totalCoins}&nbsp;</span>
          <LittleCoins coins={player.coins} golds={player.golds} />
        </div>
        <div>
          {
            player.reserve.map((reserveCard, index) => (
                <ReservedCard key={index} index={index} 
                  card={reserveCard} player={this.props.player} />
            ))
          }
        </div>
      </div>
    )
  }

  pass = (e) => {
    Request.send("pass",{})
  }
}