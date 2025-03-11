import dayjs from 'dayjs/esm';

import { IPurchasedCar, NewPurchasedCar } from './purchased-car.model';

export const sampleWithRequiredData: IPurchasedCar = {
  id: 17349,
};

export const sampleWithPartialData: IPurchasedCar = {
  id: 97876,
};

export const sampleWithFullData: IPurchasedCar = {
  id: 57768,
  purchaseDate: dayjs('2023-12-04'),
};

export const sampleWithNewData: NewPurchasedCar = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
