import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPurchasedCar } from '../purchased-car.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../purchased-car.test-samples';

import { PurchasedCarService, RestPurchasedCar } from './purchased-car.service';

const requireRestSample: RestPurchasedCar = {
  ...sampleWithRequiredData,
  purchaseDate: sampleWithRequiredData.purchaseDate?.format(DATE_FORMAT),
};

describe('PurchasedCar Service', () => {
  let service: PurchasedCarService;
  let httpMock: HttpTestingController;
  let expectedResult: IPurchasedCar | IPurchasedCar[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PurchasedCarService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PurchasedCar', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const purchasedCar = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(purchasedCar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PurchasedCar', () => {
      const purchasedCar = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(purchasedCar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PurchasedCar', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PurchasedCar', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PurchasedCar', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPurchasedCarToCollectionIfMissing', () => {
      it('should add a PurchasedCar to an empty array', () => {
        const purchasedCar: IPurchasedCar = sampleWithRequiredData;
        expectedResult = service.addPurchasedCarToCollectionIfMissing([], purchasedCar);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchasedCar);
      });

      it('should not add a PurchasedCar to an array that contains it', () => {
        const purchasedCar: IPurchasedCar = sampleWithRequiredData;
        const purchasedCarCollection: IPurchasedCar[] = [
          {
            ...purchasedCar,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPurchasedCarToCollectionIfMissing(purchasedCarCollection, purchasedCar);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PurchasedCar to an array that doesn't contain it", () => {
        const purchasedCar: IPurchasedCar = sampleWithRequiredData;
        const purchasedCarCollection: IPurchasedCar[] = [sampleWithPartialData];
        expectedResult = service.addPurchasedCarToCollectionIfMissing(purchasedCarCollection, purchasedCar);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchasedCar);
      });

      it('should add only unique PurchasedCar to an array', () => {
        const purchasedCarArray: IPurchasedCar[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const purchasedCarCollection: IPurchasedCar[] = [sampleWithRequiredData];
        expectedResult = service.addPurchasedCarToCollectionIfMissing(purchasedCarCollection, ...purchasedCarArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const purchasedCar: IPurchasedCar = sampleWithRequiredData;
        const purchasedCar2: IPurchasedCar = sampleWithPartialData;
        expectedResult = service.addPurchasedCarToCollectionIfMissing([], purchasedCar, purchasedCar2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchasedCar);
        expect(expectedResult).toContain(purchasedCar2);
      });

      it('should accept null and undefined values', () => {
        const purchasedCar: IPurchasedCar = sampleWithRequiredData;
        expectedResult = service.addPurchasedCarToCollectionIfMissing([], null, purchasedCar, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchasedCar);
      });

      it('should return initial array if no PurchasedCar is added', () => {
        const purchasedCarCollection: IPurchasedCar[] = [sampleWithRequiredData];
        expectedResult = service.addPurchasedCarToCollectionIfMissing(purchasedCarCollection, undefined, null);
        expect(expectedResult).toEqual(purchasedCarCollection);
      });
    });

    describe('comparePurchasedCar', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePurchasedCar(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePurchasedCar(entity1, entity2);
        const compareResult2 = service.comparePurchasedCar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePurchasedCar(entity1, entity2);
        const compareResult2 = service.comparePurchasedCar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePurchasedCar(entity1, entity2);
        const compareResult2 = service.comparePurchasedCar(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
