import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { Session } from '../interfaces/session.interface';

import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClient: HttpClient;

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'Test Description',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2]
  };

  const mockSessions: Session[] = [mockSession];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all', () => {
    it('should return all sessions', () => {
      // Given
      const getSpy = jest.spyOn(httpClient, 'get').mockReturnValue(of(mockSessions));

      // When
      service.all().subscribe(sessions => {
        // Then
        expect(sessions).toEqual(mockSessions);
      });

      expect(getSpy).toHaveBeenCalledWith('api/session');
    });
  });

  describe('detail', () => {
    it('should return session detail', () => {
      // Given
      const sessionId = '1';
      const getSpy = jest.spyOn(httpClient, 'get').mockReturnValue(of(mockSession));

      // When
      service.detail(sessionId).subscribe(session => {
        // Then
        expect(session).toEqual(mockSession);
      });

      expect(getSpy).toHaveBeenCalledWith('api/session/1');
    });
  });

  describe('delete', () => {
    it('should delete session', () => {
      // Given
      const sessionId = '1';
      const deleteSpy = jest.spyOn(httpClient, 'delete').mockReturnValue(of({}));

      // When
      service.delete(sessionId).subscribe(result => {
        // Then
        expect(result).toEqual({});
      });

      expect(deleteSpy).toHaveBeenCalledWith('api/session/1');
    });
  });

  describe('create', () => {
    it('should create session', () => {
      // Given
      const postSpy = jest.spyOn(httpClient, 'post').mockReturnValue(of(mockSession));

      // When
      service.create(mockSession).subscribe(session => {
        // Then
        expect(session).toEqual(mockSession);
      });

      expect(postSpy).toHaveBeenCalledWith('api/session', mockSession);
    });
  });

  describe('update', () => {
    it('should update session', () => {
      // Given
      const sessionId = '1';
      const updatedSession = { ...mockSession, name: 'Updated Session' };
      const putSpy = jest.spyOn(httpClient, 'put').mockReturnValue(of(updatedSession));

      // When
      service.update(sessionId, updatedSession).subscribe(session => {
        // Then
        expect(session).toEqual(updatedSession);
      });

      expect(putSpy).toHaveBeenCalledWith('api/session/1', updatedSession);
    });
  });

  describe('participate', () => {
    it('should add user participation to session', () => {
      // Given
      const sessionId = '1';
      const userId = '2';
      const postSpy = jest.spyOn(httpClient, 'post').mockReturnValue(of(undefined));

      // When
      service.participate(sessionId, userId).subscribe(result => {
        // Then
        expect(result).toBeUndefined();
      });

      expect(postSpy).toHaveBeenCalledWith('api/session/1/participate/2', null);
    });
  });

  describe('unParticipate', () => {
    it('should remove user participation from session', () => {
      // Given
      const sessionId = '1';
      const userId = '2';
      const deleteSpy = jest.spyOn(httpClient, 'delete').mockReturnValue(of(undefined));

      // When
      service.unParticipate(sessionId, userId).subscribe(result => {
        // Then
        expect(result).toBeUndefined();
      });

      expect(deleteSpy).toHaveBeenCalledWith('api/session/1/participate/2');
    });
  });
});
