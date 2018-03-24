import React from 'react';
import Dropzone from 'react-dropzone';


//TODO https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/Using_XMLHttpRequest#Submitting_forms_and_uploading_files

interface Note{
    note:string;
    directory:string;
    id:number;}

//Note in the templating
export default class Basic extends React.Component<Note,any> {

  constructor() {
    super()
    this.state = { files: [] }
  }

  onDrop(files) {

    this.setState({
      files
    });
    var formData = new FormData();
    // Fields in the post
    formData.append('data', files[0]);
    formData.append('data', files[1]);
    formData.append('note', 'Une grosse sauce')
    formData.append('directory', '/Users/erickaudet/dev/postit')
    if (files.length > 1){
        fetch('http://localhost:8080/postit/notes/merge', {
            method: 'post',
            body: formData
            }).then(response => {
            console.log("image uploaded")
            }).catch(err => {
            console.log(err)
            });
        }
    }


  render() {
    return (
      <section>
        <div className="dropzone">
          <Dropzone onDrop={this.onDrop.bind(this)}>
            <p>Try dropping some files here, or click to select files to upload.</p>
            <p>{this.props.note}</p>
            <p>{this.props.id}</p>
          </Dropzone>
        </div>
        <aside>
          <h2>Dropped files</h2>
          <ul>
            {
              this.state.files.map(f => <li key={f.name}>{f.name} - {f.size} bytes</li>)
            }
          </ul>
        </aside>
      </section>
    );
  }
}
