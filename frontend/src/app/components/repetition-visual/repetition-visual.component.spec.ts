import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepetitionVisualComponent } from './repetition-visual.component';

describe('RepetitionVisualComponent', () => {
  let component: RepetitionVisualComponent;
  let fixture: ComponentFixture<RepetitionVisualComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RepetitionVisualComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepetitionVisualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
