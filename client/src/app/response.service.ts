import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { ResponseFromPayment } from './models';

@Injectable({
  providedIn: 'root'
})
export class ResponseService {

  response:BehaviorSubject<any> = new BehaviorSubject(null)

  constructor() { }

  addValue(response:any):void{
    this.response.next(response)
  }

  getValue():BehaviorSubject<any>{
    return this.response
  }
}
