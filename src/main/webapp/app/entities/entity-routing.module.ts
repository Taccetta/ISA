import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'manufacturer',
        data: { pageTitle: 'carDealershipApp.manufacturer.home.title' },
        loadChildren: () => import('./manufacturer/manufacturer.module').then(m => m.ManufacturerModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'carDealershipApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'car',
        data: { pageTitle: 'carDealershipApp.car.home.title' },
        loadChildren: () => import('./car/car.module').then(m => m.CarModule),
      },
      {
        path: 'purchased-car',
        data: { pageTitle: 'carDealershipApp.purchasedCar.home.title' },
        loadChildren: () => import('./purchased-car/purchased-car.module').then(m => m.PurchasedCarModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
