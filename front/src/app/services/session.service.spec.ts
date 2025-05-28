import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  const mockUser: SessionInformation = {
    token: 'mock-token',
    type: 'Bearer',
    id: 1,
    username: 'testuser',
    firstName: 'Test',
    lastName: 'User',
    admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize with isLogged false', () => {
    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined();
  });

  describe('$isLogged', () => {
    it('should return observable of login status', (done) => {
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBeFalsy();
        done();
      });
    });

    it('should emit true when user logs in', (done) => {
      service.logIn(mockUser);
      
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBeTruthy();
        done();
      });
    });
  });

  describe('logIn', () => {
    it('should set session information and isLogged to true', () => {
      service.logIn(mockUser);

      expect(service.sessionInformation).toEqual(mockUser);
      expect(service.isLogged).toBeTruthy();
    });

    it('should emit login status through observable', (done) => {
      service.$isLogged().subscribe(isLogged => {
        if (isLogged) {
          expect(isLogged).toBeTruthy();
          done();
        }
      });

      service.logIn(mockUser);
    });
  });

  describe('logOut', () => {
    beforeEach(() => {
      // Setup logged in state
      service.logIn(mockUser);
    });

    it('should clear session information and set isLogged to false', () => {
      service.logOut();

      expect(service.sessionInformation).toBeUndefined();
      expect(service.isLogged).toBeFalsy();
    });

    it('should emit logout status through observable', (done) => {
      let callCount = 0;
      
      service.$isLogged().subscribe(isLogged => {
        callCount++;
        // Skip the initial true value from setup
        if (callCount === 2) {
          expect(isLogged).toBeFalsy();
          done();
        }
      });

      service.logOut();
    });
  });
});
