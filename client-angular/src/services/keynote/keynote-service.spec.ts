import { TestBed } from '@angular/core/testing';

import { KeynoteService } from './keynote-service';

describe('KeynoteService', () => {
  let service: KeynoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KeynoteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
