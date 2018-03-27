import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
// import Basic from './components/uploader.jsx'
import AnnotationForm from './components/annotationForm.jsx'

class Postit extends React.Component {

    render() {
        return (
            <div className="game">
                {/*<div>*/}
                    {/*<Basic note="hello world" id="112131"*/}
                    {/*/>*/}
                {/*</div>*/}
                <div>
                    <AnnotationForm />
                </div>
            </div>
        );
    }

}

// ========================================

ReactDOM.render(
    <Postit/>,
    document.getElementById('root')
);
