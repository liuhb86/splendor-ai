import $ from 'jquery'
import store from './store'
import {updateStateAction} from './actions'

export default class Request {
    send(endpoint, data) {
        $.post(ednpoint,data, function(result){
            if (result.error) {
                console.log(result.error)
            } else {
                store.dispatch(updateStateAction(result))
            }
        }, "json")
    }
}