let colors = ["WHITE", "BLUE", "GREEN", "RED", "ONYX", "YELLOW"]
let colorMap = {}
for (var i in colors) {
    colorMap[colors[i]] = i
}

export function toColorClass(i) {
    return colors[i]
}

export function toColorIndex(color) {
    return colorMap[color]
}

export function sum(arr) {
    return arr.reduce((a, b) => a + b, 0)
}

export function newColorArray() {
    return [0,0,0,0,0]
}

export function canAfford(card, player) {
    if (!card) return false
    var lack = 0
    for (var i in card.price) {
     lack += Math.max(card.price[i] - player.coins[i] -player.cards[i] , 0)  
    }
    return (lack <= player.golds)
}