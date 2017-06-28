import React from 'react'
import ActionListItem from './ActionListItem.jsx'

export default class ActionList extends React.Component {
  render () {
      let totalItems = this.props.actions.length
      return (
      <div className="action-list">
        {
          this.props.actions.map((item, index) => (
            <ActionListItem key={totalItems - index} index={totalItems - index}
              action={item} />
          ))
        }
      </div>
    );
  }
}
