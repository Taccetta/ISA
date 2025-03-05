import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IManufacturer } from '../manufacturer.model';
import { ManufacturerService } from '../service/manufacturer.service';

@Component({
  templateUrl: './manufacturer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ManufacturerDeleteDialogComponent {
  manufacturer?: IManufacturer;

  protected manufacturerService = inject(ManufacturerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manufacturerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
