import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IClientDetails, ClientDetails } from '../client-details.model';

import { ClientDetailsService } from './client-details.service';

describe('Service Tests', () => {
  describe('ClientDetails Service', () => {
    let service: ClientDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IClientDetails;
    let expectedResult: IClientDetails | IClientDetails[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClientDetailsService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        kominyDymowe: 'AAAAAAA',
        kominySpalinowe: 'AAAAAAA',
        przewodyWentylacyjne: 'AAAAAAA',
        ryczaltJednorazowy: 'AAAAAAA',
        dataCzyszczenia: currentDate,
        dzienCzyszczenia: 0,
        zaplacono: 'AAAAAAA',
        podpis: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataCzyszczenia: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ClientDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataCzyszczenia: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCzyszczenia: currentDate,
          },
          returnedFromService
        );

        service.create(new ClientDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClientDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            kominyDymowe: 'BBBBBB',
            kominySpalinowe: 'BBBBBB',
            przewodyWentylacyjne: 'BBBBBB',
            ryczaltJednorazowy: 'BBBBBB',
            dataCzyszczenia: currentDate.format(DATE_FORMAT),
            dzienCzyszczenia: 1,
            zaplacono: 'BBBBBB',
            podpis: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCzyszczenia: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ClientDetails', () => {
        const patchObject = Object.assign(
          {
            kominySpalinowe: 'BBBBBB',
            ryczaltJednorazowy: 'BBBBBB',
            zaplacono: 'BBBBBB',
            podpis: 'BBBBBB',
          },
          new ClientDetails()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataCzyszczenia: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ClientDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            kominyDymowe: 'BBBBBB',
            kominySpalinowe: 'BBBBBB',
            przewodyWentylacyjne: 'BBBBBB',
            ryczaltJednorazowy: 'BBBBBB',
            dataCzyszczenia: currentDate.format(DATE_FORMAT),
            dzienCzyszczenia: 1,
            zaplacono: 'BBBBBB',
            podpis: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCzyszczenia: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ClientDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClientDetailsToCollectionIfMissing', () => {
        it('should add a ClientDetails to an empty array', () => {
          const clientDetails: IClientDetails = { id: 123 };
          expectedResult = service.addClientDetailsToCollectionIfMissing([], clientDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(clientDetails);
        });

        it('should not add a ClientDetails to an array that contains it', () => {
          const clientDetails: IClientDetails = { id: 123 };
          const clientDetailsCollection: IClientDetails[] = [
            {
              ...clientDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addClientDetailsToCollectionIfMissing(clientDetailsCollection, clientDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClientDetails to an array that doesn't contain it", () => {
          const clientDetails: IClientDetails = { id: 123 };
          const clientDetailsCollection: IClientDetails[] = [{ id: 456 }];
          expectedResult = service.addClientDetailsToCollectionIfMissing(clientDetailsCollection, clientDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(clientDetails);
        });

        it('should add only unique ClientDetails to an array', () => {
          const clientDetailsArray: IClientDetails[] = [{ id: 123 }, { id: 456 }, { id: 56369 }];
          const clientDetailsCollection: IClientDetails[] = [{ id: 123 }];
          expectedResult = service.addClientDetailsToCollectionIfMissing(clientDetailsCollection, ...clientDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const clientDetails: IClientDetails = { id: 123 };
          const clientDetails2: IClientDetails = { id: 456 };
          expectedResult = service.addClientDetailsToCollectionIfMissing([], clientDetails, clientDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(clientDetails);
          expect(expectedResult).toContain(clientDetails2);
        });

        it('should accept null and undefined values', () => {
          const clientDetails: IClientDetails = { id: 123 };
          expectedResult = service.addClientDetailsToCollectionIfMissing([], null, clientDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(clientDetails);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
