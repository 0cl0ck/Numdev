describe('Session Participation Flow', () => {
  beforeEach(() => {
    cy.visit('/login');

    // Mock login as regular user
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 2,
        username: 'user@studio.com',
        firstName: 'John',
        lastName: 'User',
        admin: false,
        token: 'fake-jwt-token'
      },
    }).as('userLogin');

    // Mock sessions list
    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: 'Morning Yoga',
        description: 'Start your day with yoga',
        date: '2024-06-01T10:00:00',
        teacher_id: 1,
        users: [],
        createdAt: '2024-01-01T00:00:00',
        updatedAt: '2024-01-01T00:00:00'
      }
    ]).as('sessions');

    // Login as regular user
    cy.get('input[formControlName=email]').type('user@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type=submit]').click();

    cy.wait('@userLogin');
    cy.url().should('include', '/sessions');
    cy.wait('@sessions');
  });

  it('should participate in a session', () => {
    // Mock session detail (user not participating)
    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Morning Yoga',
      description: 'Start your day with yoga',
      date: '2024-06-01T10:00:00',
      teacher_id: 1,
      users: [],
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

    // Mock participation
    cy.intercept('POST', '/api/session/1/participate/2', {
      statusCode: 200
    }).as('participate');

    // Go to session detail
    cy.get('button').contains('Detail').click();
    cy.url().should('include', '/sessions/detail/1');

    cy.wait('@sessionDetail');
    cy.wait('@teacherDetail');

    // Verify participate button exists and click it
    cy.get('button').contains('Participate').should('exist').click();

    cy.wait('@participate');

    // Test passes if participation API call was successful
    cy.url().should('include', '/sessions/detail/1');
  });

  it('should unparticipate from a session', () => {
    // Mock session detail (user already participating)
    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Morning Yoga',
      description: 'Start your day with yoga',
      date: '2024-06-01T10:00:00',
      teacher_id: 1,
      users: [2],
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('sessionDetailWithUser');

    // Mock teacher detail
    cy.intercept('GET', '/api/teacher/1', {
      id: 1,
      firstName: 'Margot',
      lastName: 'DELAHAYE',
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('teacherDetailForUnparticipate');

    // Mock unparticipation
    cy.intercept('DELETE', '/api/session/1/participate/2', {
      statusCode: 200
    }).as('unparticipate');

    // Go to session detail
    cy.get('button').contains('Detail').click();
    cy.url().should('include', '/sessions/detail/1');

    cy.wait('@sessionDetailWithUser');
    cy.wait('@teacherDetailForUnparticipate');

    // Verify unparticipate button exists and click it
    cy.get('button').contains('Do not participate').should('exist').click();

    cy.wait('@unparticipate');

    // The test passes if we can successfully unparticipate
    cy.url().should('include', '/sessions/detail/1');
  });

  it('should not show admin buttons for regular user', () => {
    // Mock session detail
    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Morning Yoga',
      description: 'Start your day with yoga',
      date: '2024-06-01T10:00:00',
      teacher_id: 1,
      users: [],
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('sessionDetailForPermissions');

    // Mock teacher detail
    cy.intercept('GET', '/api/teacher/1', {
      id: 1,
      firstName: 'Margot',
      lastName: 'DELAHAYE',
      createdAt: '2024-01-01T00:00:00',
      updatedAt: '2024-01-01T00:00:00'
    }).as('teacherDetailForPermissions');

    // Go to session detail
    cy.get('button').contains('Detail').click();
    cy.url().should('include', '/sessions/detail/1');

    cy.wait('@sessionDetailForPermissions');
    cy.wait('@teacherDetailForPermissions');

    // Verify admin buttons don't exist for regular user
    cy.get('body').should('not.contain', 'Edit');
    cy.get('body').should('not.contain', 'Delete');
  });
});
