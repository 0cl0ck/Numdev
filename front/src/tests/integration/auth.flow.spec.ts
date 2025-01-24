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
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { LoginComponent } from 'src/app/features/auth/components/login/login.component';
import { RegisterComponent } from 'src/app/features/auth/components/register/register.component';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { ListComponent } from 'src/app/features/sessions/components/list/list.component';

describe('Authentication Flow', () => {
  let loginComponent: LoginComponent;
  let registerComponent: RegisterComponent;
  let loginFixture: ComponentFixture<LoginComponent>;
  let registerFixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent, RegisterComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'login', component: LoginComponent },
          { path: 'register', component: RegisterComponent },
          { path: 'sessions', component: ListComponent }
        ]),
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [AuthService, SessionService]
    }).compileComponents();

    router = TestBed.inject(Router);
    authService = TestBed.inject(AuthService);
    
    loginFixture = TestBed.createComponent(LoginComponent);
    loginComponent = loginFixture.componentInstance;
    
    registerFixture = TestBed.createComponent(RegisterComponent);
    registerComponent = registerFixture.componentInstance;
  });

  describe('Registration Flow', () => {
    it('should navigate to login page on successful registration', () => {
      const navigateSpy = jest.spyOn(router, 'navigate');
      jest.spyOn(authService, 'register').mockReturnValue(of(void 0));

      registerComponent.form.setValue({
        email: 'test@test.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123'
      });
      
      registerComponent.submit();
      
      expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    });

    it('should show error message when registration fails', () => {
      const spy = jest.spyOn(authService, 'register').mockReturnValue(
        throwError(() => new Error('Registration failed'))
      );
      
      registerComponent.form.setValue({
        email: 'test@test.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123'
      });
      
      registerComponent.submit();
      registerFixture.detectChanges();
      
      const errorMessage = registerFixture.nativeElement.querySelector('.error');
      expect(errorMessage.textContent).toContain('An error occurred');
      expect(spy).toHaveBeenCalled();
    });
  });

  describe('Login Flow', () => {
    it('should show error message when login fails', () => {
      const spy = jest.spyOn(authService, 'login').mockReturnValue(
        throwError(() => new Error('Login failed'))
      );
      
      loginComponent.form.setValue({
        email: 'test@test.com',
        password: 'wrongpassword'
      });
      
      loginComponent.submit();
      loginFixture.detectChanges();
      
      const errorMessage = loginFixture.nativeElement.querySelector('.error');
      expect(errorMessage.textContent).toContain('An error occurred');
      expect(spy).toHaveBeenCalled();
    });

    it('should navigate to sessions page on successful login', () => {
      const navigateSpy = jest.spyOn(router, 'navigate');
      const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(of({
        token: 'fake-token',
        type: 'Bearer',
        id: 1,
        username: 'test@test.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false
      }));

      loginComponent.form.setValue({
        email: 'test@test.com',
        password: 'password123'
      });
      
      loginComponent.submit();
      
      expect(loginSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    });
  });
}); 