import { TestBed, inject } from '@angular/core/testing';

import { RandomNumberService } from './random-number.service';

describe('RandomNumberService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RandomNumberService]
    });
  });

  it('should be created', inject([RandomNumberService], (service: RandomNumberService) => {
    expect(service).toBeTruthy();
  }));
});
