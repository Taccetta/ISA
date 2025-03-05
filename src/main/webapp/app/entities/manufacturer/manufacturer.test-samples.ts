import { IManufacturer, NewManufacturer } from './manufacturer.model';

export const sampleWithRequiredData: IManufacturer = {
  id: 14753,
  name: 'lest vary',
};

export const sampleWithPartialData: IManufacturer = {
  id: 1917,
  name: 'pupil',
};

export const sampleWithFullData: IManufacturer = {
  id: 3071,
  name: 'however till blah',
};

export const sampleWithNewData: NewManufacturer = {
  name: 'incidentally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
