import { Component, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { HttpClient } from '@angular/common/http';

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

  processPatents() {
    this.http.post<any>('patents/process', this.patentsList).subscribe(message => {
      this.sucessMessage = message;
    });

  }

}
