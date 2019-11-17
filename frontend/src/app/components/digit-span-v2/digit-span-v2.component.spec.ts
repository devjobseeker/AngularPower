import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitSpanV2Component } from './digit-span-v2.component';

describe('DigitSpanV2Component', () => {
  let component: DigitSpanV2Component;
  let fixture: ComponentFixture<DigitSpanV2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitSpanV2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitSpanV2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
