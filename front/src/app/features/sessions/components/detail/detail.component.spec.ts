import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { DetailComponent } from './detail.component';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let mockSessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
      mockSessionApiService = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display session details', () => {
    const mockSession = {
      id: 1,
      name: 'Session 1',
      description: 'Description',
      date: new Date(),
      teacher_id: 1,
      users: []
    };
    jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(mockSession));
    
    component.ngOnInit();
    fixture.detectChanges();
    
    const titleElement = fixture.nativeElement.querySelector('h1');
    expect(titleElement.textContent).toContain(mockSession.name);
  });

  it('should show delete button for admin users', () => {
    component.session = {
      id: 1,
      name: 'Session 1',
      description: 'Description',
      date: new Date(),
      teacher_id: 1,
      users: []
    };
    component.isAdmin = true;
    fixture.detectChanges();
    
    const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
    expect(deleteButton).toBeTruthy();
  });

  it('should hide delete button for non-admin users', () => {
    component.isAdmin = false;
    fixture.detectChanges();
    
    const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
    expect(deleteButton).toBeFalsy();
  });

  it('should call window.history.back on back method', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    
    component.back();
    
    expect(backSpy).toHaveBeenCalled();
  });

  it('should call unParticipate and refresh session', () => {
    // Setup
    component.sessionId = '1';
    component.userId = '2';
    const unParticipateSpy = jest.spyOn(mockSessionApiService, 'unParticipate').mockReturnValue(of(void 0));
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
    
    // Act
    component.unParticipate();
    
    // Assert
    expect(unParticipateSpy).toHaveBeenCalledWith('1', '2');
    expect(fetchSessionSpy).toHaveBeenCalled();
  });
});