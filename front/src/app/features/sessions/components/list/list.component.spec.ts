import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { ListComponent } from './list.component';
import { expect } from '@jest/globals';

describe('ListComponent with admin user', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessions = [{ 
    id: 1, 
    name: 'Session 1',
    description: 'Description 1',
    date: new Date(),
    teacher_id: 1,
    users: []
  }];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    
    jest.spyOn(sessionApiService, 'all').mockReturnValue(of(mockSessions));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

describe('ListComponent with non-admin user', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;

  const mockNonAdminService = {
    sessionInformation: {
      admin: false
    }
  };

  const mockSessions = [{ 
    id: 1, 
    name: 'Session 1',
    description: 'Description 1',
    date: new Date(),
    teacher_id: 1,
    users: []
  }];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule, 
        MatCardModule, 
        MatIconModule,
        RouterTestingModule
      ],
      providers: [
        { provide: SessionService, useValue: mockNonAdminService },
        SessionApiService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    
    jest.spyOn(sessionApiService, 'all').mockReturnValue(of(mockSessions));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
