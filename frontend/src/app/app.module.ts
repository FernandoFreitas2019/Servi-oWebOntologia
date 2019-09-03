import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { NgxUploaderModule } from 'ngx-uploader';
import { ProgressbarModule } from 'ngx-bootstrap';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    ProgressbarModule.forRoot(),
    FormsModule,
    NgxUploaderModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
