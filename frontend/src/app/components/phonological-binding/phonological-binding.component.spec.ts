import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhonologicalBindingComponent } from './phonological-binding.component';

describe('PhonologicalBindingComponent', () => {
  let component: PhonologicalBindingComponent;
  let fixture: ComponentFixture<PhonologicalBindingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PhonologicalBindingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PhonologicalBindingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
