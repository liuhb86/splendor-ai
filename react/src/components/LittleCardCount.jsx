import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'

export default class LittleCardCount extends React.Component {
  render() {
    if (this.props.skipZero && this.props.count==0) return null
    let colorClass = this.props.colorClass || toColorClass(this.props.color)
    return(
      <span className="little-card-count">
        <span className={classNames("little-card-color", colorClass)} />{this.props.count}&nbsp; 
      </span>
    )
  }
}