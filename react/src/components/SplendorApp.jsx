import React from 'react'
import SetupForm from './SetupForm.jsx'
import GameBoard from './GameBoard.jsx'
import ActionPanel from './ActionPanel.jsx'

export default class SplendorApp extends React.Component {
  render () {
    if (this.props.state == null) return (<SetupForm />)
    let game = this.props.state.game
    return (
      <div style={{display:"flex"}}>
        <GameBoard game={game}/>
        <ActionPanel actions={game.actionList} message={this.props.state.message}/>
      </div>
    );
  }
}
