import { Injectable } from "@angular/core";
import { Item,ItemSlice } from "./models";
import { ComponentStore } from "@ngrx/component-store";

const INIT_STATE:ItemSlice = {
    items : []
}

@Injectable({providedIn:'root'})
export class OrderStore extends ComponentStore<ItemSlice>{
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

    readonly deleteItemFromCart = this.updater<Item>(
        (store:ItemSlice, itemToDelete:Item) => {
            return {
                items: this.removeOneOnly(store.items, itemToDelete)
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

    private removeOneOnly(items:Item[], item:Item):Item[]{
        const indexOfItem = items.indexOf(item)
        if(indexOfItem > -1){
            items.splice(indexOfItem, 1)
        }
        return items
    }

    private calculateOrderPrice(items:Item[]){
        var totalPrice = 0
        for(var i = 0;i < items.length; i++){
            totalPrice += (items[i].price * items[i].quantity)
        }
        return totalPrice
    }
}