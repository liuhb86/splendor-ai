import React from 'react'
import { render } from 'react-dom'
import store from './store'
import SplendorApp from './components/SplendorApp.jsx'

function renderApp() { 
    render(
        <SplendorApp state={store.getState()}/>,
    document.getElementById('react-root')
    )
}

store.subscribe(renderApp)
renderApp()

export {store}