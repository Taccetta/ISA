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

describe('Car e2e test', () => {
  const carPageUrl = '/car';
  const carPageUrlPattern = new RegExp('/car(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const carSample = { model: 'total goose', year: 'definitive best', available: 22463 };

  let car;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cars+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cars').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cars/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (car) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cars/${car.id}`,
      }).then(() => {
        car = undefined;
      });
    }
  });

  it('Cars menu should load Cars page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('car');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Car').should('exist');
    cy.url().should('match', carPageUrlPattern);
  });

  describe('Car page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(carPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Car page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/car/new$'));
        cy.getEntityCreateUpdateHeading('Car');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cars',
          body: carSample,
        }).then(({ body }) => {
          car = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cars+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cars?page=0&size=20>; rel="last",<http://localhost/api/cars?page=0&size=20>; rel="first"',
              },
              body: [car],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(carPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Car page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('car');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carPageUrlPattern);
      });

      it('edit button click should load edit Car page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Car');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carPageUrlPattern);
      });

      it('edit button click should load edit Car page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Car');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carPageUrlPattern);
      });

      it('last delete button click should delete instance of Car', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('car').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', carPageUrlPattern);

        car = undefined;
      });
    });
  });

  describe('new Car page', () => {
    beforeEach(() => {
      cy.visit(`${carPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Car');
    });

    it('should create an instance of Car', () => {
      cy.get(`[data-cy="model"]`).type('hmph unfreeze aw');
      cy.get(`[data-cy="model"]`).should('have.value', 'hmph unfreeze aw');

      cy.get(`[data-cy="year"]`).type('failing among');
      cy.get(`[data-cy="year"]`).should('have.value', 'failing among');

      cy.get(`[data-cy="available"]`).type('5649');
      cy.get(`[data-cy="available"]`).should('have.value', '5649');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        car = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', carPageUrlPattern);
    });
  });
});
