import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPurchasedCar, NewPurchasedCar } from '../purchased-car.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPurchasedCar for edit and NewPurchasedCarFormGroupInput for create.
 */
type PurchasedCarFormGroupInput = IPurchasedCar | PartialWithRequiredKeyOf<NewPurchasedCar>;

type PurchasedCarFormDefaults = Pick<NewPurchasedCar, 'id'>;

type PurchasedCarFormGroupContent = {
  id: FormControl<IPurchasedCar['id'] | NewPurchasedCar['id']>;
  purchaseDate: FormControl<IPurchasedCar['purchaseDate']>;
  car: FormControl<IPurchasedCar['car']>;
  client: FormControl<IPurchasedCar['client']>;
};

export type PurchasedCarFormGroup = FormGroup<PurchasedCarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PurchasedCarFormService {
  createPurchasedCarFormGroup(purchasedCar: PurchasedCarFormGroupInput = { id: null }): PurchasedCarFormGroup {
    const purchasedCarRawValue = {
      ...this.getFormDefaults(),
      ...purchasedCar,
    };
    return new FormGroup<PurchasedCarFormGroupContent>({
      id: new FormControl(
        { value: purchasedCarRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      purchaseDate: new FormControl(purchasedCarRawValue.purchaseDate),
      car: new FormControl(purchasedCarRawValue.car),
      client: new FormControl(purchasedCarRawValue.client),
    });
  }

  getPurchasedCar(form: PurchasedCarFormGroup): IPurchasedCar | NewPurchasedCar {
    return form.getRawValue() as IPurchasedCar | NewPurchasedCar;
  }

  resetForm(form: PurchasedCarFormGroup, purchasedCar: PurchasedCarFormGroupInput): void {
    const purchasedCarRawValue = { ...this.getFormDefaults(), ...purchasedCar };
    form.reset(
      {
        ...purchasedCarRawValue,
        id: { value: purchasedCarRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PurchasedCarFormDefaults {
    return {
      id: null,
    };
  }
}
