import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PurchasedCarResolve from './route/purchased-car-routing-resolve.service';

const purchasedCarRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/purchased-car.component').then(m => m.PurchasedCarComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/purchased-car-detail.component').then(m => m.PurchasedCarDetailComponent),
    resolve: {
      purchasedCar: PurchasedCarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/purchased-car-update.component').then(m => m.PurchasedCarUpdateComponent),
    resolve: {
      purchasedCar: PurchasedCarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/purchased-car-update.component').then(m => m.PurchasedCarUpdateComponent),
    resolve: {
      purchasedCar: PurchasedCarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default purchasedCarRoute;
