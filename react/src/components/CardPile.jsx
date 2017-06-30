import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'
import * as Request from '../request'

export default class CardPile extends React.Component {
  render () {
    return (
        <div className="card card-pile">
            <div className="remaining-cards">
                <span>{this.props.remains}</span>
            </div>
            <div className="card-actions">
              {
                  this.props.reservable && this.props.remains != 0 &&
                  <button onClick={this.reserve} title="Reserve">
                        <span className="icon-lock" />
                  </button>
              }
            </div>   
        </div>
    );
  }

  reserve = (e) => {
      Request.sendAction({
          reserve: true,
          gold: this.props.hasGold ? 1 : 0,
          cardGroup: this.props.group,
          cardIndex: -1
      })
  }
}
