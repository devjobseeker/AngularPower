import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrossModalBindingComponent } from './cross-modal-binding.component';

describe('CrossModalBindingComponent', () => {
  let component: CrossModalBindingComponent;
  let fixture: ComponentFixture<CrossModalBindingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrossModalBindingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrossModalBindingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
