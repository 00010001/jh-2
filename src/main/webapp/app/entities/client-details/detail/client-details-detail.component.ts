import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClientDetails } from '../client-details.model';

@Component({
  selector: 'jhi-client-details-detail',
  templateUrl: './client-details-detail.component.html',
})
export class ClientDetailsDetailComponent implements OnInit {
  clientDetails: IClientDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clientDetails }) => {
      this.clientDetails = clientDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
