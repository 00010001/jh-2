import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClientDetailsComponent } from '../list/client-details.component';
import { ClientDetailsDetailComponent } from '../detail/client-details-detail.component';
import { ClientDetailsUpdateComponent } from '../update/client-details-update.component';
import { ClientDetailsRoutingResolveService } from './client-details-routing-resolve.service';

const clientDetailsRoute: Routes = [
  {
    path: '',
    component: ClientDetailsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClientDetailsDetailComponent,
    resolve: {
      clientDetails: ClientDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClientDetailsUpdateComponent,
    resolve: {
      clientDetails: ClientDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClientDetailsUpdateComponent,
    resolve: {
      clientDetails: ClientDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(clientDetailsRoute)],
  exports: [RouterModule],
})
export class ClientDetailsRoutingModule {}
