import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClientDetails } from '../client-details.model';
import { ClientDetailsService } from '../service/client-details.service';
import { ClientDetailsDeleteDialogComponent } from '../delete/client-details-delete-dialog.component';

@Component({
  selector: 'jhi-client-details',
  templateUrl: './client-details.component.html',
})
export class ClientDetailsComponent implements OnInit {
  clientDetails?: IClientDetails[];
  isLoading = false;

  constructor(protected clientDetailsService: ClientDetailsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.clientDetailsService.query().subscribe(
      (res: HttpResponse<IClientDetails[]>) => {
        this.isLoading = false;
        this.clientDetails = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IClientDetails): number {
    return item.id!;
  }

  delete(clientDetails: IClientDetails): void {
    const modalRef = this.modalService.open(ClientDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.clientDetails = clientDetails;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
