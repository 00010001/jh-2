jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClientDetailsService } from '../service/client-details.service';
import { IClientDetails, ClientDetails } from '../client-details.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { ClientDetailsUpdateComponent } from './client-details-update.component';

describe('Component Tests', () => {
  describe('ClientDetails Management Update Component', () => {
    let comp: ClientDetailsUpdateComponent;
    let fixture: ComponentFixture<ClientDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clientDetailsService: ClientDetailsService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClientDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clientDetailsService = TestBed.inject(ClientDetailsService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const clientDetails: IClientDetails = { id: 456 };
        const client: IClient = { id: 65506 };
        clientDetails.client = client;

        const clientCollection: IClient[] = [{ id: 26169 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ clientDetails });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const clientDetails: IClientDetails = { id: 456 };
        const client: IClient = { id: 52291 };
        clientDetails.client = client;

        activatedRoute.data = of({ clientDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(clientDetails));
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const clientDetails = { id: 123 };
        spyOn(clientDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ clientDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: clientDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clientDetailsService.update).toHaveBeenCalledWith(clientDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const clientDetails = new ClientDetails();
        spyOn(clientDetailsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ clientDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: clientDetails }));
        saveSubject.complete();

        // THEN
        expect(clientDetailsService.create).toHaveBeenCalledWith(clientDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const clientDetails = { id: 123 };
        spyOn(clientDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ clientDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clientDetailsService.update).toHaveBeenCalledWith(clientDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
