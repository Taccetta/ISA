import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PurchasedCarFormService, PurchasedCarFormGroup } from './purchased-car-form.service';
import { IPurchasedCar } from '../purchased-car.model';
import { PurchasedCarService } from '../service/purchased-car.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-purchased-car-update',
  templateUrl: './purchased-car-update.component.html',
})
export class PurchasedCarUpdateComponent implements OnInit {
  isSaving = false;
  purchasedCar: IPurchasedCar | null = null;

  carsSharedCollection: ICar[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm: PurchasedCarFormGroup = this.purchasedCarFormService.createPurchasedCarFormGroup();

  constructor(
    protected purchasedCarService: PurchasedCarService,
    protected purchasedCarFormService: PurchasedCarFormService,
    protected carService: CarService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCar = (o1: ICar | null, o2: ICar | null): boolean => this.carService.compareCar(o1, o2);

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchasedCar }) => {
      this.purchasedCar = purchasedCar;
      if (purchasedCar) {
        this.updateForm(purchasedCar);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchasedCar = this.purchasedCarFormService.getPurchasedCar(this.editForm);
    if (purchasedCar.id !== null) {
      this.subscribeToSaveResponse(this.purchasedCarService.update(purchasedCar));
    } else {
      this.subscribeToSaveResponse(this.purchasedCarService.create(purchasedCar));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchasedCar>>): void {
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

  protected updateForm(purchasedCar: IPurchasedCar): void {
    this.purchasedCar = purchasedCar;
    this.purchasedCarFormService.resetForm(this.editForm, purchasedCar);

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing<ICar>(this.carsSharedCollection, purchasedCar.car);
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(
      this.clientsSharedCollection,
      purchasedCar.client
    );
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing<ICar>(cars, this.purchasedCar?.car)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.purchasedCar?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }
}
