import React from 'react'
import * as Request from '../request'
import ActionList from './ActionList.jsx'

export default class ActionPanel extends React.Component {
  render () {
      return (
      <div className="action-panel">
        <div> {this.props.message} </div>
        <div>
          {
            this.props.actions.length > 0 &&
            <button onClick={this.undo} title="Solve">
                <span className="icon-undo" /></button>
          }
        </div>
        <ActionList actions={this.props.actions}/>
      </div>
    );
  }
  
  undo() {
    Request.send("undo", {})
  }
}
