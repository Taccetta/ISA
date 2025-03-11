import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CarFormService } from './car-form.service';
import { CarService } from '../service/car.service';
import { ICar } from '../car.model';
import { IManufacturer } from 'app/entities/manufacturer/manufacturer.model';
import { ManufacturerService } from 'app/entities/manufacturer/service/manufacturer.service';

import { CarUpdateComponent } from './car-update.component';

describe('Car Management Update Component', () => {
  let comp: CarUpdateComponent;
  let fixture: ComponentFixture<CarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let carFormService: CarFormService;
  let carService: CarService;
  let manufacturerService: ManufacturerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CarUpdateComponent],
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
      .overrideTemplate(CarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    carFormService = TestBed.inject(CarFormService);
    carService = TestBed.inject(CarService);
    manufacturerService = TestBed.inject(ManufacturerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Manufacturer query and add missing value', () => {
      const car: ICar = { id: 456 };
      const manufacturer: IManufacturer = { id: 6897 };
      car.manufacturer = manufacturer;

      const manufacturerCollection: IManufacturer[] = [{ id: 97852 }];
      jest.spyOn(manufacturerService, 'query').mockReturnValue(of(new HttpResponse({ body: manufacturerCollection })));
      const additionalManufacturers = [manufacturer];
      const expectedCollection: IManufacturer[] = [...additionalManufacturers, ...manufacturerCollection];
      jest.spyOn(manufacturerService, 'addManufacturerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(manufacturerService.query).toHaveBeenCalled();
      expect(manufacturerService.addManufacturerToCollectionIfMissing).toHaveBeenCalledWith(
        manufacturerCollection,
        ...additionalManufacturers.map(expect.objectContaining)
      );
      expect(comp.manufacturersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const car: ICar = { id: 456 };
      const manufacturer: IManufacturer = { id: 70056 };
      car.manufacturer = manufacturer;

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(comp.manufacturersSharedCollection).toContain(manufacturer);
      expect(comp.car).toEqual(car);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICar>>();
      const car = { id: 123 };
      jest.spyOn(carFormService, 'getCar').mockReturnValue(car);
      jest.spyOn(carService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: car }));
      saveSubject.complete();

      // THEN
      expect(carFormService.getCar).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(carService.update).toHaveBeenCalledWith(expect.objectContaining(car));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICar>>();
      const car = { id: 123 };
      jest.spyOn(carFormService, 'getCar').mockReturnValue({ id: null });
      jest.spyOn(carService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: car }));
      saveSubject.complete();

      // THEN
      expect(carFormService.getCar).toHaveBeenCalled();
      expect(carService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICar>>();
      const car = { id: 123 };
      jest.spyOn(carService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(carService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareManufacturer', () => {
      it('Should forward to manufacturerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(manufacturerService, 'compareManufacturer');
        comp.compareManufacturer(entity, entity2);
        expect(manufacturerService.compareManufacturer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
