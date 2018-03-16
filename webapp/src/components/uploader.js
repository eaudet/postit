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
    //TODO bytes is null
    const reader = new FileReader();
    fetch('http://localhost:8080/postit/notes/upload', {
    	method: 'post',
    	headers: new Headers({
                 		'Content-Type': 'application/json;charset=UTF-8'
                 	}),
    	body: JSON.stringify({
    		note: 'Une note',
    		fileName: 'Some dammed file.pdf',
    		directory: '/Users/erickaudet/dev/postit',
    		bytes: reader.readAsBinaryString(files[0])
    	})
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
