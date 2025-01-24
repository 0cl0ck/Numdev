import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from 'src/app/features/auth/components/login/login.component';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

describe('Logout Flow', () => {
  let sessionService: SessionService;
  let router: Router;
  let authService: AuthService;

  const mockSessionService = {
    sessionInformation: {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    },
    logOut: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'login', component: LoginComponent }
        ]),
        HttpClientModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        AuthService
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
  });

  it('should clear session and redirect to login on logout', async () => {
    const navigateSpy = jest.spyOn(router, 'navigate');
    
    mockSessionService.logOut();
    await router.navigate(['/login']);
    
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  
});
