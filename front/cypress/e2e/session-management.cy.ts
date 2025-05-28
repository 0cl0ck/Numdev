describe('Session Management Flow', () => {
  beforeEach(() => {
    cy.visit('/login');

    // Mock login as admin
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'Admin',
        lastName: 'User',
        admin: true,
        token: 'fake-jwt-token'
      },
    }).as('adminLogin');

    // Mock sessions list
    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: 'Morning Yoga',
        description: 'Start your day with yoga',
        date: '2024-06-01T10:00:00',
        teacher_id: 1,
        users: [1],
        createdAt: '2024-01-01T00:00:00',
        updatedAt: '2024-01-01T00:00:00'
      }
    ]).as('sessions');

    // Mock teachers
    cy.intercept('GET', '/api/teacher', [
      {
        id: 1,
        firstName: 'Margot',
        lastName: 'DELAHAYE',
        createdAt: '2024-01-01T00:00:00',
        updatedAt: '2024-01-01T00:00:00'
      }
    ]).as('teachers');

    // Login as admin
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type=submit]').click();

    cy.wait('@adminLogin');
    cy.url().should('include', '/sessions');
    cy.wait('@sessions');
  });

  it('should create a new session as admin', () => {
    // Mock session creation
    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      body: {
        id: 2,
        name: 'Evening Yoga',
        description: 'Relax after work',
        date: '2024-06-02T18:00:00',
        teacher_id: 1,
        users: [],
        createdAt: '2024-01-01T00:00:00',
        updatedAt: '2024-01-01T00:00:00'
      }
    }).as('createSession');

    // Click create button
    cy.get('button').contains('Create').click();
    cy.url().should('include', '/sessions/create');

    cy.wait('@teachers');

    // Fill form
    cy.get('input[formControlName=name]').type('Evening Yoga');
    cy.get('input[formControlName=date]').type('2024-06-02');
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').first().click();
    cy.get('textarea[formControlName=description]').type('Relax after work');

    // Submit form
    cy.get('button[type=submit]').click();

    cy.wait('@createSession');

    // Should redirect to sessions list
    cy.url().should('include', '/sessions');
  });

  it('should view session details', () => {
    // Mock session detail
    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Morning Yoga',
      description: 'Start your day with yoga',
      date: '2024-06-01T10:00:00',
      teacher_id: 1,
      users: [1],
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('sessionDetail');

    // Mock teacher detail
    cy.intercept('GET', '/api/teacher/1', {
      id: 1,
      firstName: 'Margot',
      lastName: 'DELAHAYE',
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('teacherDetail');

    // Click on detail button
    cy.get('button').contains('Detail').click();
    cy.url().should('include', '/sessions/detail/1');

    cy.wait('@sessionDetail');
    cy.wait('@teacherDetail');

    // Verify session details are displayed
    cy.contains('Morning Yoga').should('be.visible');
    cy.contains('Start your day with yoga').should('be.visible');
    cy.contains('Margot DELAHAYE').should('be.visible');
  });

  it('should edit a session as admin', () => {
    // Mock session detail for edit
    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Morning Yoga',
      description: 'Start your day with yoga',
      date: '2024-06-01T10:00:00',
      teacher_id: 1,
      users: [1],
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('sessionDetailForEdit');

    // Mock session update
    cy.intercept('PUT', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Updated Morning Yoga',
        description: 'Updated description',
        date: '2024-06-01T10:00:00',
        teacher_id: 1,
        users: [1],
        createdAt: '2024-01-01T00:00:00',
        updatedAt: '2024-01-01T00:00:00'
      }
    }).as('updateSession');

    // Click on edit button
    cy.get('button').contains('Edit').click();
    cy.url().should('include', '/sessions/update/1');

    cy.wait('@sessionDetailForEdit');
    cy.wait('@teachers');

    // Update form
    cy.get('input[formControlName=name]').clear().type('Updated Morning Yoga');
    cy.get('textarea[formControlName=description]').clear().type('Updated description');

    // Submit form
    cy.get('button[type=submit]').click();

    cy.wait('@updateSession');

    // Should redirect to sessions list
    cy.url().should('include', '/sessions');
  });

  it('should delete a session as admin', () => {
    // Mock session detail
    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Morning Yoga',
      description: 'Start your day with yoga',
      date: '2024-06-01T10:00:00',
      teacher_id: 1,
      users: [1],
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('sessionDetailForDelete');

    // Mock teacher detail
    cy.intercept('GET', '/api/teacher/1', {
      id: 1,
      firstName: 'Margot',
      lastName: 'DELAHAYE',
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('teacherDetailForDelete');

    // Mock session deletion
    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200
    }).as('deleteSession');

    // Go to session detail
    cy.get('button').contains('Detail').click();
    cy.url().should('include', '/sessions/detail/1');

    cy.wait('@sessionDetailForDelete');
    cy.wait('@teacherDetailForDelete');

    // Click delete button
    cy.get('button').contains('Delete').click();

    cy.wait('@deleteSession');

    // Should redirect to sessions list
    cy.url().should('include', '/sessions');
  });
});
