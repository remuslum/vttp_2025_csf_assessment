// You may use this file to create any models
export interface Item {
    _id:string
    name:string
    description:string
    price:number
    quantity:number
}

export interface ItemSlice {
    items : Item[]
}

export interface Order {
    username:string
    password:string
    items:Item[]
}