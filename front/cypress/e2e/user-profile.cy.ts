describe('User Profile (Me) Flow', () => {
  beforeEach(() => {
    cy.visit('/login');

    // Mock login
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false,
      },
    }).as('login');

    // Mock sessions list
    cy.intercept('GET', '/api/session', []).as('sessions');

    // Login
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type=submit]').click();

    cy.wait('@login');
    cy.url().should('include', '/sessions');
  });

  it('should navigate back from profile', () => {
    // Navigate to profile
    cy.contains('Account').click();
    cy.url().should('include', '/me');

    // Go back - click on Sessions instead of back button
    cy.contains('Sessions').click();

    // Should be back to sessions
    cy.url().should('include', '/sessions');
  });
});
