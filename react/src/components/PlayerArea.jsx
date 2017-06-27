import React from 'react'
import Player from './Player.jsx'

export default class PlayerArea extends React.Component {
  render () {
    return (
      <div className="player-area">
        {
          this.props.players.map((player, index)=> (
            <Player key={index} index={index} player={this.props.players[index]} 
              active={this.props.turn==index} />
          ))
        }
      </div>
    );
  }
}
