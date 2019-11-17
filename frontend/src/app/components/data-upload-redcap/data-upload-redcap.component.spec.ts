import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataUploadRedcapComponent } from './data-upload-redcap.component';

describe('DataUploadRedcapComponent', () => {
  let component: DataUploadRedcapComponent;
  let fixture: ComponentFixture<DataUploadRedcapComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataUploadRedcapComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataUploadRedcapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
