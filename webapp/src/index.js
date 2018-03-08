
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {Board} from './components/board.js'
import {calculateWinner} from './components/square.js'


//TODO https://reactjs.org/tutorial/tutorial.html#showing-the-moves

class Game extends React.Component {

    constructor(props) {
    super(props);
    this.state = {
      status: "none",
      history: [{
        squares: Array(9).fill(null),
      }],
      xIsNext: true,
    };
    }

    handleClick(i) {
        const history = this.state.history;
        const current = history[history.length - 1];
        const squares = current.squares.slice();
        if (calculateWinner(squares) || squares[i]) {
          return;
        }
        squares[i] = this.state.xIsNext ? 'X' : 'O';
        this.setState({
          history: history.concat([{
            squares: squares,
          }]),
          xIsNext: !this.state.xIsNext,
        });
      }

  render() {
    const history = this.state.history;
    const current = history[history.length - 1];
    const winner = calculateWinner(current.squares);

    //let status;
    if (winner) {
      this.state.status = 'Winner: ' + winner;
    } else {
      this.state.status = 'Next player: ' + (this.state.xIsNext ? 'X' : 'O');
    }


    return (
      <div className="game">
        <div className="game-board">
          <Board
            squares={current.squares}
            onClick={(i) => this.handleClick(i)}
          />
        </div>
        <div className="game-info">
          <div>{this.state.status}</div>
          <ol>{/* TODO */}</ol>
        </div>
      </div>
    );
  }
}

// ========================================

ReactDOM.render(
  <Game />,
  document.getElementById('root')
);
