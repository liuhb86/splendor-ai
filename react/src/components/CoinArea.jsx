import React from 'react'
import BankCoin from './BankCoin.jsx'
import {sum} from '../util'

export default class CoinArea extends React.Component {
  render () {
    let player = this.props.player
    let total = sum(player.coins) + player.golds
    let canTake = total < 10
      return (
      <div className="coin-area">
        { this.props.coins.map((coin, index) => (
          <BankCoin key={index} index={index} count={coin} 
            canTake={canTake} canReturn={player.coins[index] > 0}/>
        ))}
        <BankCoin key={5} index={5} count={this.props.golds} 
          canTake={canTake} canReturn={player.golds > 0} />
      </div>
    );
  }
}
