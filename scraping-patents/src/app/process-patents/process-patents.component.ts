import { Component, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { HttpClient } from '@angular/common/http';
import { GlobalConstants } from '../common/GlobalConstants';

@Component({
  selector: 'app-process-patents',
  templateUrl: './process-patents.component.html',
  styleUrls: ['./process-patents.component.css']
})
export class ProcessPatentsComponent implements OnInit {

  patentsList: string;
  sucessMessage: string;

  constructor(private primengConfig: PrimeNGConfig, private http : HttpClient) {
    this.patentsList='';
    this.sucessMessage='';
  }

  ngOnInit(): void {
    this.primengConfig.ripple = true;
  }

  public processPatents() {
    this.http.post(GlobalConstants.REST_PATENT_PROCESS_URL, this.patentsList, {
      responseType: 'text'
    }).subscribe(res => {
        this.sucessMessage = res;
        this.patentsList='';
    });

  }

}
