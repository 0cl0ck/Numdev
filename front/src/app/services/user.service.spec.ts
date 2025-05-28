import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { User } from '../interfaces/user.interface';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpClient: HttpClient;

  const mockUser: User = {
    id: 1,
    email: 'test@test.com',
    lastName: 'Test',
    firstName: 'User',
    admin: false,
    password: 'password123',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(UserService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getById', () => {
    it('should get user by id', () => {
      // Given
      const userId = '1';
      const getSpy = jest.spyOn(httpClient, 'get').mockReturnValue(of(mockUser));

      // When
      service.getById(userId).subscribe(user => {
        // Then
        expect(user).toEqual(mockUser);
      });

      expect(getSpy).toHaveBeenCalledWith('api/user/1');
    });
  });

  describe('delete', () => {
    it('should delete user', () => {
      // Given
      const userId = '1';
      const deleteSpy = jest.spyOn(httpClient, 'delete').mockReturnValue(of({}));

      // When
      service.delete(userId).subscribe(result => {
        // Then
        expect(result).toEqual({});
      });

      expect(deleteSpy).toHaveBeenCalledWith('api/user/1');
    });
  });
});
