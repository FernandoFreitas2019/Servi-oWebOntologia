import { Component } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map'

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
  constructor(private http: Http) {
    this.sparkqlData = new Object();
    this.sparkqlData.rows = [];
    this.listaPrefixos.push({ uri: 'rdf', url: 'http://www.w3.org/1999/02/22-rdf-syntax-ns#' });
    this.listaPrefixos.push({ uri: 'owl', url: 'http://www.w3.org/2002/07/owl#' });
    this.listaPrefixos.push({ uri: 'rdfs', url: 'http://www.w3.org/2000/01/rdf-schema#' });
    this.listaPrefixos.push({ uri: 'xsd', url: 'http://www.w3.org/2001/XMLSchema#' });

  }
  criaNovoPrefixo() {
    this.listaPrefixos.push({ uri: '', url: '' });

  }
  remove(i) {
    this.listaPrefixos.splice(i, 1);
  }
  sparkql() {
    if (!this.query || this.query == "") {
      alert("Query vazia!");
      return;

    }

    this.http.post('http://localhost/Jenna/request.php', { query: this.query ,listaPrefixos:this.listaPrefixos}) // 1
      .map(res => res.json())
      .subscribe(data => {
        console.log(data);
        this.sparkqlData = data;
        if (data.err )
          this.er = data.err[0];
      },
        error => {
          this.er = error._body;
        });
  }
}
