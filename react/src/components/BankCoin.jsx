import React from 'react'
import classNames from 'classnames'
import {toColorClass} from '../util'

export default class BankCoin extends React.Component {
  render() {
    let colorClass = toColorClass(this.props.index)
    return (
      <div className="bank-coin-wrapper">
        <div className={classNames("bank-coin", colorClass)}>
          {this.props.count}
        </div> 
      </div>
    )
  }
}