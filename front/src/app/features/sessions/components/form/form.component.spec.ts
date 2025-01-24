import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { of, throwError } from 'rxjs';

import { FormComponent } from './form.component';
import { Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { ListComponent } from '../../components/list/list.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const routes = [
    { path: 'sessions', component: ListComponent }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        SessionApiService
      ],
      declarations: [FormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.sessionForm?.get('name')?.value).toBe('');
    expect(component.sessionForm?.get('date')?.value).toBe('');
    expect(component.sessionForm?.get('teacher_id')?.value).toBe('');
    expect(component.sessionForm?.get('description')?.value).toBe('');
  });

  it('should validate required fields', () => {
    expect(component.sessionForm?.valid).toBeFalsy();
    component.sessionForm?.patchValue({
      name: 'Test Session',
      date: '2024-01-01',
      teacher_id: 1,
      description: 'Test Description'
    });
    expect(component.sessionForm?.valid).toBeTruthy();
  });
    
  it('should handle form submission success', () => {
    const router = TestBed.inject(Router);
    const navigateSpy = jest.spyOn(router, 'navigate');
    jest.spyOn(sessionApiService, 'create').mockReturnValue(of({} as Session));

    component.sessionForm?.patchValue({
      name: 'Test Session',
      date: '2024-01-01',
      teacher_id: 1,
      description: 'Test Description'
    });

    component.submit();

    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Session created !',
      'Close',
      { duration: 3000 }
    );
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });
});
