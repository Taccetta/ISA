import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ManufacturerResolve from './route/manufacturer-routing-resolve.service';

const manufacturerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/manufacturer.component').then(m => m.ManufacturerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/manufacturer-detail.component').then(m => m.ManufacturerDetailComponent),
    resolve: {
      manufacturer: ManufacturerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/manufacturer-update.component').then(m => m.ManufacturerUpdateComponent),
    resolve: {
      manufacturer: ManufacturerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/manufacturer-update.component').then(m => m.ManufacturerUpdateComponent),
    resolve: {
      manufacturer: ManufacturerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default manufacturerRoute;
