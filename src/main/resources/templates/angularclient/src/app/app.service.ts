import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  private apiServerUrl = '';

  constructor(private http: HttpClient) {};

  public getMethodResult(functionString: string){
    const params = new HttpParams().set(
      'functionString', functionString
    )
    console.log("good!");
    return this.http.get(
      `${this.apiServerUrl}/method/halving`, {params}
    )
  } 
}
