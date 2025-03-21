import { Component, inject, OnInit } from '@angular/core';
import { ResponseService } from '../response.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { ResponseFromPayment } from '../models';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit{

  // TODO: Task 5
  private responseSvc = inject(ResponseService)

  protected response$ !: BehaviorSubject<any>
  protected paymentResponse !: ResponseFromPayment

  ngOnInit(): void {
    this.responseSvc.getValue().asObservable().subscribe({
      next : (data) => {
        const responseFromPayment = {
          orderId:data.orderId,
          paymentId:data.paymentId,
          total:data.total,
          timestamp:data.timestamp
        }
        this.paymentResponse = responseFromPayment
      }
        
    })
  }


}
