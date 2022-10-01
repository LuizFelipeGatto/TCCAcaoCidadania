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

describe('Doacao e2e test', () => {
  const doacaoPageUrl = '/doacao';
  const doacaoPageUrlPattern = new RegExp('/doacao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const doacaoSample = { data: '2022-10-01' };

  let doacao;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/doacaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/doacaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/doacaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (doacao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/doacaos/${doacao.id}`,
      }).then(() => {
        doacao = undefined;
      });
    }
  });

  it('Doacaos menu should load Doacaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('doacao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Doacao').should('exist');
    cy.url().should('match', doacaoPageUrlPattern);
  });

  describe('Doacao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(doacaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Doacao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/doacao/new$'));
        cy.getEntityCreateUpdateHeading('Doacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doacaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/doacaos',
          body: doacaoSample,
        }).then(({ body }) => {
          doacao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/doacaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/doacaos?page=0&size=20>; rel="last",<http://localhost/api/doacaos?page=0&size=20>; rel="first"',
              },
              body: [doacao],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(doacaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Doacao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('doacao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doacaoPageUrlPattern);
      });

      it('edit button click should load edit Doacao page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Doacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doacaoPageUrlPattern);
      });

      it('edit button click should load edit Doacao page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Doacao');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doacaoPageUrlPattern);
      });

      it('last delete button click should delete instance of Doacao', () => {
        cy.intercept('GET', '/api/doacaos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('doacao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doacaoPageUrlPattern);

        doacao = undefined;
      });
    });
  });

  describe('new Doacao page', () => {
    beforeEach(() => {
      cy.visit(`${doacaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Doacao');
    });

    it('should create an instance of Doacao', () => {
      cy.get(`[data-cy="data"]`).type('2022-10-01').blur().should('have.value', '2022-10-01');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        doacao = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', doacaoPageUrlPattern);
    });
  });
});
