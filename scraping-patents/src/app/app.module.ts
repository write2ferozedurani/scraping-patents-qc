import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { SplitterModule } from 'primeng/splitter';
import { ButtonModule } from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import {ScrollTopModule} from 'primeng/scrolltop';
import { MenuModule } from 'primeng/menu';
import { RippleModule } from 'primeng/ripple';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PatentListComponent } from './patent-list/patent-list.component';
import { PatentListService } from './patent-list/patent-list.service';
import { LeftnavComponent } from './leftnav/leftnav.component';

import { MatMenuModule } from '@angular/material/menu';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { ProcessPatentsComponent } from './process-patents/process-patents.component'; 

@NgModule({
  declarations: [
    AppComponent,
    PatentListComponent,
    LeftnavComponent,
    ProcessPatentsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    SplitterModule,
    ButtonModule,
    BrowserAnimationsModule,
    InputTextModule,
    TableModule,
    FormsModule,
    ScrollTopModule,
    MenuModule,
    RippleModule,

    // Material Modules
    MatMenuModule,
    MatListModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatDividerModule
  ],
  providers: [PatentListService],
  bootstrap: [AppComponent]
})
export class AppModule { }
