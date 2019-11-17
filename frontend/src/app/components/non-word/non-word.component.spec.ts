import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NonWordComponent } from './non-word.component';

describe('NonWordComponent', () => {
  let component: NonWordComponent;
  let fixture: ComponentFixture<NonWordComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NonWordComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NonWordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
