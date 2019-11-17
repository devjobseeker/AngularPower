import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationSpanRunningComponent } from './location-span-running.component';

describe('LocationSpanRunningComponent', () => {
  let component: LocationSpanRunningComponent;
  let fixture: ComponentFixture<LocationSpanRunningComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LocationSpanRunningComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationSpanRunningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
