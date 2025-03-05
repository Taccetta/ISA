import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IManufacturer } from 'app/entities/manufacturer/manufacturer.model';
import { ManufacturerService } from 'app/entities/manufacturer/service/manufacturer.service';
import { ICar } from '../car.model';
import { CarService } from '../service/car.service';
import { CarFormGroup, CarFormService } from './car-form.service';

@Component({
  selector: 'jhi-car-update',
  templateUrl: './car-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CarUpdateComponent implements OnInit {
  isSaving = false;
  car: ICar | null = null;

  manufacturersCollection: IManufacturer[] = [];

  protected carService = inject(CarService);
  protected carFormService = inject(CarFormService);
  protected manufacturerService = inject(ManufacturerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CarFormGroup = this.carFormService.createCarFormGroup();

  compareManufacturer = (o1: IManufacturer | null, o2: IManufacturer | null): boolean =>
    this.manufacturerService.compareManufacturer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ car }) => {
      this.car = car;
      if (car) {
        this.updateForm(car);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const car = this.carFormService.getCar(this.editForm);
    if (car.id !== null) {
      this.subscribeToSaveResponse(this.carService.update(car));
    } else {
      this.subscribeToSaveResponse(this.carService.create(car));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICar>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(car: ICar): void {
    this.car = car;
    this.carFormService.resetForm(this.editForm, car);

    this.manufacturersCollection = this.manufacturerService.addManufacturerToCollectionIfMissing<IManufacturer>(
      this.manufacturersCollection,
      car.manufacturer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.manufacturerService
      .query({ 'carId.specified': 'false' })
      .pipe(map((res: HttpResponse<IManufacturer[]>) => res.body ?? []))
      .pipe(
        map((manufacturers: IManufacturer[]) =>
          this.manufacturerService.addManufacturerToCollectionIfMissing<IManufacturer>(manufacturers, this.car?.manufacturer),
        ),
      )
      .subscribe((manufacturers: IManufacturer[]) => (this.manufacturersCollection = manufacturers));
  }
}
