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

describe('Familia e2e test', () => {
  const familiaPageUrl = '/familia';
  const familiaPageUrlPattern = new RegExp('/familia(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const familiaSample = { nome: 'system-worthy', ativa: true, renda: 3872 };

  let familia;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/familias+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/familias').as('postEntityRequest');
    cy.intercept('DELETE', '/api/familias/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (familia) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/familias/${familia.id}`,
      }).then(() => {
        familia = undefined;
      });
    }
  });

  it('Familias menu should load Familias page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('familia');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Familia').should('exist');
    cy.url().should('match', familiaPageUrlPattern);
  });

  describe('Familia page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(familiaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Familia page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/familia/new$'));
        cy.getEntityCreateUpdateHeading('Familia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familiaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/familias',
          body: familiaSample,
        }).then(({ body }) => {
          familia = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/familias+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/familias?page=0&size=20>; rel="last",<http://localhost/api/familias?page=0&size=20>; rel="first"',
              },
              body: [familia],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(familiaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Familia page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('familia');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familiaPageUrlPattern);
      });

      it('edit button click should load edit Familia page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Familia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familiaPageUrlPattern);
      });

      it('edit button click should load edit Familia page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Familia');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familiaPageUrlPattern);
      });

      it('last delete button click should delete instance of Familia', () => {
        cy.intercept('GET', '/api/familias/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('familia').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', familiaPageUrlPattern);

        familia = undefined;
      });
    });
  });

  describe('new Familia page', () => {
    beforeEach(() => {
      cy.visit(`${familiaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Familia');
    });

    it('should create an instance of Familia', () => {
      cy.get(`[data-cy="nome"]`).type('escarlate Eletrônicos time-frame').should('have.value', 'escarlate Eletrônicos time-frame');

      cy.get(`[data-cy="ativa"]`).should('not.be.checked');
      cy.get(`[data-cy="ativa"]`).click().should('be.checked');

      cy.get(`[data-cy="renda"]`).type('845').should('have.value', '845');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        familia = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', familiaPageUrlPattern);
    });
  });
});
