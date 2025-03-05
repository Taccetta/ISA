import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPurchasedCar } from '../purchased-car.model';
import { PurchasedCarService } from '../service/purchased-car.service';

const purchasedCarResolve = (route: ActivatedRouteSnapshot): Observable<null | IPurchasedCar> => {
  const id = route.params.id;
  if (id) {
    return inject(PurchasedCarService)
      .find(id)
      .pipe(
        mergeMap((purchasedCar: HttpResponse<IPurchasedCar>) => {
          if (purchasedCar.body) {
            return of(purchasedCar.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default purchasedCarResolve;
