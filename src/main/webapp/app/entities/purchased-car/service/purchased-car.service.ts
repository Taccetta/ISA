import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchasedCar, NewPurchasedCar } from '../purchased-car.model';

export type PartialUpdatePurchasedCar = Partial<IPurchasedCar> & Pick<IPurchasedCar, 'id'>;

type RestOf<T extends IPurchasedCar | NewPurchasedCar> = Omit<T, 'purchaseDate'> & {
  purchaseDate?: string | null;
};

export type RestPurchasedCar = RestOf<IPurchasedCar>;

export type NewRestPurchasedCar = RestOf<NewPurchasedCar>;

export type PartialUpdateRestPurchasedCar = RestOf<PartialUpdatePurchasedCar>;

export type EntityResponseType = HttpResponse<IPurchasedCar>;
export type EntityArrayResponseType = HttpResponse<IPurchasedCar[]>;

@Injectable({ providedIn: 'root' })
export class PurchasedCarService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchased-cars');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchasedCar: NewPurchasedCar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchasedCar);
    return this.http
      .post<RestPurchasedCar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(purchasedCar: IPurchasedCar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchasedCar);
    return this.http
      .put<RestPurchasedCar>(`${this.resourceUrl}/${this.getPurchasedCarIdentifier(purchasedCar)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(purchasedCar: PartialUpdatePurchasedCar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchasedCar);
    return this.http
      .patch<RestPurchasedCar>(`${this.resourceUrl}/${this.getPurchasedCarIdentifier(purchasedCar)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPurchasedCar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPurchasedCar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPurchasedCarIdentifier(purchasedCar: Pick<IPurchasedCar, 'id'>): number {
    return purchasedCar.id;
  }

  comparePurchasedCar(o1: Pick<IPurchasedCar, 'id'> | null, o2: Pick<IPurchasedCar, 'id'> | null): boolean {
    return o1 && o2 ? this.getPurchasedCarIdentifier(o1) === this.getPurchasedCarIdentifier(o2) : o1 === o2;
  }

  addPurchasedCarToCollectionIfMissing<Type extends Pick<IPurchasedCar, 'id'>>(
    purchasedCarCollection: Type[],
    ...purchasedCarsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const purchasedCars: Type[] = purchasedCarsToCheck.filter(isPresent);
    if (purchasedCars.length > 0) {
      const purchasedCarCollectionIdentifiers = purchasedCarCollection.map(
        purchasedCarItem => this.getPurchasedCarIdentifier(purchasedCarItem)!
      );
      const purchasedCarsToAdd = purchasedCars.filter(purchasedCarItem => {
        const purchasedCarIdentifier = this.getPurchasedCarIdentifier(purchasedCarItem);
        if (purchasedCarCollectionIdentifiers.includes(purchasedCarIdentifier)) {
          return false;
        }
        purchasedCarCollectionIdentifiers.push(purchasedCarIdentifier);
        return true;
      });
      return [...purchasedCarsToAdd, ...purchasedCarCollection];
    }
    return purchasedCarCollection;
  }

  protected convertDateFromClient<T extends IPurchasedCar | NewPurchasedCar | PartialUpdatePurchasedCar>(purchasedCar: T): RestOf<T> {
    return {
      ...purchasedCar,
      purchaseDate: purchasedCar.purchaseDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPurchasedCar: RestPurchasedCar): IPurchasedCar {
    return {
      ...restPurchasedCar,
      purchaseDate: restPurchasedCar.purchaseDate ? dayjs(restPurchasedCar.purchaseDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPurchasedCar>): HttpResponse<IPurchasedCar> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPurchasedCar[]>): HttpResponse<IPurchasedCar[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
