import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Item } from "./models";

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {

  private httpClient = inject(HttpClient)
  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems():Observable<Item[]> {
    return this.httpClient.get<Item[]>("/api/menu")
  }

  // TODO: Task 3.2
}
