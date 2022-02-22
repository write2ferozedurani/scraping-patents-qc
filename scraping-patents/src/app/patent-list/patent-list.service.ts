import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Patent } from './patent';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PatentListService {

  constructor(private http: HttpClient) { }

  public getPatents(): Observable<Patent[]> {
    return this.http.get<any>('patents/list');
  }
}
