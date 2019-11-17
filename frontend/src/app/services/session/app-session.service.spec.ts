import { TestBed, inject } from '@angular/core/testing';

import { AppSessionService } from './app-session.service';

describe('AppSessionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AppSessionService]
    });
  });

  it('should be created', inject([AppSessionService], (service: AppSessionService) => {
    expect(service).toBeTruthy();
  }));
});
