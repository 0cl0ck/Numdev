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
    const sessionApiService = TestBed.inject(SessionApiService);
    jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
    
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
});