import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClientDetailsDetailComponent } from './client-details-detail.component';

describe('Component Tests', () => {
  describe('ClientDetails Management Detail Component', () => {
    let comp: ClientDetailsDetailComponent;
    let fixture: ComponentFixture<ClientDetailsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ClientDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ clientDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ClientDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClientDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load clientDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.clientDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
