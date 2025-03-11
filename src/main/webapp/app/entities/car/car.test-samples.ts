import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 96848,
  model: 'local National Account',
  year: 'Loan',
  available: 42812,
};

export const sampleWithPartialData: ICar = {
  id: 30253,
  model: 'web-enabled fuchsia',
  year: 'Soft payment',
  available: 18809,
};

export const sampleWithFullData: ICar = {
  id: 37075,
  model: 'Taiwan IB',
  year: 'content',
  available: 43567,
};

export const sampleWithNewData: NewCar = {
  model: 'Divide infrastructure Loan',
  year: 'National',
  available: 88813,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
