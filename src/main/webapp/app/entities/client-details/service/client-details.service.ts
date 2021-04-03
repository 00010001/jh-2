import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClientDetails, getClientDetailsIdentifier } from '../client-details.model';

export type EntityResponseType = HttpResponse<IClientDetails>;
export type EntityArrayResponseType = HttpResponse<IClientDetails[]>;

@Injectable({ providedIn: 'root' })
export class ClientDetailsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/client-details');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(clientDetails: IClientDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clientDetails);
    return this.http
      .post<IClientDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(clientDetails: IClientDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clientDetails);
    return this.http
      .put<IClientDetails>(`${this.resourceUrl}/${getClientDetailsIdentifier(clientDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(clientDetails: IClientDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clientDetails);
    return this.http
      .patch<IClientDetails>(`${this.resourceUrl}/${getClientDetailsIdentifier(clientDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClientDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClientDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClientDetailsToCollectionIfMissing(
    clientDetailsCollection: IClientDetails[],
    ...clientDetailsToCheck: (IClientDetails | null | undefined)[]
  ): IClientDetails[] {
    const clientDetails: IClientDetails[] = clientDetailsToCheck.filter(isPresent);
    if (clientDetails.length > 0) {
      const clientDetailsCollectionIdentifiers = clientDetailsCollection.map(
        clientDetailsItem => getClientDetailsIdentifier(clientDetailsItem)!
      );
      const clientDetailsToAdd = clientDetails.filter(clientDetailsItem => {
        const clientDetailsIdentifier = getClientDetailsIdentifier(clientDetailsItem);
        if (clientDetailsIdentifier == null || clientDetailsCollectionIdentifiers.includes(clientDetailsIdentifier)) {
          return false;
        }
        clientDetailsCollectionIdentifiers.push(clientDetailsIdentifier);
        return true;
      });
      return [...clientDetailsToAdd, ...clientDetailsCollection];
    }
    return clientDetailsCollection;
  }

  protected convertDateFromClient(clientDetails: IClientDetails): IClientDetails {
    return Object.assign({}, clientDetails, {
      dataCzyszczenia: clientDetails.dataCzyszczenia?.isValid() ? clientDetails.dataCzyszczenia.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataCzyszczenia = res.body.dataCzyszczenia ? dayjs(res.body.dataCzyszczenia) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((clientDetails: IClientDetails) => {
        clientDetails.dataCzyszczenia = clientDetails.dataCzyszczenia ? dayjs(clientDetails.dataCzyszczenia) : undefined;
      });
    }
    return res;
  }
}
