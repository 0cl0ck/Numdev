import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';

import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let router: Router;
  let matSnackBar: MatSnackBar;
  let sessionService: SessionService;

  const mockUser: User = {
    id: 1,
    email: 'test@example.com',
    lastName: 'Doe',
    firstName: 'John',
    admin: false,
    password: '',
    createdAt: new Date(),
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  };

  beforeEach(async () => {
    // Création des mocks avec Jest
    const userServiceSpy = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of(undefined))
    };
    
    const routerSpy = {
      navigate: jest.fn()
    };
    
    const matSnackBarSpy = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: userServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MatSnackBar, useValue: matSnackBarSpy }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    
    // Récupération des services injectés
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    sessionService = TestBed.inject(SessionService);
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should fetch user data on init', () => {
    // Act
    fixture.detectChanges();
    
    // Assert
    expect(userService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should navigate back when back() is called', () => {
    // Arrange
    jest.spyOn(window.history, 'back').mockImplementation(() => {});
    
    // Act
    component.back();
    
    // Assert
    expect(window.history.back).toHaveBeenCalled();
  });

  it('should delete user account and handle logout', () => {
    // Arrange
    fixture.detectChanges();
    
    // Act
    component.delete();
    
    // Assert
    expect(userService.delete).toHaveBeenCalledWith('1');
    expect(matSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !', 
      'Close', 
      { duration: 3000 }
    );
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });
});
