import React from 'react'
import LittleCardCount from './LittleCardCount.jsx'

export default class LittleCardCounts extends React.Component {
  render() {
    return(
      <span>
        {
          this.props.cards.map((count, index) =>(
            <LittleCardCount key={index} color={index} count={count} skipZero={this.props.skipZero}/>
          ))
        }
      </span>
    )
  }
}