import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'

export default class Noble extends React.Component {
  render () {
    let noble = this.props.noble

    let cards = []
    for (var i in noble.cards) {
        if (noble.cards[i] > 0) {
            let colorClass = toColorClass(i)
            cards.push(
                <div key={i} className={classNames("noble-card","price",colorClass)}>
                   {noble.cards[i]}
                </div>
            )
        }
    }
    
    return (
      <div className="noble-wrapper">
        <div className="noble">  
            <div className="noble-cards">
                {cards}
            </div>
            <div className="points">3</div>
        </div>    
      </div>
    );
  }
}
