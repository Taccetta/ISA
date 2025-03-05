import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'carDealershipApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'manufacturer',
    data: { pageTitle: 'carDealershipApp.manufacturer.home.title' },
    loadChildren: () => import('./manufacturer/manufacturer.routes'),
  },
  {
    path: 'client',
    data: { pageTitle: 'carDealershipApp.client.home.title' },
    loadChildren: () => import('./client/client.routes'),
  },
  {
    path: 'car',
    data: { pageTitle: 'carDealershipApp.car.home.title' },
    loadChildren: () => import('./car/car.routes'),
  },
  {
    path: 'purchased-car',
    data: { pageTitle: 'carDealershipApp.purchasedCar.home.title' },
    loadChildren: () => import('./purchased-car/purchased-car.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
