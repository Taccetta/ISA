import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PurchasedCarFormService } from './purchased-car-form.service';
import { PurchasedCarService } from '../service/purchased-car.service';
import { IPurchasedCar } from '../purchased-car.model';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { PurchasedCarUpdateComponent } from './purchased-car-update.component';

describe('PurchasedCar Management Update Component', () => {
  let comp: PurchasedCarUpdateComponent;
  let fixture: ComponentFixture<PurchasedCarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let purchasedCarFormService: PurchasedCarFormService;
  let purchasedCarService: PurchasedCarService;
  let carService: CarService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PurchasedCarUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PurchasedCarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchasedCarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    purchasedCarFormService = TestBed.inject(PurchasedCarFormService);
    purchasedCarService = TestBed.inject(PurchasedCarService);
    carService = TestBed.inject(CarService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const purchasedCar: IPurchasedCar = { id: 456 };
      const car: ICar = { id: 62802 };
      purchasedCar.car = car;

      const carCollection: ICar[] = [{ id: 1506 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchasedCar });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars.map(expect.objectContaining));
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const purchasedCar: IPurchasedCar = { id: 456 };
      const client: IClient = { id: 35851 };
      purchasedCar.client = client;

      const clientCollection: IClient[] = [{ id: 50779 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchasedCar });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining)
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const purchasedCar: IPurchasedCar = { id: 456 };
      const car: ICar = { id: 10558 };
      purchasedCar.car = car;
      const client: IClient = { id: 7892 };
      purchasedCar.client = client;

      activatedRoute.data = of({ purchasedCar });
      comp.ngOnInit();

      expect(comp.carsSharedCollection).toContain(car);
      expect(comp.clientsSharedCollection).toContain(client);
      expect(comp.purchasedCar).toEqual(purchasedCar);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchasedCar>>();
      const purchasedCar = { id: 123 };
      jest.spyOn(purchasedCarFormService, 'getPurchasedCar').mockReturnValue(purchasedCar);
      jest.spyOn(purchasedCarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchasedCar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchasedCar }));
      saveSubject.complete();

      // THEN
      expect(purchasedCarFormService.getPurchasedCar).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(purchasedCarService.update).toHaveBeenCalledWith(expect.objectContaining(purchasedCar));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchasedCar>>();
      const purchasedCar = { id: 123 };
      jest.spyOn(purchasedCarFormService, 'getPurchasedCar').mockReturnValue({ id: null });
      jest.spyOn(purchasedCarService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchasedCar: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchasedCar }));
      saveSubject.complete();

      // THEN
      expect(purchasedCarFormService.getPurchasedCar).toHaveBeenCalled();
      expect(purchasedCarService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchasedCar>>();
      const purchasedCar = { id: 123 };
      jest.spyOn(purchasedCarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchasedCar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(purchasedCarService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCar', () => {
      it('Should forward to carService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(carService, 'compareCar');
        comp.compareCar(entity, entity2);
        expect(carService.compareCar).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
