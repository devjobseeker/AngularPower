import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberUpdateAuditoryComponent } from './number-update-auditory.component';

describe('NumberUpdateAuditoryComponent', () => {
  let component: NumberUpdateAuditoryComponent;
  let fixture: ComponentFixture<NumberUpdateAuditoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NumberUpdateAuditoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberUpdateAuditoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
