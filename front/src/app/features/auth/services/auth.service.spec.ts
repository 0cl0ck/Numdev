import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpClient: HttpClient;

  const mockLoginRequest: LoginRequest = {
    email: 'test@test.com',
    password: 'password123'
  };

  const mockRegisterRequest: RegisterRequest = {
    email: 'test@test.com',
    firstName: 'Test',
    lastName: 'User',
    password: 'password123'
  };

  const mockSessionInformation: SessionInformation = {
    token: 'mock-token',
    type: 'Bearer',
    id: 1,
    username: 'testuser',
    firstName: 'Test',
    lastName: 'User',
    admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        {
          provide: HttpClient,
          useValue: {
            post: jest.fn()
          }
        }
      ]
    });
    service = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should register user', () => {
      // Given
      const postSpy = jest.spyOn(httpClient, 'post').mockReturnValue(of(undefined));

      // When
      service.register(mockRegisterRequest).subscribe(result => {
        // Then
        expect(result).toBeUndefined();
      });

      expect(postSpy).toHaveBeenCalledWith('api/auth/register', mockRegisterRequest);
    });
  });

  describe('login', () => {
    it('should login user and return session information', () => {
      // Given
      const postSpy = jest.spyOn(httpClient, 'post').mockReturnValue(of(mockSessionInformation));

      // When
      service.login(mockLoginRequest).subscribe(sessionInfo => {
        // Then
        expect(sessionInfo).toEqual(mockSessionInformation);
      });

      expect(postSpy).toHaveBeenCalledWith('api/auth/login', mockLoginRequest);
    });
  });
});
