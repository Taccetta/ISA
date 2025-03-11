import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PurchasedCarComponent } from './list/purchased-car.component';
import { PurchasedCarDetailComponent } from './detail/purchased-car-detail.component';
import { PurchasedCarUpdateComponent } from './update/purchased-car-update.component';
import { PurchasedCarDeleteDialogComponent } from './delete/purchased-car-delete-dialog.component';
import { PurchasedCarRoutingModule } from './route/purchased-car-routing.module';

@NgModule({
  imports: [SharedModule, PurchasedCarRoutingModule],
  declarations: [PurchasedCarComponent, PurchasedCarDetailComponent, PurchasedCarUpdateComponent, PurchasedCarDeleteDialogComponent],
})
export class PurchasedCarModule {}
