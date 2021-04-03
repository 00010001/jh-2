import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClientDetails, ClientDetails } from '../client-details.model';
import { ClientDetailsService } from '../service/client-details.service';

@Injectable({ providedIn: 'root' })
export class ClientDetailsRoutingResolveService implements Resolve<IClientDetails> {
  constructor(protected service: ClientDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClientDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((clientDetails: HttpResponse<ClientDetails>) => {
          if (clientDetails.body) {
            return of(clientDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClientDetails());
  }
}
