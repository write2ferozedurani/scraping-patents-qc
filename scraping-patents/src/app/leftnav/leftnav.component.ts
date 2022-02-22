import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Patent } from '../patent-list/patent';
import { PatentListService } from '../patent-list/patent-list.service';

@Component({
  selector: 'app-leftnav',
  templateUrl: './leftnav.component.html',
  styleUrls: ['./leftnav.component.css']
})
export class LeftnavComponent implements OnInit {

  lefnav: MenuItem[];
  patents: Patent[];
  
  constructor(private patentService: PatentListService) { 
    this.patents =[];
    this.lefnav = [];
  }

  ngOnInit(): void {
    this.lefnav = [
      {
        label: 'Patents',
        icon: 'pi pi-list',
        routerLink: ['/patents'],
        command: () => {
            this.getPatentList();
        }
      }, 
      {
        label: 'Process Patents',
        icon: 'pi pi-cloud-upload',
        routerLink: ['/patents']
        
      }
    ];
  }

  private getPatentList() {
    this.patentService.getPatents().subscribe((items) => {
      this.patents = items;
    })
  }

}
