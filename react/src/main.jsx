import React from 'react'
import { render } from 'react-dom'
import store from './store'
import SplendorApp from './components/SplendorApp.jsx'

function renderApp() { 
    render(
        <SplendorApp game={store.getState()}/>,
    document.getElementById('react-root')
    )
}

store.subscribe(renderApp)
//renderApp()
import * as Request from './request'
Request.send("init", {players: 3, autoMode: true})

export {store}