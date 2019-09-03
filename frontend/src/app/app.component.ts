import { Component, EventEmitter } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map'
import { UploadOutput, UploadInput, UploadFile, humanizeBytes, UploaderOptions, UploadStatus } from 'ngx-uploader';
import { Subject } from 'rxjs/Subject';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  private sparkqlData = null;
  uri: any;
  er: any;
  listaPrefixos = [];
  query: any;
  enableButoon: boolean = false;
  porta: any;
  formData: FormData;
  files: UploadFile[];
  uploadInput: EventEmitter<UploadInput>;
  humanizeBytes: Function;
  dragOver: boolean = false;
  options: UploaderOptions;
  scannerDocument: boolean = false;
  uploadDocument: boolean = false;
  fileUpload: boolean = false;
  public onClose: Subject<boolean>;
  fileAttachment: any;



  constructor(private http: Http) {
    this.sparkqlData = new Object();
    this.sparkqlData.rows = [];
    this.listaPrefixos.push({ uri: 'rdf', url: 'http://www.w3.org/1999/02/22-rdf-syntax-ns#' });
    this.listaPrefixos.push({ uri: 'owl', url: 'http://www.w3.org/2002/07/owl#' });
    this.listaPrefixos.push({ uri: 'rdfs', url: 'http://www.w3.org/2000/01/rdf-schema#' });
    this.listaPrefixos.push({ uri: 'xsd', url: 'http://www.w3.org/2001/XMLSchema#' });
    this.options = { concurrency: 1, maxUploads: 1 };
    this.files = [];
    this.uploadInput = new EventEmitter<UploadInput>();
    this.humanizeBytes = humanizeBytes;
    this.onClose = new Subject();
    this.fileAttachment = new Object();

  }
  criaNovoPrefixo() {
    this.listaPrefixos.push({ uri: '', url: '' });

  }
  onUploadOutput(output: UploadOutput): void {
    this.uploadDocument = true;
    if (output.type === 'allAddedToQueue') {
      const event: UploadInput = {
        type: 'uploadAll',
        url: 'http://localhost/BackendOntologia/upload.php',
        method: 'POST',
        data: { foo: 'bar' }
      };

      this.uploadInput.emit(event);
    } else if (output.type === 'addedToQueue' && typeof output.file !== 'undefined') {
      this.files.push(output.file);
      this.fileUpload = true;
    } else if (output.type === 'uploading' && typeof output.file !== 'undefined') {
      const index = this.files.findIndex(file => typeof output.file !== 'undefined' && file.id === output.file.id);
      this.files[index] = output.file;
    } else if (output.type === 'removed') {
      this.files = this.files.filter((file: UploadFile) => file !== output.file);
    } else if (output.type === 'dragOver') {
      this.dragOver = true;
    } else if (output.type === 'dragOut') {
      //this.dragOver = false;
    } else if (output.type === 'drop') {
      //this.dragOver = false;
    } else if (output.type === 'rejected' && typeof output.file !== 'undefined') {
      console.log(output.file.name + ' rejected');
    }
    if (output.file) {
      if (output.file.response) {
        this.enableButoon = true;
        this.porta = output.file.response.porta;
        this.listaPrefixos.push({ uri: 'uri', url: output.file.response.baseUri });
    
      }
    }
    if (this.files) {
      if (this.files.length > 0) {
        if (this.files[0].response) {
          if (this.files[0].response.generatedName) {
            this.fileAttachment.arquivoArmazenamento = this.files[0].response.generatedName;
          }
        }
      }
    }
    // this.files = this.files.filter(file => file.progress.status !== UploadStatus.Done);
  }
  remove(i) {
    this.listaPrefixos.splice(i, 1);
  }
  sparkql() {
    if (!this.query || this.query == "") {
      alert("Query vazia!");
      return;

    }

    this.http.post('http://localhost/BackendOntologia/request.php', { query: this.query, listaPrefixos: this.listaPrefixos, porta: this.porta }) // 1
      .map(res => res.json())
      .subscribe(data => {
        console.log(data);
        this.sparkqlData = data;
        if (data.err)
          this.er = data.err[0];
      },
        error => {
          this.er = error._body;
        });
  }
}
