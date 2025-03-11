import { IManufacturer, NewManufacturer } from './manufacturer.model';

export const sampleWithRequiredData: IManufacturer = {
  id: 6644,
  name: 'GB Paradigm generation',
};

export const sampleWithPartialData: IManufacturer = {
  id: 95404,
  name: 'parse Indiana Associate',
};

export const sampleWithFullData: IManufacturer = {
  id: 28036,
  name: 'structure Turkey',
};

export const sampleWithNewData: NewManufacturer = {
  name: '24/7 Focused',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
