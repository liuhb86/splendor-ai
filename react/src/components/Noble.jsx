import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'
import * as Request from '../request'

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

    var satisfy = true
    for (var i in noble.cards) {
        if (noble.cards[i] > this.props.player.cards[i]) {
            satisfy = false
            break
        }
    }
    
    return (
        <div className="noble">  
            <div className="noble-cards">
                {cards}
            </div>
            <div className="points">3</div>
            <div className="card-actions">
                { satisfy &&
                    <button onClick={this.take}>Take</button>
                }
            </div>
        </div>
    );
  }

  take = (e) => {
      Request.sendAction({nobleIndex: this.props.index})
  }
}
