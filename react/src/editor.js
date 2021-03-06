import * as Request from './request'
import React from 'react'
import { render } from 'react-dom'
import EditorWindow from './components/EditorWindow.jsx'
class Editor {
    init() {
        this.exists = true
        let _self = this
        Request.get("deck", function(deck) {
            _self.deck = deck
        })
    }

    clear() {
        this.exists = false
        this.deck = null 
    }

    exists() {
        return this.exists
    }

    getNobles () {
        return this.deck.nobles
    }

    getCards(group) {
        return this.deck.cards[group]
    }

    chooseCards(group, index, playerIndex) {
        render(<EditorWindow group={group} index={index} 
            cards={this.getCards(group)} playerIndex={playerIndex} />, 
        document.getElementById("editor"))
    }

    chooseNoble(index) {
        render(<EditorWindow index={index} 
            nobles={this.getNobles()}  />, 
        document.getElementById("editor"))
    }
}

let editor = new Editor()

export default editor