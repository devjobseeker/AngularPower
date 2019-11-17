import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualBindingSpanV2Component } from './visual-binding-span-v2.component';

describe('VisualBindingSpanV2Component', () => {
  let component: VisualBindingSpanV2Component;
  let fixture: ComponentFixture<VisualBindingSpanV2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualBindingSpanV2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualBindingSpanV2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
