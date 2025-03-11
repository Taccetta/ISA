import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchasedCar } from '../purchased-car.model';
import { PurchasedCarService } from '../service/purchased-car.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './purchased-car-delete-dialog.component.html',
})
export class PurchasedCarDeleteDialogComponent {
  purchasedCar?: IPurchasedCar;

  constructor(protected purchasedCarService: PurchasedCarService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.purchasedCarService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
