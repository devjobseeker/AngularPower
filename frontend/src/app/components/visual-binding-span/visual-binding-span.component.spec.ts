import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualBindingSpanComponent } from './visual-binding-span.component';

describe('VisualBindingSpanComponent', () => {
  let component: VisualBindingSpanComponent;
  let fixture: ComponentFixture<VisualBindingSpanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualBindingSpanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualBindingSpanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
