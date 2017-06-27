import React from 'react'
import Noble from './Noble.jsx'

export default class NobleArea extends React.Component {
  render () {
      return (
      <div className="noble-area">
        { this.props.nobles.map((noble, index) => (
                <Noble key={index} index={index} noble={noble} player={this.props.player} />
        ))}
      </div>
    );
  }
}
