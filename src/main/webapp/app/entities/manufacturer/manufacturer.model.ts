export interface IManufacturer {
  id: number;
  name?: string | null;
}

export type NewManufacturer = Omit<IManufacturer, 'id'> & { id: null };
