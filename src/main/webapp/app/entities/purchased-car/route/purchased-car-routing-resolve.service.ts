import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPurchasedCar } from '../purchased-car.model';
import { PurchasedCarService } from '../service/purchased-car.service';

@Injectable({ providedIn: 'root' })
export class PurchasedCarRoutingResolveService implements Resolve<IPurchasedCar | null> {
  constructor(protected service: PurchasedCarService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPurchasedCar | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((purchasedCar: HttpResponse<IPurchasedCar>) => {
          if (purchasedCar.body) {
            return of(purchasedCar.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
