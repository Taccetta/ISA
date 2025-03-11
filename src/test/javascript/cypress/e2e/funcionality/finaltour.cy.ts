import {
  titleLoginSelector,
  errorLoginSelector,
  usernameLoginSelector,
  passwordLoginSelector,
  submitLoginSelector,
} from '../../support/commands';

import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Tour', () => {
  // Obtener credenciales de usuario desde las variables de entorno
  const username = Cypress.env('E2E_USERNAME');
  const password = Cypress.env('E2E_PASSWORD');
  const carPageUrl = '/car';
  const carPageUrlPattern = new RegExp('/car(\\?.*)?$');
  const carSample = { model: 'Tesla Model S', year: '2021', available: '6' };

  let car;

  // Logearse e ir al inicio
  beforeEach(() => {
    cy.login(username, password);
    cy.visit('');
  });

  // Interceptar la peticion de autenticacion, creacion, obtencio o eliminacion de autos
  beforeEach(() => {
    cy.intercept('POST', '/api/authenticate').as('authenticate');
    cy.intercept('GET', '/api/cars+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cars').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cars/*').as('deleteEntityRequest');
  });

  // Test: Ir a la pagina de entidades de autos y crear un nuevo auto
  it('Go to Car entities and create a new car', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('car');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Car').should('exist');
    cy.url().should('match', carPageUrlPattern);

    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.get(`[data-cy="model"]`).type(carSample.model);
    cy.get(`[data-cy="year"]`).type(carSample.year);
    cy.get(`[data-cy="available"]`).type(carSample.available);

    cy.get(entityCreateSaveButtonSelector).click({ force: true });

    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
      car = response.body;
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
  });

  // Test: Ir a la pagina de detalles del auto creado anteriormente
  it('Go to the car details page', () => {
    // Ir a la pagina de entidades de autos
    cy.visit(carPageUrl);
    cy.wait('@entitiesRequest');

    // Verificar que exista la tabla de autos
    cy.getEntityHeading('Car').should('exist');
    cy.url().should('match', carPageUrlPattern);

    // Buscar el auto creado anteriormente y ver detalles
    cy.get('tr')
      .filter(':contains("' + carSample.model + '")')
      .filter(':contains("' + carSample.year + '")')
      .filter(':contains("' + carSample.available + '")')
      .within(() => {
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
      });
  });

  // Test: Eliminar el auto creado anteriormente
  it('Delete the car', () => {
    // Ir a la pagina de entidades de autos
    cy.visit(carPageUrl);
    cy.wait('@entitiesRequest');

    // Verificar que exista la tabla de autos
    cy.getEntityHeading('Car').should('exist');
    cy.url().should('match', carPageUrlPattern);

    // Buscar el auto creado anteriormente y eliminar
    cy.get('tr')
      .filter(':contains("' + carSample.model + '")')
      .filter(':contains("' + carSample.year + '")')
      .filter(':contains("' + carSample.available + '")')
      .within(() => {
        cy.get(entityDeleteButtonSelector).first().click({ force: true });
      });

    cy.get(entityConfirmDeleteButtonSelector).click();
    cy.wait('@deleteEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(204);
    });
  });
});
