import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import AnnotationForm from './components/annotationForm.jsx';
// import App from 'jarics-react-components';

class Postit extends React.Component {

    render() {
        return <div className="game">
            <div>
                <AnnotationForm/>
            </div>
            {/*<div>*/}
                {/*<App/>*/}
            {/*</div>*/}
        </div>;
    }

}

// ========================================

ReactDOM.render(
    <Postit/>,
    document.getElementById('root')
);
