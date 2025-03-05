import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IManufacturer } from '../manufacturer.model';

@Component({
  selector: 'jhi-manufacturer-detail',
  templateUrl: './manufacturer-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ManufacturerDetailComponent {
  manufacturer = input<IManufacturer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
