import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitSpanRunningComponent } from './digit-span-running.component';

describe('DigitSpanRunningComponent', () => {
  let component: DigitSpanRunningComponent;
  let fixture: ComponentFixture<DigitSpanRunningComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitSpanRunningComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitSpanRunningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
