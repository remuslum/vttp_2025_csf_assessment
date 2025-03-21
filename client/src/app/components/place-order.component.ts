import { Component, inject, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Item, Order } from '../models';
import { OrderStore } from '../order.store';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RestaurantService } from '../restaurant.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit{

  private orderStore = inject(OrderStore)
  private router = inject(Router)
  private formBuilder = inject(FormBuilder)
  private restaurantSvc = inject(RestaurantService)

  // TODO: Task 3
  protected order$!:Observable<Item[]>
  protected orders:Item[] = []
  protected totalPrice$!:Observable<number>
  protected form!:FormGroup

  ngOnInit(): void {
    this.order$ = this.orderStore.getItems
    this.totalPrice$ = this.orderStore.getTotalPrice
    this.order$.subscribe({
      next: (data) => this.orders = data
    })
    this.form = this.createForm()
  }

  resetOrder():void{
    this.orderStore.addItems([])
    this.router.navigate(["/"])
  }

  createForm():FormGroup {
    return this.formBuilder.group({
      username : this.formBuilder.control<string>('', [Validators.required]),
      password : this.formBuilder.control<string>('', [Validators.required])
    })
  }
  
  isFormInvalid():boolean{
    return this.form.invalid
  }

  submitOrder():void{
    const order:Order = {
      username:this.form.value.username,
      password:this.form.value.password,
      items:this.orders
    }
    this.restaurantSvc.postOrder(order).then((response) => console.log(response))
  }


}
