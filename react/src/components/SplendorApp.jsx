import React from 'react'
import SetupForm from './SetupForm.jsx'

export default class SplendorApp extends React.Component {
  render () {
    let state = this.props.state
    if (state == null) return (<SetupForm />)
    return (
      <div>
        "hello world!"
      </div>
    );
  }
}
