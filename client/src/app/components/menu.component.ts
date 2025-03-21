import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Observable } from 'rxjs';
import { Item } from '../models';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{
  // TODO: Task 2

  private restaurantSvc = inject(RestaurantService)
  protected menu$ !: Observable<Item[]>

  ngOnInit():void {
    this.menu$ = this.restaurantSvc.getMenuItems()
    this.menu$.subscribe({
      next : data => console.log(data)
    })
  }

}
