import React from 'react';
import Dropzone from 'react-dropzone';


//TODO https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/Using_XMLHttpRequest#Submitting_forms_and_uploading_files


export default class Basic extends React.Component {
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
    formData.append('note', 'Une grosse sauce')
    fetch('http://localhost:8080/postit/notes/upload2', {
        method: 'post',
        body: formData
        }).then(response => {
        console.log("image uploaded")
        }).catch(err => {
        console.log(err)
        });
    }


  render() {
    return (
      <section>
        <div className="dropzone">
          <Dropzone onDrop={this.onDrop.bind(this)}>
            <p>Try dropping some files here, or click to select files to upload.</p>
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
