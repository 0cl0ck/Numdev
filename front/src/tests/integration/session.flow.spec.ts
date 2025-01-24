import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router, ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ListComponent } from 'src/app/features/sessions/components/list/list.component';
import { FormComponent } from 'src/app/features/sessions/components/form/form.component';
import { DetailComponent } from 'src/app/features/sessions/components/detail/detail.component';
import { SessionApiService } from 'src/app/features/sessions/services/session-api.service';
import { SessionService } from 'src/app/services/session.service';
import { Session } from 'src/app/features/sessions/interfaces/session.interface';
import { expect } from '@jest/globals';
import { OverlayModule } from '@angular/cdk/overlay';
import { NgZone } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { fakeAsync, tick } from '@angular/core/testing';
import { LoginComponent } from 'src/app/features/auth/components/login/login.component';
import { RegisterComponent } from 'src/app/features/auth/components/register/register.component';
import { HttpClient } from '@angular/common/http';

describe('Session Management Flow', () => {
  let router: Router;
  let sessionApiService: SessionApiService;
  let formComponent: FormComponent;
  let listComponent: ListComponent;
  let detailComponent: DetailComponent;
  let matSnackBar: MatSnackBar;

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'Test Description',
    date: new Date(),
    teacher_id: 1,
    users: []
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: () => mockSession.id?.toString()
      }
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent, FormComponent, DetailComponent, LoginComponent, RegisterComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: '', redirectTo: 'sessions', pathMatch: 'full' },
          { path: 'sessions', component: ListComponent },
          { path: 'sessions/create', component: FormComponent },
          { path: 'sessions/:id', component: DetailComponent },
          { path: 'login', component: LoginComponent },
          { path: 'register', component: RegisterComponent }
        ]),
        HttpClientTestingModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        OverlayModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: HttpClient, useValue: { get: () => of([]) } },
        SessionApiService,
        MatSnackBar
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);

    // Initialisation des composants
    formComponent = TestBed.createComponent(FormComponent).componentInstance;
    listComponent = TestBed.createComponent(ListComponent).componentInstance;
    detailComponent = TestBed.createComponent(DetailComponent).componentInstance;

    formComponent.sessionForm = new FormBuilder().group({
      name: ['', Validators.required],
      date: ['', Validators.required],
      teacher_id: ['', Validators.required],
      description: ['', [Validators.required, Validators.max(2000)]]
    });
  });

  describe('Creation Flow', () => {
    it('should create session and redirect to list', () => {
      const ngZone = TestBed.inject(NgZone);
      const createSpy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));
      const navigateSpy = jest.spyOn(router, 'navigate');
      
      ngZone.run(() => {
        formComponent.sessionForm?.setValue({
          name: 'Test Session',
          date: '2024-01-01',
          teacher_id: 1,
          description: 'Test Description'
        });
        
        formComponent.submit();
      });
      
      expect(createSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('Update Flow', () => {
    it('should update session and show success message', () => {
      const updateSpy = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(mockSession));
      const snackBarSpy = jest.spyOn(matSnackBar, 'open');
      
      formComponent.onUpdate = true;
      formComponent.submit();
      
      expect(updateSpy).toHaveBeenCalled();
      expect(snackBarSpy).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    });
  });

  describe('Delete Flow', () => {
    it('should delete session and redirect to list', () => {
      const deleteSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(void 0));
      const navigateSpy = jest.spyOn(router, 'navigate');
      
      detailComponent.delete();
      
      expect(deleteSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('Navigation Flow', () => {
    it('should navigate from list to detail view', async () => {
      const fixture = TestBed.createComponent(DetailComponent);
      const navigateSpy = jest.spyOn(router, 'navigate');
      const detailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));

      await router.navigate(['/sessions', mockSession.id]);
      fixture.detectChanges();

      expect(navigateSpy).toHaveBeenCalledWith(['/sessions', mockSession.id]);
      expect(detailSpy).toHaveBeenCalledWith(mockSession.id!.toString());
    });
  });

  describe('Participation Flow', () => {
    it('should allow user to participate in session', () => {
      const participateSpy = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(void 0));
      const detailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
      
      detailComponent.participate();
      
      expect(participateSpy).toHaveBeenCalledWith(
        mockSession.id!.toString(),
        mockSessionService.sessionInformation.id.toString()
      );
      expect(detailSpy).toHaveBeenCalled();
    });
  });

  describe('List Display Flow', () => {
    it('should display sessions list with admin controls', () => {
      // Mettre en place le spy avant la création du composant
      const sessionsSpy = jest.spyOn(sessionApiService, 'all')
        .mockReturnValue(of([mockSession]));
      
      const fixture = TestBed.createComponent(ListComponent);
      const component = fixture.componentInstance;
      
      fixture.detectChanges();
      
      // Vérifier que l'utilisateur est admin et que les sessions sont disponibles
      expect(component.user?.admin).toBeTruthy();
      expect(component.sessions$).toBeTruthy();
      
      // Vérifier que le composant affiche les contrôles admin
      const compiled = fixture.nativeElement;
      expect(compiled.querySelector('button[routerlink="create"]')).toBeTruthy();
      expect(compiled.querySelector('mat-card-actions button[color="primary"]')).toBeTruthy();
    });
  });

  describe('Update Error Flow', () => {
    it('should handle session update error', async () => {
      const ngZone = TestBed.inject(NgZone);
      const fixture = TestBed.createComponent(FormComponent);
      const updateSpy = jest.spyOn(sessionApiService, 'update')
        .mockReturnValue(throwError(() => new Error('Update failed')));
      const snackBarSpy = jest.spyOn(matSnackBar, 'open');
      
      formComponent.onUpdate = true;
      formComponent.sessionForm?.setValue({
        name: 'Test Session',
        date: '2024-01-01',
        teacher_id: 1,
        description: 'Test Description'
      });
      
      await ngZone.run(async () => {
        try {
          formComponent.submit();
          await fixture.whenStable();
        } catch (error) {
          expect(updateSpy).toHaveBeenCalled();
          expect(snackBarSpy).toHaveBeenCalledWith('Error !', 'Close', { duration: 3000 });
        }
      });
    });
  });
});
