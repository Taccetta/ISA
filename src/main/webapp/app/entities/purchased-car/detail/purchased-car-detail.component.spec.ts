import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PurchasedCarDetailComponent } from './purchased-car-detail.component';

describe('PurchasedCar Management Detail Component', () => {
  let comp: PurchasedCarDetailComponent;
  let fixture: ComponentFixture<PurchasedCarDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PurchasedCarDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./purchased-car-detail.component').then(m => m.PurchasedCarDetailComponent),
              resolve: { purchasedCar: () => of({ id: 21385 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PurchasedCarDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PurchasedCarDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load purchasedCar on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PurchasedCarDetailComponent);

      // THEN
      expect(instance.purchasedCar()).toEqual(expect.objectContaining({ id: 21385 }));
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
