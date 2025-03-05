import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 4218,
  model: 'dim yuck splendid',
  year: 'sans integer contrast',
  available: 5068,
};

export const sampleWithPartialData: ICar = {
  id: 19509,
  model: 'supportive',
  year: 'astride',
  available: 10718,
};

export const sampleWithFullData: ICar = {
  id: 22796,
  model: 'testimonial yahoo a',
  year: 'even parody',
  available: 12743,
};

export const sampleWithNewData: NewCar = {
  model: 'ick',
  year: 'consequently till ruin',
  available: 5450,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
