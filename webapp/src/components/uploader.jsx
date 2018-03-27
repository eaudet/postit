import React from 'react';
import Dropzone from 'react-dropzone';

interface Note{
    note:string;
    directory:string;
    id:number;}

//Note is called: templating
export default class Basic extends React.Component<Note,any> {

  constructor() {
    super()
    this.state = { files: [] }
  }

//TODO https://react-dropzone.netlify.com/ in order to add input variables and notes.



  onDrop(files) {

    this.setState({
      files
    });
    var formData = new FormData();
    // Fields in the post
    formData.append('data', files[0]);
    formData.append('note', this.props.note.content)
    formData.append('directory', '/Users/erickaudet/dev/postit')
    if (this.props.note.content != null){
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
          <h1>Annotate and Store!</h1>
            <div>
                <Dropzone onDrop={this.onDrop.bind(this)}>
                    <p>Try dropping some files here, or click to select files to upload.</p>
                    <p>{this.props.note}</p>
                    <p>{this.props.id}</p>
                </Dropzone>
                <button onClick={
                  this.onDrop.bind(this)
                }>Delete note</button>
            </div>
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
