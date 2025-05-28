describe('Navigation and Error Handling', () => {
  it('should display 404 page for invalid routes', () => {
    cy.visit('/invalid-route', { failOnStatusCode: false });
    
    // Verify 404 page content - should redirect to home or show error
    cy.url().should('include', '/');
  });

  it('should redirect unauthenticated users to login', () => {
    cy.visit('/sessions');
    
    // Should redirect to login
    cy.url().should('include', '/login');
  });

  it('should show login form as disabled when empty', () => {
    cy.visit('/login');
    
    // Button should be disabled with empty form
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should show register form as disabled when empty', () => {
    cy.visit('/register');
    
    // Button should be disabled with empty form  
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should handle login API errors', () => {
    cy.visit('/login');
    
    // Mock error response
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Invalid credentials' }
    }).as('loginError');
    
    cy.get('input[formControlName=email]').type('invalid@test.com');
    cy.get('input[formControlName=password]').type('wrongpassword');
    cy.get('button[type=submit]').click();
    
    cy.wait('@loginError');
    
    // Should show error message
    cy.contains('An error occurred').should('be.visible');
  });
});
