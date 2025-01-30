describe('Session Management', () => {
  beforeEach(() => {
    // Login before each test
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });
    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: 'Session Test',
        description: 'Description test',
        date: '2024-01-01',
        teacher_id: 1,
        users: []
      }
    ]).as('sessions');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
    cy.url().should('include', '/sessions');
  });

  it('should create a new session', () => {
    // Code existant du test de création
    // Lignes 21-60 du fichier original
  });

  it('should display sessions list with admin controls', () => {
    cy.wait('@sessions');
    cy.get('mat-card').should('have.length.at.least', 1);
    cy.get('button[routerlink="create"]').should('be.visible');
    cy.get('mat-card-actions button[color="primary"]').should('be.visible');
  });

  it('should display session details correctly', () => {
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session Test',
        description: 'Description test',
        date: '2024-01-01',
        teacher_id: 1,
        users: []
      }
    }).as('sessionDetail');

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: 'Teacher',
        firstName: 'Test',
        email: 'test@test.com',
      }
    });

    cy.get('mat-card-actions button[color="primary"]').first().click();
    cy.wait('@sessionDetail');
    
    cy.get('mat-card-title').should('contain', 'Session Test');
    cy.get('mat-card-content').should('contain', 'Description test');
  });

  it('should delete a session', () => {
    // Mock de la réponse API pour le détail de la session
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session Test',
        description: 'Description test',
        date: '2024-01-01',
        teacher_id: 1,
        users: []
      }
    }).as('sessionDetail');

    // Mock de la suppression
    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200
    }).as('deleteSession');

    // Ajout du mock pour teacher/1
    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: 'Teacher',
        firstName: 'Test',
        email: 'test@test.com',
      }
    });

    // Navigation vers le détail
    cy.contains('Detail').click();
    cy.wait('@sessionDetail');

    // Suppression
    cy.contains('Delete').click();
    cy.wait('@deleteSession');
    cy.url().should('include', '/sessions');
  });

  it('should update a session', () => {
    // Mock de la réponse API pour le détail de la session
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session Test',
        description: 'Description test',
        date: '2024-01-01',
        teacher_id: 1,
        users: []
      }
    }).as('sessionDetail');

    // Mock des teachers - Ajout du mock pour teacher/1
    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: 'Teacher',
        firstName: 'Test',
        email: 'test@test.com',
      }
    });

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: 'Teacher',
          firstName: 'Test',
          email: 'test@test.com',
        },
      ],
    }).as('teachers');

    // Mock de la mise à jour
    cy.intercept('PUT', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Updated Session',
        date: '2024-12-31',
        description: 'Updated description',
        teacher_id: 1,
      }
    }).as('updateSession');

    // Clic direct sur le bouton Edit dans les actions de la carte
    cy.get('mat-card-actions button').contains('Edit').click();
    cy.wait('@teachers');

    // Remplissage du formulaire
    cy.get('input[formControlName=name]').clear().type('Updated Session');
    cy.get('textarea[formControlName=description]').clear().type('Updated description');
    
    // Sauvegarde
    cy.contains('Save').click();
    cy.wait('@updateSession');
    cy.url().should('include', '/sessions');
  });
});