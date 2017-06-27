import React from 'react'
import BankCoin from './BankCoin.jsx'

export default class CoinArea extends React.Component {
  render () {
      return (
      <div className="coin-area">
        { this.props.coins.map((coin, index) => (
                <BankCoin key={index} index={index} count={coin} />
        ))}
        <BankCoin key={5} index={5} count={this.props.golds} />
      </div>
    );
  }
}
