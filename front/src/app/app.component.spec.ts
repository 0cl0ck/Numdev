import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';


describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: any;
  let mockSessionService: any;
  let router: Router;

  beforeEach(async () => {
    mockSessionService = {
      $isLogged: jest.fn(),
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService }
      ]
    }).compileComponents();
    
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should return observable from sessionService $isLogged', () => {
    const mockObservable = of(true);
    mockSessionService.$isLogged.mockReturnValue(mockObservable);

    const result = component.$isLogged();

    expect(mockSessionService.$isLogged).toHaveBeenCalled();
    expect(result).toBe(mockObservable);
  });

  it('should call sessionService logOut and navigate to home on logout', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.logout();

    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});
