import dayjs from 'dayjs/esm';
import { ICar } from 'app/entities/car/car.model';
import { IClient } from 'app/entities/client/client.model';

export interface IPurchasedCar {
  id: number;
  purchaseDate?: dayjs.Dayjs | null;
  car?: Pick<ICar, 'id' | 'model'> | null;
  client?: Pick<IClient, 'id' | 'email'> | null;
}

export type NewPurchasedCar = Omit<IPurchasedCar, 'id'> & { id: null };
