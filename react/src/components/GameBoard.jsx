import React from 'react'
import NobleArea from './NobleArea.jsx'
import CoinArea from './CoinArea.jsx'
import CardArea from './CardArea.jsx'
import PlayerArea from './PlayerArea.jsx'

export default class GameBoard extends React.Component {
  render () {
    let game = this.props.game
    let state = game.state
    let player = state.players[game.turn]
    return (
        <div style={{display:"flex"}}>
            <div>
                <div style={{display:"flex"}}>
                    <NobleArea nobles={state.nobles} player={player} />
                    <CoinArea coins={state.coins} golds={state.golds} player={player} />
                </div>
                <CardArea cards={state.cards} remains={game.cardPile ? game.cardPile.size :[-1,-1,-1]}
                    player={player} hasGold={state.golds > 0}/>
            </div>
            <PlayerArea players={state.players} turn={game.turn}/>
        </div>
    );
  }
}
