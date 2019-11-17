import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepetitionAuditoryComponent } from './repetition-auditory.component';

describe('RepetitionAuditoryComponent', () => {
  let component: RepetitionAuditoryComponent;
  let fixture: ComponentFixture<RepetitionAuditoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RepetitionAuditoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepetitionAuditoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
