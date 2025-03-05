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

describe('PurchasedCar e2e test', () => {
  const purchasedCarPageUrl = '/purchased-car';
  const purchasedCarPageUrlPattern = new RegExp('/purchased-car(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const purchasedCarSample = {};

  let purchasedCar;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/purchased-cars+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/purchased-cars').as('postEntityRequest');
    cy.intercept('DELETE', '/api/purchased-cars/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (purchasedCar) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/purchased-cars/${purchasedCar.id}`,
      }).then(() => {
        purchasedCar = undefined;
      });
    }
  });

  it('PurchasedCars menu should load PurchasedCars page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('purchased-car');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PurchasedCar').should('exist');
    cy.url().should('match', purchasedCarPageUrlPattern);
  });

  describe('PurchasedCar page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(purchasedCarPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PurchasedCar page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/purchased-car/new$'));
        cy.getEntityCreateUpdateHeading('PurchasedCar');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedCarPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/purchased-cars',
          body: purchasedCarSample,
        }).then(({ body }) => {
          purchasedCar = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/purchased-cars+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/purchased-cars?page=0&size=20>; rel="last",<http://localhost/api/purchased-cars?page=0&size=20>; rel="first"',
              },
              body: [purchasedCar],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(purchasedCarPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PurchasedCar page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('purchasedCar');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedCarPageUrlPattern);
      });

      it('edit button click should load edit PurchasedCar page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedCar');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedCarPageUrlPattern);
      });

      it('edit button click should load edit PurchasedCar page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedCar');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedCarPageUrlPattern);
      });

      it('last delete button click should delete instance of PurchasedCar', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('purchasedCar').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedCarPageUrlPattern);

        purchasedCar = undefined;
      });
    });
  });

  describe('new PurchasedCar page', () => {
    beforeEach(() => {
      cy.visit(`${purchasedCarPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PurchasedCar');
    });

    it('should create an instance of PurchasedCar', () => {
      cy.get(`[data-cy="purchaseDate"]`).type('2025-03-02');
      cy.get(`[data-cy="purchaseDate"]`).blur();
      cy.get(`[data-cy="purchaseDate"]`).should('have.value', '2025-03-02');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        purchasedCar = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', purchasedCarPageUrlPattern);
    });
  });
});
