import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ClientDetailsService } from '../service/client-details.service';

import { ClientDetailsComponent } from './client-details.component';

describe('Component Tests', () => {
  describe('ClientDetails Management Component', () => {
    let comp: ClientDetailsComponent;
    let fixture: ComponentFixture<ClientDetailsComponent>;
    let service: ClientDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientDetailsComponent],
      })
        .overrideTemplate(ClientDetailsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientDetailsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ClientDetailsService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.clientDetails?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
