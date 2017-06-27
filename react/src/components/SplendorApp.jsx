import React from 'react'
import SetupForm from './SetupForm.jsx'
import GameBoard from './GameBoard.jsx'

export default class SplendorApp extends React.Component {
  render () {
    let game = this.props.game
    if (game == null) return (<SetupForm />)
    return (<GameBoard game={game}/>);
  }
}
