import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Patent } from './patent';
import { Observable } from 'rxjs';
import { GlobalConstants } from '../common/GlobalConstants';

@Injectable({
  providedIn: 'root'
})
export class PatentListService {

  constructor(private http: HttpClient) { }

  public getPatents(): Observable<Patent[]> {
    return this.http.get<any>(GlobalConstants.REST_PATENT_LIST_URL);
  }
}
