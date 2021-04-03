import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClientDetails, ClientDetails } from '../client-details.model';
import { ClientDetailsService } from '../service/client-details.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-client-details-update',
  templateUrl: './client-details-update.component.html',
})
export class ClientDetailsUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    kominyDymowe: [],
    kominySpalinowe: [],
    przewodyWentylacyjne: [],
    ryczaltJednorazowy: [],
    dataCzyszczenia: [],
    dzienCzyszczenia: [],
    zaplacono: [],
    podpis: [],
    client: [],
  });

  constructor(
    protected clientDetailsService: ClientDetailsService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clientDetails }) => {
      this.updateForm(clientDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clientDetails = this.createFromForm();
    if (clientDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.clientDetailsService.update(clientDetails));
    } else {
      this.subscribeToSaveResponse(this.clientDetailsService.create(clientDetails));
    }
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClientDetails>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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

  protected updateForm(clientDetails: IClientDetails): void {
    this.editForm.patchValue({
      id: clientDetails.id,
      kominyDymowe: clientDetails.kominyDymowe,
      kominySpalinowe: clientDetails.kominySpalinowe,
      przewodyWentylacyjne: clientDetails.przewodyWentylacyjne,
      ryczaltJednorazowy: clientDetails.ryczaltJednorazowy,
      dataCzyszczenia: clientDetails.dataCzyszczenia,
      dzienCzyszczenia: clientDetails.dzienCzyszczenia,
      zaplacono: clientDetails.zaplacono,
      podpis: clientDetails.podpis,
      client: clientDetails.client,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, clientDetails.client);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): IClientDetails {
    return {
      ...new ClientDetails(),
      id: this.editForm.get(['id'])!.value,
      kominyDymowe: this.editForm.get(['kominyDymowe'])!.value,
      kominySpalinowe: this.editForm.get(['kominySpalinowe'])!.value,
      przewodyWentylacyjne: this.editForm.get(['przewodyWentylacyjne'])!.value,
      ryczaltJednorazowy: this.editForm.get(['ryczaltJednorazowy'])!.value,
      dataCzyszczenia: this.editForm.get(['dataCzyszczenia'])!.value,
      dzienCzyszczenia: this.editForm.get(['dzienCzyszczenia'])!.value,
      zaplacono: this.editForm.get(['zaplacono'])!.value,
      podpis: this.editForm.get(['podpis'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
