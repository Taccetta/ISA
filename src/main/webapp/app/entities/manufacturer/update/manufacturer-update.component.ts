import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IManufacturer } from '../manufacturer.model';
import { ManufacturerService } from '../service/manufacturer.service';
import { ManufacturerFormGroup, ManufacturerFormService } from './manufacturer-form.service';

@Component({
  selector: 'jhi-manufacturer-update',
  templateUrl: './manufacturer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ManufacturerUpdateComponent implements OnInit {
  isSaving = false;
  manufacturer: IManufacturer | null = null;

  protected manufacturerService = inject(ManufacturerService);
  protected manufacturerFormService = inject(ManufacturerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ManufacturerFormGroup = this.manufacturerFormService.createManufacturerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manufacturer }) => {
      this.manufacturer = manufacturer;
      if (manufacturer) {
        this.updateForm(manufacturer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manufacturer = this.manufacturerFormService.getManufacturer(this.editForm);
    if (manufacturer.id !== null) {
      this.subscribeToSaveResponse(this.manufacturerService.update(manufacturer));
    } else {
      this.subscribeToSaveResponse(this.manufacturerService.create(manufacturer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManufacturer>>): void {
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

  protected updateForm(manufacturer: IManufacturer): void {
    this.manufacturer = manufacturer;
    this.manufacturerFormService.resetForm(this.editForm, manufacturer);
  }
}
