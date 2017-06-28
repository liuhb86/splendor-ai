import React from 'react'
import classNames from 'classnames'
import settings from '../settings'
import LittleCoins from './LittleCoins.jsx'
import LittleCard from './LittleCard.jsx'
import LittleCardCounts from './LittleCardCounts.jsx'


export default class ActionListItem extends React.Component {
  render () {
      let action = this.props.action
      var coinChanged = action.gold != 0
      if (!coinChanged && action.coins) {
        for (var c of action.coins) {
          if (c != 0) {
            coinChanged = true
            break
          }
        }
      }
    
    let band = this.props.index % 2

    return (
      <div className={classNames("action-list-item", {band: band})}>
        <div>
            <span>
              #{this.props.index}&nbsp;&nbsp;
              <span className="icon-user"/>
              {settings.playerNames[action.playerIndex]} &nbsp;&nbsp;
            </span>
        </div>
        { coinChanged &&
          <div>
            <span className="icon-coin-euro"/>
            <LittleCoins coins={action.coins} golds={action.gold} skipZero={true} />
          </div>
        }
        { action.card &&
          <div>
            <span className={action.reserve ? "icon-lock" : "icon-cart"} />
            <LittleCard card={action.card} />
          </div>
        }
        {
          action.noble &&
          <div>
            <span className="icon-trophy" />
            <LittleCardCounts cards={action.noble.cards} skipZero={true} />
          </div>
        }
      </div>
    );
  }
}
