import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PurchasedCarDetailComponent } from './purchased-car-detail.component';

describe('PurchasedCar Management Detail Component', () => {
  let comp: PurchasedCarDetailComponent;
  let fixture: ComponentFixture<PurchasedCarDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PurchasedCarDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ purchasedCar: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PurchasedCarDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PurchasedCarDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load purchasedCar on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.purchasedCar).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
