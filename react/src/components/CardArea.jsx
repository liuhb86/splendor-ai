import React from 'react'
import Card from './Card.jsx'
import CardPile from './CardPile.jsx'

class CardRow extends React.Component {
  render() {
    return (
      <div className="card-row">
        <CardPile group={this.props.group} 
          remains={this.props.remains} reservable={this.props.reservable} />
        {
          this.props.cards.map((card, index)=> (
            <Card key={index} group={this.props.group} index={index}
              card={this.props.cards[index]} player={this.props.player} 
              reservable={this.props.reservable} />
          ))
        }
      </div>
    )
  }
}

export default class CardArea extends React.Component {
  render () {
    let rows = []
    let reservable = (this.props.player.reserve.length < 3)
    for (var i = 2; i>=0; i--) {
      let cards = this.props.cards.slice(i*4, i*4+4)
      rows.push(
        <CardRow key={i} group={i} cards={cards} remains={this.props.remains[i]}
          player={this.props.player} reservable={reservable}/>
      )
    }
    return (<div className="card-area">{rows}</div>);
  }
}
