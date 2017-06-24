export const UPDATE_STATE ="UPDATE_STATE"

export function updateStateAction(state) {
    return {
        type : UPDATE_STATE,
        state
    }
}