import React from 'react'
import SetupForm from './SetupForm.jsx'
import GameBoard from './GameBoard.jsx'
import ActionList from './ActionList.jsx'

export default class SplendorApp extends React.Component {
  render () {
    let game = this.props.game
    if (game == null) return (<SetupForm />)
    return (
      <div style={{display:"flex"}}>
        <GameBoard game={game}/>
        <ActionList actions={game.actionList} />
      </div>
    );
  }
}
