describe('Logout Flow', () => {
  beforeEach(() => {
    cy.visit('/login');
    
    // Intercepter le login
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    }).as('login');

    // Intercepter l'appel à /api/session qui se fait après le login
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
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('button[type=submit]').click();
    
    cy.wait('@login');
    cy.url().should('include', '/sessions');
  });

  it('should logout successfully and redirect to home page', () => {
    // Vérifier que nous sommes bien connectés
    cy.url().should('include', '/sessions');
    
    // Intercepter la requête de déconnexion
    cy.intercept('GET', '/api/session', {
      statusCode: 401,
      body: {
        message: 'Unauthorized'
      }
    }).as('unauthorizedRequest');

    // Cliquer sur le bouton de déconnexion
    cy.contains('Logout').click();

    // Vérifier la redirection vers la page d'accueil
    cy.location('pathname').should('eq', '/');

    // Vérifier que la session est invalidée en essayant d'accéder à une page protégée
    cy.visit('/sessions');
    cy.location('pathname').should('eq', '/login');
  });

  it('should clear session information after logout', () => {
    // Vérifier que nous sommes connectés
    cy.url().should('include', '/sessions');

    // Cliquer sur le bouton de déconnexion
    cy.contains('Logout').click();

    // Vérifier que le localStorage est vidé
    cy.window().then((win) => {
      expect(win.localStorage.getItem('token')).to.be.null;
    });
  });
});
