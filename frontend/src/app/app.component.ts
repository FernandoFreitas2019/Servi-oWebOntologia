import { Component } from '@angular/core';
import { Http, RequestOptionsArgs, Headers, URLSearchParams } from '@angular/http';
import 'rxjs/add/operator/map'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  private sparkqlData = null;
  uri: any;
  er:any;
  
  constructor(private http: Http) {


  }

  sparkql(query) {
    if (!query || query == "") {
      alert("Query vazia!");
      return;

    }
    let headers: Headers = new Headers({
      'Content-type': 'application/x-www-form-urlencoded',
      'Accept': 'application/json'
    });


    let options: RequestOptionsArgs = {
      headers: headers,
    };

    this.http.get('http://localhost:3030/ds/query?query=' + query, options) // 1
      .map(response => response.json())
      .subscribe(data => {
        console.log(data);


        for (var i = 0; i < data.results.bindings.length; i++) {
          data.results.bindings[i].type.value = data.results.bindings[i].type.value.replace(this.uri, "");
        }
        this.sparkqlData = data.results.bindings; // 3
      },
      error => {
       this.er=error._body; 
        
      });
  }
}
