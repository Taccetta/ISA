import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchasedCar } from '../purchased-car.model';

@Component({
  selector: 'jhi-purchased-car-detail',
  templateUrl: './purchased-car-detail.component.html',
})
export class PurchasedCarDetailComponent implements OnInit {
  purchasedCar: IPurchasedCar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchasedCar }) => {
      this.purchasedCar = purchasedCar;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
