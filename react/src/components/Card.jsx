import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'

export default class Card extends React.Component {
  render () {
    let card = this.props.card
    let colorClass = card.color.value
    let coins = []
    for (var i in card.price) {
        if (card.price[i] > 0) {
            let coinColorClass = toColorClass(i)
            coins.push(
                <div key={i} className={classNames("card-coin","price",coinColorClass)}>
                   {card.price[i]}
                </div>
            )
        }
    }
    
    return (
      <div className="card-wrapper">
        <div className={classNames("card", colorClass)}>  
            <div className="card-coins">
                {coins}
            </div>
            <div className="points">{card.point}</div>
        </div>    
      </div>
    );
  }
}
