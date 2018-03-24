
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Basic from './components/uploader.js'

class Game extends React.Component {

    render(){
        return (
            <div className="game">
                <Basic note="hello world" id="112131"
                />
            </div>
        );
    }

}

// ========================================

ReactDOM.render(
  <Game/>,
  document.getElementById('root')
);
