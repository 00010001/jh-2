import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClientDetailsComponent } from './list/client-details.component';
import { ClientDetailsDetailComponent } from './detail/client-details-detail.component';
import { ClientDetailsUpdateComponent } from './update/client-details-update.component';
import { ClientDetailsDeleteDialogComponent } from './delete/client-details-delete-dialog.component';
import { ClientDetailsRoutingModule } from './route/client-details-routing.module';

@NgModule({
  imports: [SharedModule, ClientDetailsRoutingModule],
  declarations: [ClientDetailsComponent, ClientDetailsDetailComponent, ClientDetailsUpdateComponent, ClientDetailsDeleteDialogComponent],
  entryComponents: [ClientDetailsDeleteDialogComponent],
})
export class ClientDetailsModule {}
