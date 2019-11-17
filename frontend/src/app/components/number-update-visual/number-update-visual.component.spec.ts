import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberUpdateVisualComponent } from './number-update-visual.component';

describe('NumberUpdateVisualComponent', () => {
  let component: NumberUpdateVisualComponent;
  let fixture: ComponentFixture<NumberUpdateVisualComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NumberUpdateVisualComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberUpdateVisualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
