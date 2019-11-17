import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitSpanComponent } from './digit-span.component';

describe('DigitSpanComponent', () => {
  let component: DigitSpanComponent;
  let fixture: ComponentFixture<DigitSpanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitSpanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitSpanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
