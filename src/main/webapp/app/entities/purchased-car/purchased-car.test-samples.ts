import dayjs from 'dayjs/esm';

import { IPurchasedCar, NewPurchasedCar } from './purchased-car.model';

export const sampleWithRequiredData: IPurchasedCar = {
  id: 26848,
};

export const sampleWithPartialData: IPurchasedCar = {
  id: 27918,
};

export const sampleWithFullData: IPurchasedCar = {
  id: 11377,
  purchaseDate: dayjs('2025-03-02'),
};

export const sampleWithNewData: NewPurchasedCar = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
