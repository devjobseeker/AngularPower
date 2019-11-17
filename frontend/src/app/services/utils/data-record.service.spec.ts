import { TestBed, inject } from '@angular/core/testing';

import { DataRecordService } from './data-record.service';

describe('DataRecordService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DataRecordService]
    });
  });

  it('should be created', inject([DataRecordService], (service: DataRecordService) => {
    expect(service).toBeTruthy();
  }));
});
