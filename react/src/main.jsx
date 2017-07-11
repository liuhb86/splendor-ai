import React from 'react'
import { render } from 'react-dom'
import store from './store'
import settings from './settings'
import editor from './editor'
import SplendorApp from './components/SplendorApp.jsx'

function renderApp() { 
    render(
        <SplendorApp state={store.getState()}/>,
    document.getElementById('react-root')
    )
}

store.subscribe(renderApp)
renderApp()
import * as Request from './request'
//Request.send("init", {players: 3, autoMode: true})

export {store, settings, editor}