import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IPurchasedCar } from '../purchased-car.model';

@Component({
  selector: 'jhi-purchased-car-detail',
  templateUrl: './purchased-car-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class PurchasedCarDetailComponent {
  purchasedCar = input<IPurchasedCar | null>(null);

  previousState(): void {
    window.history.back();
  }
}
