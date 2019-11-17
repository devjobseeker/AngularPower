import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationSpanComponent } from './location-span.component';

describe('LocationSpanComponent', () => {
  let component: LocationSpanComponent;
  let fixture: ComponentFixture<LocationSpanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LocationSpanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationSpanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
