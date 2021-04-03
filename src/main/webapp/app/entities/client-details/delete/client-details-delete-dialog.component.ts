import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClientDetails } from '../client-details.model';
import { ClientDetailsService } from '../service/client-details.service';

@Component({
  templateUrl: './client-details-delete-dialog.component.html',
})
export class ClientDetailsDeleteDialogComponent {
  clientDetails?: IClientDetails;

  constructor(protected clientDetailsService: ClientDetailsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clientDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
