import React from 'react'
import {toColorClass} from '../util'
import classNames from 'classnames'

export default class CardPile extends React.Component {
  render () {
    return (
      <div className="card-wrapper">
        <div className="card card-pile">
            <span>{this.props.remains}</span>
        </div>    
      </div>
    );
  }
}
