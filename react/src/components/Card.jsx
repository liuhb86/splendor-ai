import React from 'react'
import {toColorClass, newColorArray, canAfford} from '../util'
import classNames from 'classnames'
import * as Request from '../request'
import editor from '../editor'

export default class Card extends React.Component {
  render () {
    let card = this.props.card
    if (!card) return this.renderEmptyCard();
    let player = this.props.player
    let colorClass = card.color
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

    let affordable = canAfford(card, player)
    
    return (
        <div className={classNames("card", colorClass)}>  
            <div className="card-coins">
                {coins}
            </div>
            <div className="points">{card.point}</div>
            <div className="card-actions">
                {
                    editor.exists() &&
                    <button onClick={this.edit} title="Edit">
                        <span className="icon-pencil" />
                    </button>
                }
                {affordable &&
                    <button onClick={this.buy} title="Buy">
                        <span className="icon-cart" />
                    </button>
                }
                {
                    this.props.reservable &&
                    <button onClick={this.reserve} title="Reserve">
                        <span className="icon-lock" />
                    </button>
                }
            </div>
        </div>
    );
  }

  renderEmptyCard() {
      return (
        <div className={classNames("card", "card-null")} onClick={this.edit}>  
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

  buy = (e) => {
    Request.sendBuyAction(
        this.props.card,
        this.props.group,
        this.props.index,
        this.props.player
    )
  }

  reserve = (e) => {
      Request.sendAction({
          reserve: true,
          gold: this.props.hasGold ? 1 : 0,
          cardGroup: this.props.group,
          cardIndex: this.props.index
      })
  }

  edit = (e) => {
      editor.chooseCards(this.props.group, this.props.index)
  }
}
