import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IPurchasedCar } from '../purchased-car.model';
import { PurchasedCarService } from '../service/purchased-car.service';
import { PurchasedCarFormService } from './purchased-car-form.service';

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
      imports: [PurchasedCarUpdateComponent],
      providers: [
        provideHttpClient(),
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
    it('Should call car query and add missing value', () => {
      const purchasedCar: IPurchasedCar = { id: 28655 };
      const car: ICar = { id: 30624 };
      purchasedCar.car = car;

      const carCollection: ICar[] = [{ id: 30624 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const expectedCollection: ICar[] = [car, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchasedCar });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, car);
      expect(comp.carsCollection).toEqual(expectedCollection);
    });

    it('Should call client query and add missing value', () => {
      const purchasedCar: IPurchasedCar = { id: 28655 };
      const client: IClient = { id: 26282 };
      purchasedCar.client = client;

      const clientCollection: IClient[] = [{ id: 26282 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const expectedCollection: IClient[] = [client, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchasedCar });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, client);
      expect(comp.clientsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const purchasedCar: IPurchasedCar = { id: 28655 };
      const car: ICar = { id: 30624 };
      purchasedCar.car = car;
      const client: IClient = { id: 26282 };
      purchasedCar.client = client;

      activatedRoute.data = of({ purchasedCar });
      comp.ngOnInit();

      expect(comp.carsCollection).toContainEqual(car);
      expect(comp.clientsCollection).toContainEqual(client);
      expect(comp.purchasedCar).toEqual(purchasedCar);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchasedCar>>();
      const purchasedCar = { id: 21385 };
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
      const purchasedCar = { id: 21385 };
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
      const purchasedCar = { id: 21385 };
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
        const entity = { id: 30624 };
        const entity2 = { id: 14019 };
        jest.spyOn(carService, 'compareCar');
        comp.compareCar(entity, entity2);
        expect(carService.compareCar).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 26282 };
        const entity2 = { id: 16836 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
