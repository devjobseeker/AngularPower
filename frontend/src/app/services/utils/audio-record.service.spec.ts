import { TestBed, inject } from '@angular/core/testing';

import { AudioRecordService } from './audio-record.service';

describe('AudioRecordService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AudioRecordService]
    });
  });

  it('should be created', inject([AudioRecordService], (service: AudioRecordService) => {
    expect(service).toBeTruthy();
  }));
});
