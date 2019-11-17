import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualSpanComponent } from './visual-span.component';

describe('VisualSpanComponent', () => {
  let component: VisualSpanComponent;
  let fixture: ComponentFixture<VisualSpanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualSpanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualSpanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
