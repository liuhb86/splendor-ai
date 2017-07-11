import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'
import LittleCardCount from './LittleCardCount.jsx'
import LittleCoins from './LittleCoins.jsx'

export default class LittleCard extends React.Component {
  render() {
    let card = this.props.card
    if (!card) return (<span>???</span>)
    if (card.secret) {
      var cardGroup = "I"
      if (card.group == -2) cardGroup = "II";
      else if (card.group == -3) cardGroup = "III";
      return <span> ?&nbsp;{cardGroup}&nbsp;?</span>
    }
    return(
      <span>
        <LittleCardCount colorClass={card.color} count={card.point} />
        <span>&nbsp;&nbsp;</span>
        <LittleCoins coins={card.price} skipZero={true} />
      </span>
    )
  }
}