import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Client e2e test', () => {
  const clientPageUrl = '/client';
  const clientPageUrlPattern = new RegExp('/client(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const clientSample = { firstName: 'Randi', lastName: 'MacGyver' };

  let client;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/clients+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/clients').as('postEntityRequest');
    cy.intercept('DELETE', '/api/clients/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (client) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/clients/${client.id}`,
      }).then(() => {
        client = undefined;
      });
    }
  });

  it('Clients menu should load Clients page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Client').should('exist');
    cy.url().should('match', clientPageUrlPattern);
  });

  describe('Client page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(clientPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Client page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/client/new$'));
        cy.getEntityCreateUpdateHeading('Client');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/clients',
          body: clientSample,
        }).then(({ body }) => {
          client = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/clients+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/clients?page=0&size=20>; rel="last",<http://localhost/api/clients?page=0&size=20>; rel="first"',
              },
              body: [client],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(clientPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Client page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('client');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);
      });

      it('edit button click should load edit Client page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Client');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);
      });

      it('edit button click should load edit Client page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Client');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);
      });

      it('last delete button click should delete instance of Client', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('client').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);

        client = undefined;
      });
    });
  });

  describe('new Client page', () => {
    beforeEach(() => {
      cy.visit(`${clientPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Client');
    });

    it('should create an instance of Client', () => {
      cy.get(`[data-cy="firstName"]`).type('Pearl');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Pearl');

      cy.get(`[data-cy="lastName"]`).type('Hermann');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Hermann');

      cy.get(`[data-cy="email"]`).type('Darius12@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Darius12@gmail.com');

      cy.get(`[data-cy="address"]`).type('more think um');
      cy.get(`[data-cy="address"]`).should('have.value', 'more think um');

      cy.get(`[data-cy="phone"]`).type('1-315-878-4177 x4547');
      cy.get(`[data-cy="phone"]`).should('have.value', '1-315-878-4177 x4547');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        client = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', clientPageUrlPattern);
    });
  });
});
