import React from 'react'
import classNames from 'classnames'
import {toColorClass, newColorArray} from '../util'
import * as Request from '../request'

export default class BankCoin extends React.Component {
  render() {
    let colorClass = toColorClass(this.props.index)
    let canTake = this.props.count > 0 && this.props.canTake
    return (
      <div className="bank-coin-wrapper">
        <div className={classNames("bank-coin", colorClass)}>
          {this.props.count}
        </div>
        <div>
          { canTake && 
            <button onClick={this.takeCoin} title="Take">
                <span className="icon-plus" />
            </button>
          }
        </div>
        <div>
          { this.props.canReturn &&
            <button onClick={this.returnCoin} title="Return">
              <span className="icon-minus" />
            </button>
          }
        </div>
      </div>
    )
  }

  takeGold() {
    Request.sendAction({gold : 1})
  }

  returnGold() {
    Request.sendAction({gold : -1})
  }

  takeCoin = (e) => {
    if (this.props.index == 5) return this.takeGold()
    let coins = newColorArray()
    coins[this.props.index] = 1
    Request.sendAction({coins : coins})
  }

   returnCoin = (e) => {
    if (this.props.index == 5) return this.returnGold()
    let coins = newColorArray()
    coins[this.props.index] = -1
    Request.sendAction({coins : coins})
  }
}