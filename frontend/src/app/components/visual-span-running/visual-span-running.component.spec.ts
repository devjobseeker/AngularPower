import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualSpanRunningComponent } from './visual-span-running.component';

describe('VisualSpanRunningComponent', () => {
  let component: VisualSpanRunningComponent;
  let fixture: ComponentFixture<VisualSpanRunningComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VisualSpanRunningComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualSpanRunningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
