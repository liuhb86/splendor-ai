import React from 'react'
import SetupForm from './SetupForm.jsx'
import GameBoard from './GameBoard.jsx'
import ActionList from './ActionList.jsx'

export default class SplendorApp extends React.Component {
  render () {
    if (this.props.state == null) return (<SetupForm />)
    let game = this.props.state.game
    return (
      <div style={{display:"flex"}}>
        <GameBoard game={game}/>
        <div>
          <div> {this.props.state.message} </div>
          <ActionList actions={game.actionList} />
        </div>
      </div>
    );
  }
}
