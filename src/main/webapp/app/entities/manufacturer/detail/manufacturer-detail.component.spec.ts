import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ManufacturerDetailComponent } from './manufacturer-detail.component';

describe('Manufacturer Management Detail Component', () => {
  let comp: ManufacturerDetailComponent;
  let fixture: ComponentFixture<ManufacturerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManufacturerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./manufacturer-detail.component').then(m => m.ManufacturerDetailComponent),
              resolve: { manufacturer: () => of({ id: 7851 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ManufacturerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManufacturerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load manufacturer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ManufacturerDetailComponent);

      // THEN
      expect(instance.manufacturer()).toEqual(expect.objectContaining({ id: 7851 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
