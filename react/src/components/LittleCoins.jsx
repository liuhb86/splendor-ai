import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'

class LittleCoin extends React.Component {
  render() {
    let count = this.props.count || 0
    if (this.props.skipZero && count==0) return null
    let colorClass = toColorClass(this.props.color)
    return(
      <span className="little-coin">
        <span className={classNames("little-coin-color", colorClass)} />{count}&nbsp; 
      </span>
    )
  }
}

export default class LittleCoins extends React.Component {
  render() {
    return(
      <span>
        { this.props.coins &&
          this.props.coins.map((count, index) =>(
            <LittleCoin key={index} color={index} count={count} skipZero={this.props.skipZero}/>
          ))
        }
        <LittleCoin key={5} color={5} count={this.props.golds} skipZero={this.props.skipZero} />
      </span>
    )
  }
}