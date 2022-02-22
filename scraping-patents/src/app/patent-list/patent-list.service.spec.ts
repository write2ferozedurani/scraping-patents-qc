import { TestBed } from '@angular/core/testing';

import { PatentListService } from './patent-list.service';

describe('PatentListService', () => {
  let service: PatentListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatentListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
