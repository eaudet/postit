import React from 'react';
import Dropzone from 'react-dropzone';


export default class AnnotationForm extends React.Component{
    state = {
        note: "",
        directory: "",
        files: "",
    };
    change = e =>{
        this.setState({
            [e.target.name]: e.target.value
        });
    };
    onSubmit = e =>{
        e.preventDefault();
        console.log(this.state);
    };

    onDrop(files) {
        this.setState({
            files
        });
    }
    render(){
        return <form>
            <input
                name= "note"
                placeholder='Note'
                value={this.state.note}
                onChange={e => this.change(e)}/>
            <br />
            <input
                name= "directory"
                placeholder='Directory'
                value={this.state.directory}
                onChange={e => this.change(e)}/>
            <br />
            <Dropzone onDrop={this.onDrop.bind(this)}>
                <p>Try dropping some files here, or click to select files to upload.</p>
                <p>{this.props.note}</p>
                <p>{this.props.id}</p>
            </Dropzone>
            <button onClick={e => this.onSubmit(e)}>Submit</button>
        </form>;
    }

}