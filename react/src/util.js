let colors = ["WHITE", "BLUE", "GREEN", "RED", "ONYX", "YELLOW"]

export function toColorClass(i) {
    return colors[i]
}

export function sum(arr) {
    return arr.reduce((a, b) => a + b, 0)
}