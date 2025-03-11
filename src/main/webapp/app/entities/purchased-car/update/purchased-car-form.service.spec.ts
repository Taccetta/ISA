import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../purchased-car.test-samples';

import { PurchasedCarFormService } from './purchased-car-form.service';

describe('PurchasedCar Form Service', () => {
  let service: PurchasedCarFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PurchasedCarFormService);
  });

  describe('Service methods', () => {
    describe('createPurchasedCarFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPurchasedCarFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            purchaseDate: expect.any(Object),
            car: expect.any(Object),
            client: expect.any(Object),
          })
        );
      });

      it('passing IPurchasedCar should create a new form with FormGroup', () => {
        const formGroup = service.createPurchasedCarFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            purchaseDate: expect.any(Object),
            car: expect.any(Object),
            client: expect.any(Object),
          })
        );
      });
    });

    describe('getPurchasedCar', () => {
      it('should return NewPurchasedCar for default PurchasedCar initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPurchasedCarFormGroup(sampleWithNewData);

        const purchasedCar = service.getPurchasedCar(formGroup) as any;

        expect(purchasedCar).toMatchObject(sampleWithNewData);
      });

      it('should return NewPurchasedCar for empty PurchasedCar initial value', () => {
        const formGroup = service.createPurchasedCarFormGroup();

        const purchasedCar = service.getPurchasedCar(formGroup) as any;

        expect(purchasedCar).toMatchObject({});
      });

      it('should return IPurchasedCar', () => {
        const formGroup = service.createPurchasedCarFormGroup(sampleWithRequiredData);

        const purchasedCar = service.getPurchasedCar(formGroup) as any;

        expect(purchasedCar).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPurchasedCar should not enable id FormControl', () => {
        const formGroup = service.createPurchasedCarFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPurchasedCar should disable id FormControl', () => {
        const formGroup = service.createPurchasedCarFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
