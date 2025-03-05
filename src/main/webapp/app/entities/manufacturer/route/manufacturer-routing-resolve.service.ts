import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManufacturer } from '../manufacturer.model';
import { ManufacturerService } from '../service/manufacturer.service';

const manufacturerResolve = (route: ActivatedRouteSnapshot): Observable<null | IManufacturer> => {
  const id = route.params.id;
  if (id) {
    return inject(ManufacturerService)
      .find(id)
      .pipe(
        mergeMap((manufacturer: HttpResponse<IManufacturer>) => {
          if (manufacturer.body) {
            return of(manufacturer.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default manufacturerResolve;
