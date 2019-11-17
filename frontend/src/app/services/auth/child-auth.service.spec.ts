import { TestBed, inject } from '@angular/core/testing';

import { ChildAuthService } from './child-auth.service';

describe('ChildAuthService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ChildAuthService]
    });
  });

  it('should be created', inject([ChildAuthService], (service: ChildAuthService) => {
    expect(service).toBeTruthy();
  }));
});
