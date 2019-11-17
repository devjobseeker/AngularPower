import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberUpdateVisualThreeBoxesComponent } from './number-update-visual-three-boxes.component';

describe('NumberUpdateVisualThreeBoxesComponent', () => {
  let component: NumberUpdateVisualThreeBoxesComponent;
  let fixture: ComponentFixture<NumberUpdateVisualThreeBoxesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NumberUpdateVisualThreeBoxesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberUpdateVisualThreeBoxesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
