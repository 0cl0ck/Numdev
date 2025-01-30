describe('Register', () => {
    beforeEach(() => {
      cy.visit('/register')
    })
  
    it('should register successfully with valid data', () => {
      // Mock de la réponse API
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 200,
        body: {
          message: 'User registered successfully!'
        }
      }).as('registerRequest')
  
      // Remplir le formulaire avec les mêmes champs que dans le composant
      cy.get('input[formControlName=firstName]').type('John')
      cy.get('input[formControlName=lastName]').type('Doe')
      cy.get('input[formControlName=email]').type('john.doe@test.com')
      cy.get('input[formControlName=password]').type('Test123!')
  
      // Soumettre le formulaire
      cy.get('button[type=submit]').click()
  
      // Vérifier la requête API avec la structure attendue par le back-end
      cy.wait('@registerRequest').its('request.body').should('deep.equal', {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@test.com',
        password: 'Test123!'
      })
  
      // Vérifier la redirection vers login comme dans auth.flow.spec.ts
      cy.url().should('include', '/login')
    })
  
    it('should display error for existing email', () => {
      // Mock de l'erreur API comme définie dans AuthController.java
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 400,
        body: {
          message: 'Error: Email is already taken!'
        }
      }).as('registerError')
  
      cy.get('input[formControlName=firstName]').type('John')
      cy.get('input[formControlName=lastName]').type('Doe')
      cy.get('input[formControlName=email]').type('existing@test.com')
      cy.get('input[formControlName=password]').type('Test123!')
  
      cy.get('button[type=submit]').click()
  
      // Vérifier le message d'erreur comme dans le composant
      cy.get('.error').should('be.visible')
    })
  })