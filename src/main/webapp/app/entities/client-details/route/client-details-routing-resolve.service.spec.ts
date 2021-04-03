jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClientDetails, ClientDetails } from '../client-details.model';
import { ClientDetailsService } from '../service/client-details.service';

import { ClientDetailsRoutingResolveService } from './client-details-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClientDetails routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClientDetailsRoutingResolveService;
    let service: ClientDetailsService;
    let resultClientDetails: IClientDetails | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClientDetailsRoutingResolveService);
      service = TestBed.inject(ClientDetailsService);
      resultClientDetails = undefined;
    });

    describe('resolve', () => {
      it('should return IClientDetails returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClientDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClientDetails).toEqual({ id: 123 });
      });

      it('should return new IClientDetails if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClientDetails = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClientDetails).toEqual(new ClientDetails());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClientDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClientDetails).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
