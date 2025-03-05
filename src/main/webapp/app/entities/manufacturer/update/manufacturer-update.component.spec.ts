import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ManufacturerService } from '../service/manufacturer.service';
import { IManufacturer } from '../manufacturer.model';
import { ManufacturerFormService } from './manufacturer-form.service';

import { ManufacturerUpdateComponent } from './manufacturer-update.component';

describe('Manufacturer Management Update Component', () => {
  let comp: ManufacturerUpdateComponent;
  let fixture: ComponentFixture<ManufacturerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let manufacturerFormService: ManufacturerFormService;
  let manufacturerService: ManufacturerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ManufacturerUpdateComponent],
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
      .overrideTemplate(ManufacturerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManufacturerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    manufacturerFormService = TestBed.inject(ManufacturerFormService);
    manufacturerService = TestBed.inject(ManufacturerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const manufacturer: IManufacturer = { id: 13084 };

      activatedRoute.data = of({ manufacturer });
      comp.ngOnInit();

      expect(comp.manufacturer).toEqual(manufacturer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManufacturer>>();
      const manufacturer = { id: 7851 };
      jest.spyOn(manufacturerFormService, 'getManufacturer').mockReturnValue(manufacturer);
      jest.spyOn(manufacturerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manufacturer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manufacturer }));
      saveSubject.complete();

      // THEN
      expect(manufacturerFormService.getManufacturer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(manufacturerService.update).toHaveBeenCalledWith(expect.objectContaining(manufacturer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManufacturer>>();
      const manufacturer = { id: 7851 };
      jest.spyOn(manufacturerFormService, 'getManufacturer').mockReturnValue({ id: null });
      jest.spyOn(manufacturerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manufacturer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manufacturer }));
      saveSubject.complete();

      // THEN
      expect(manufacturerFormService.getManufacturer).toHaveBeenCalled();
      expect(manufacturerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManufacturer>>();
      const manufacturer = { id: 7851 };
      jest.spyOn(manufacturerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manufacturer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(manufacturerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
