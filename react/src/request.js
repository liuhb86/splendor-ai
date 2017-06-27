import $ from 'jquery'
import store from './store'
import {updateStateAction} from './actions'

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