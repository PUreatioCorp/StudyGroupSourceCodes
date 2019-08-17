import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DemoEditComponent } from './demo-edit/demo-edit.component';
import { DemoListComponent } from './demo-list/demo-list.component';
import { DemoAddComponent } from './demo-add/demo-add.component';

@NgModule({
  declarations: [
    AppComponent,
    DemoEditComponent,
    DemoListComponent,
    DemoAddComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
