import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'
import * as Request from '../request'
import editor from '../editor'

export default class Noble extends React.Component {
  render () {
    let noble = this.props.noble
    if (!noble) return this.renderEmptyNoble();

    let cards = []
    for (var i in noble.cards) {
        if (noble.cards[i] > 0) {
            let colorClass = toColorClass(i)
            cards.push(
                <div key={i} className={classNames("card-count","price",colorClass)}>
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
                {
                    editor.exists() &&
                    <button onClick={this.edit} title="Edit">
                        <span className="icon-pencil" />
                    </button>
                }
                { satisfy &&
                    <button onClick={this.take} title="Take">
                        <span className="icon-plus" />
                    </button>
                }
            </div>
        </div>
    );
  }

  renderEmptyNoble() {
      return (
        <div className="noble" onClick={this.edit}>  
            <div className="card-actions">
                {
                    editor.exists() &&
                    <button onClick={this.edit} title="Edit">
                        <span className="icon-pencil" />
                    </button>
                }
            </div>
        </div>
      )
  }

  take = (e) => {
      Request.sendAction({nobleIndex: this.props.index})
  }

  edit = (e) => {
      editor.chooseNoble(this.props.index)
  }
}
