import React from 'react'
import {sum, canAfford} from '../util'
import classNames from 'classnames'
import LittleCardCounts from './LittleCardCounts.jsx'
import LittleCoins from './LittleCoins.jsx'
import LittleCard from './LittleCard.jsx'
import * as Request from '../request'
import settings from '../settings'

class ReservedCard extends React.Component {
  render() {
    return (
      <div>
        <span><span className="icon-lock"/>{this.props.index}&nbsp;</span>
        { (this.props.card) ?
          (<LittleCard card={this.props.card} />) :
          (<div>???</div>)
        }
        {
          canAfford(this.props.card, this.props.player) &&
          <button onClick={this.buy}title="Buy">
            <span className="icon-cart" />
          </button>
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
        <div> <span className="icon-user"/>{settings.playerNames[this.props.index]}
          { this.props.active &&
            <span>&nbsp;&nbsp;
              <button onClick={this.pass} title="Pass">
                <span className="icon-arrow-down" />
              </button>
              <button onClick={this.solve} title="Solve">
                <span className="icon-android" />
              </button>
            </span>
          }
        </div>
        <div>
          <b><span className="icon-star-empty" />{player.points}&nbsp;</b>
          <LittleCardCounts cards={player.cards} />
        </div>
        <div>
          <span><span className="icon-coin-euro"/>{totalCoins}&nbsp;</span>
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

  solve = (e) => {
    Request.send("solve", {playerIndex : this.props.index})
  }
}