import { TestBed, inject } from '@angular/core/testing';

import { GameProgressService } from './game-progress.service';

describe('GameProgressService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GameProgressService]
    });
  });

  it('should be created', inject([GameProgressService], (service: GameProgressService) => {
    expect(service).toBeTruthy();
  }));
});
