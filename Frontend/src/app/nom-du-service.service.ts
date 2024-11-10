import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { piste } from './Piste';

@Injectable({
  providedIn: 'root'
})
export class NomDuServiceService {

  readonly API_URL = 'http://192.168.100.34:8089/api/piste';

  constructor(private httpClient: HttpClient) { }

  // Get all pistes
  getAllPiste() {
    return this.httpClient.get(`${this.API_URL}/all`);
  }

  // Add a new piste
  addPiste(piste: any) {
    return this.httpClient.post(`${this.API_URL}/add`, piste);
  }

  // Delete a piste by numPiste
  deletePiste(numPiste: number): Observable<any> {
    return this.httpClient.delete(`${this.API_URL}/delete/${numPiste}`);
  }
}
