import React from 'react';
import Dropzone from 'react-dropzone';

//TODO https://github.com/react-dropzone/react-dropzone/tree/master/examples/Accept

export default class Basic extends React.Component {
  constructor() {
    super()
    this.state = { files: [] }
  }

  onDrop(files) {
    this.setState({
      files
    });
    //TODO check byte size.....still not working
    var reader = new FileReader();
    reader.readAsArrayBuffer(files[0]);
    reader.onload = function() {
      var arrayBuffer = reader.result
      var allBytes = new Uint8Array(arrayBuffer);
        fetch('http://localhost:8080/postit/notes/upload', {
            method: 'post',
            headers: new Headers({
                            'Content-Type': 'application/json;charset=UTF-8'
                        }),
            body: JSON.stringify({
                note: 'Une note',
                fileName: 'Some dammed file.pdf',
                directory: '/Users/erickaudet/dev/postit',
                bytes: btoa(allBytes),
            })
        });
    }

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
