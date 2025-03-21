import { inject, Injectable } from "@angular/core";
import { Item,ItemSlice } from "./models";
import { ComponentStore } from "@ngrx/component-store";
import { RestaurantService } from "./restaurant.service";

const INIT_STATE:ItemSlice = {
    items : []
}

@Injectable({providedIn:'root'})
export class OrderStore extends ComponentStore<ItemSlice>{
    private restaurantSvc = inject(RestaurantService)

    constructor() {
        super(INIT_STATE)
    }

    readonly getItems = this.select<Item[]>(
        (store:ItemSlice) => store.items
    )

    readonly addItemToOrder = this.updater<Item>(
        (store:ItemSlice, itemToAdd:Item) => {
            return {
                items : [...store.items, itemToAdd]
            } as ItemSlice
        }
    )

    readonly deleteItemFromCart = this.updater<string>(
        (store:ItemSlice, itemToDelete:string) => {
            return {
                items: store.items.filter(i => i._id != itemToDelete)
            }
        }
    )

    readonly getTotalPrice = this.select<number>(
        (store:ItemSlice) => {
            return this.calculateOrderPrice(store.items)
        }
    )

    readonly addItems = this.updater<Item[]>(
        (store:ItemSlice, itemsToAdd:Item[]) => {
            return {
                items : []
            } as ItemSlice
        }
    )

    private calculateOrderPrice(items:Item[]){
        var totalPrice = 0
        for(var i = 0;i < items.length; i++){
            console.log(totalPrice)
            totalPrice += (items[i].price * items[i].quantity)
        }
        return totalPrice
    }
}