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
  protected priceOfOrders!:Observable<number>
  

  ngOnInit():void {
    this.restaurantSvc.getMenuItems().subscribe({

      next: (data) => {
        this.menuWithoutQuantity = data
        this.menuWithQuantity = this.populateArray()
      }
    })

    this.orderStore.getItems.subscribe({
      next: (data) => this.orders = data
    })

    this.priceOfOrders = this.orderStore.getTotalPrice
  }

  increment(item:Item):void{
    item.quantity += 1
    this.orderStore.addItemToOrder(item)
  }

  decrement(item:Item):void{
    if(item.quantity > 0){
      item.quantity -= 1
      this.orderStore.deleteItemFromCart(item)
    } 
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
    this.router.navigate(["/confirm"])
  }



}
