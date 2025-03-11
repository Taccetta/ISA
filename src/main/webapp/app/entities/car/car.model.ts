import { IManufacturer } from 'app/entities/manufacturer/manufacturer.model';

export interface ICar {
  id: number;
  model?: string | null;
  year?: string | null;
  available?: number | null;
  manufacturer?: Pick<IManufacturer, 'id' | 'name'> | null;
}

export type NewCar = Omit<ICar, 'id'> & { id: null };
