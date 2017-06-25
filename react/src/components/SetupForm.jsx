import React from 'react'
import * as request from '../request'

export default class SetupForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {players : 3, autoMode : true};
    }

    render () {
        return (
            <div>
                <h1> Splendor </h1>
                <div>
                    <label> Players </label>
                    <select name="players" value={this.state.players} 
                        onChange={this.handleInputChange}>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                    </select> 
                </div>
                <div>
                     <label> auto mode
                      <input type="checkbox" name="autoMode" checked={this.state.autoMode}
                        onChange={this.handleInputChange} />
                    </label>
                </div>
                <div>
                    <button onClick={this.submit}> Play! </button>
                </div>
            </div>
        )
    }

    submit = (e) => {
        request.send("init", this.state)
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        this.setState({
        [name]: value
        });
    }
}