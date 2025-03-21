import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Observable } from 'rxjs';
import { Item } from '../models';
import { OrderStore } from '../order.store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{
  // TODO: Task 2

  private restaurantSvc = inject(RestaurantService)
  private orderStore = inject(OrderStore)
  private router = inject(Router)

  protected menu$ !: Observable<Item[]>
  protected menuWithQuantity : Item[] = []
  protected menuWithoutQuantity : Item[] = []
  protected orders:Item[] = []
  protected price:number = 0
  protected quantity:number = 0
  

  ngOnInit():void {
    this.restaurantSvc.getMenuItems().subscribe({

      next: (data) => {
        this.menuWithoutQuantity = data
        this.menuWithQuantity = this.populateArray()
      }
    })

    this.orderStore.getItems.subscribe({
      next: (data) => {
        this.orders = data
      }
        
    })
  }

  increment(item:Item):void{
    item.quantity += 1
    var index = this.orders.indexOf(item)
    if(index < 0){
      this.orderStore.addItemToOrder(item)
    } 
    this.price = this.calculatePrice(this.orders)
    this.quantity = this.calculateQuantity(this.orders)

  }

  decrement(item:Item):void{
    item.quantity -= 1
    if(item.quantity === 0){
      this.orderStore.deleteItemFromCart(item._id)
    }
    this.price = this.calculatePrice(this.orders)
    this.quantity = this.calculateQuantity(this.orders)
  }

  populateArray():Item[]{
    var menuArray:Item[] = []
    for(var i = 0; i < this.menuWithoutQuantity.length; i++){
      var itemWithoutQuantity:Item = this.menuWithoutQuantity[i]
      const itemWithQuantity:Item = {
        _id:itemWithoutQuantity._id,
        name:itemWithoutQuantity.name,
        description:itemWithoutQuantity.description,
        price:itemWithoutQuantity.price,
        quantity:0
      }
      menuArray.push(itemWithQuantity)
    }
    return menuArray;
  }

  isOrderEmpty():boolean{
    return this.orders.length === 0
  }

  confirmOrder():void{
    this.router.navigate(["/place-order"])
  }

  calculatePrice(items:Item[]):number{
    var totalPrice:number = 0
    for(var i = 0; i< items.length; i++){
      totalPrice += items[i].quantity * items[i].price
    }
    return totalPrice
  }

  calculateQuantity(items:Item[]):number {
    var totalQuantity:number = 0
    for(var i = 0; i < items.length; i++){
      totalQuantity += items[i].quantity
    }
    return totalQuantity
  }



}
