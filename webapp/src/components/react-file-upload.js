import React from 'react'
import axios, { post, then } from 'axios';

export class SimpleReactFileUpload extends React.Component {

  constructor(props) {
    super(props);
    this.state ={
      file:null
    }
    this.onFormSubmit = this.onFormSubmit.bind(this)
    this.onChange = this.onChange.bind(this)
    this.sendFile = this.sendFile.bind(this)
  }
  onFormSubmit(e){
    e.preventDefault() // Stop form submit
    this.sendFile(this.state.file)
  }

  onChange(e) {
    this.setState({file:e.target.files[0]})
  }

// TODO how to send the file as byte array
    sendFile(file) {
       axios.post('http://localhost:8080/postit/notes/', {
           note: 'Hello Satan!',
           directory: 'aqq',
           fileName: 'aqaqa',
           bytes: 'dewded',
           contentType: 'swsw',
         })
         .then(function (response) {
           console.log(response);
         })
         .catch(function (error) {
           console.log(error);
       });
    }

  render() {
    return (
      <form onSubmit={this.onFormSubmit}>
        <h1>File Upload</h1>
        <input type="file" onChange={this.onChange} />
        <button type="submit">Upload</button>
      </form>
   )
  }
}
