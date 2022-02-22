import { Component, OnInit } from '@angular/core';
import { Patent } from './patent';
import { PatentListService } from './patent-list.service';

@Component({
  selector: 'app-patent-list',
  templateUrl: './patent-list.component.html',
  styleUrls: ['./patent-list.component.css']
})
export class PatentListComponent implements OnInit {

  patents: Patent[];

  constructor(private patentService : PatentListService) { 
    this.patents =[];
  }

  ngOnInit(): void {
    this.getPatentList();
  }

  private getPatentList() {
    this.patentService.getPatents().subscribe((items) => {
      this.patents = items;
    })
  }

}
