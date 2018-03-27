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
        let formData = new FormData();
        // Fields in the post
        formData.append('data', this.state.files[0]);
        formData.append('note', this.state.note);
        formData.append('directory', this.state.directory);
        fetch('http://localhost:8080/postit/notes/annotate', {
            method: 'post',
            body: formData
        }).then(response => {
            console.log("image uploaded")
        }).catch(err => {
            console.log(err)
        });
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