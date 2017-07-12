import React from 'react'
import LittleCard from './LittleCard.jsx'
import LittleCardCounts from './LittleCardCounts.jsx'
import {toColorIndex} from '../util'
import * as Request from '../request'
import { render } from 'react-dom'
import classNames from 'classnames'

export default class EditorWindow extends React.Component {
  render () {
    if (this.props.cards) return this.renderCards()
    else return this.renderNoble()
  }

  renderCards() {
      let sortedCards = [[],[],[],[],[]]
      for (var card of this.props.cards) {
          sortedCards[toColorIndex(card.color)].push(card)
      }
      for (var oneColor of sortedCards) {
          oneColor.sort(function(a,b) {
              if (!a.used && b.used) return -1
              if (a.used && !b.used) return 1
              if (a.point != b.point) return a.point - b.point
              for (var i in a.price) {
                  if (a.price[i] != b.price[i]) {
                     if (a.price[i]==0) return 1
                     if (b.price[i]==0) return -1
                     return a.price[i] - b.price[i]
                  }
              }
              return 0
          })
      }
      return (
        <div className={"editor-window"} onClick={this.close}>
                <div style={{display:"flex"}}>
                    { sortedCards.map((oneColor, index) => (
                        <div className="editor-column" key={index}>
                            { oneColor.map((card, index2) => (
                                <div key={index2} className={classNames("editor-item", {used : card.used})}
                                    onClick={this.choose.bind(this, card)}>
                                    <LittleCard card={card} />
                                </div>
                            ))}
                        </div>
                    ))}
                </div>
        </div>
      )
  }

  renderNoble() {
      let columns = [[],[],[],[],[]]
      for (var i in this.props.nobles) {
          columns[i % 5].push(this.props.nobles[i])
      }
      return (
        <div className={"editor-window"} onClick={this.close}>
                <div style={{display:"flex"}}>
                    { columns.map((oneColumn, index) => (
                        <div className="editor-column-noble" key={index}>
                            { oneColumn.map((noble, index2) => (
                                <div key={index2} className="editor-item" 
                                    onClick={this.chooseNoble.bind(this, noble)}>
                                    <LittleCardCounts cards={noble.cards} skipZero={true} />
                                </div>
                            ))}
                        </div>
                    ))}
                </div>
        </div>
      )
  }

  choose = (card, e) => {
      e.stopPropagation()
      card.used = true
      this.close()
      Request.send("updateCard", {
          cardIndex: this.props.index,
          card: card,
          playerIndex: this.props.playerIndex
        })
  }

  chooseNoble = (noble, e) => {
      e.stopPropagation()
      this.close()
      Request.send("updateNoble", {
          cardIndex: this.props.index,
          noble: noble
        })
  }

  close() {
      render(<noscript />, document.getElementById("editor"))
  }
}
