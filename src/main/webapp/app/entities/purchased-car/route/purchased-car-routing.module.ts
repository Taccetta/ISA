import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PurchasedCarComponent } from '../list/purchased-car.component';
import { PurchasedCarDetailComponent } from '../detail/purchased-car-detail.component';
import { PurchasedCarUpdateComponent } from '../update/purchased-car-update.component';
import { PurchasedCarRoutingResolveService } from './purchased-car-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const purchasedCarRoute: Routes = [
  {
    path: '',
    component: PurchasedCarComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PurchasedCarDetailComponent,
    resolve: {
      purchasedCar: PurchasedCarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PurchasedCarUpdateComponent,
    resolve: {
      purchasedCar: PurchasedCarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PurchasedCarUpdateComponent,
    resolve: {
      purchasedCar: PurchasedCarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(purchasedCarRoute)],
  exports: [RouterModule],
})
export class PurchasedCarRoutingModule {}
