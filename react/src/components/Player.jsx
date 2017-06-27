import React from 'react'
import {sum} from '../util'
import LittleCardCount from './LittleCardCount.jsx'
import LittleCoins from './LittleCoins.jsx'
import LittleCard from './LittleCard.jsx'

class ReservedCard extends React.Component {
  render() {
    return (
      <div>
        <span>R{this.props.index}&nbsp;</span>
        <LittleCard card={this.props.card} />
      </div>
    )
  }
}

export default class Player extends React.Component {
  render() {
    let player = this.props.player
    let totalCoins = sum(player.coins) + player.golds
    return (
      <div className="player">
        <div> Player&nbsp;{this.props.index}</div>
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
}