import $ from 'jquery'
import store from './store'
import {updateStateAction} from './actions'
import {newColorArray} from './util'

export function send(endpoint, data) {
    $.ajax(endpoint, {
        method : 'POST',
        contentType: 'application/json',
        data : JSON.stringify(data),
        dataType : "json",
        success : function(result) {
            if (result.error) {
                console.log(result.error)
            } else {
                store.dispatch(updateStateAction(result))
            }}
    })
}

export function sendAction(action) {
    send("action", action)
}

export function sendBuyAction(card, group, index, player) {
    let coins = newColorArray()
    var lack = 0
    for (var i in coins) {
        let require = Math.max(card.price[i] - player.cards[i], 0)
        coins[i] = -Math.min(require, player.coins[i])
        lack += Math.max(require - player.coins[i], 0)  
    }
    sendAction({
        coins: coins,
        gold: -lack,
        cardGroup: group,
        cardIndex: index
    })
}