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

describe('CestaDescricao e2e test', () => {
  const cestaDescricaoPageUrl = '/cesta-descricao';
  const cestaDescricaoPageUrlPattern = new RegExp('/cesta-descricao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cestaDescricaoSample = { descricao: 'Auto' };

  let cestaDescricao;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cesta-descricaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cesta-descricaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cesta-descricaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cestaDescricao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cesta-descricaos/${cestaDescricao.id}`,
      }).then(() => {
        cestaDescricao = undefined;
      });
    }
  });

  it('CestaDescricaos menu should load CestaDescricaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cesta-descricao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CestaDescricao').should('exist');
    cy.url().should('match', cestaDescricaoPageUrlPattern);
  });

  describe('CestaDescricao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cestaDescricaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CestaDescricao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cesta-descricao/new$'));
        cy.getEntityCreateUpdateHeading('CestaDescricao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cestaDescricaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cesta-descricaos',
          body: cestaDescricaoSample,
        }).then(({ body }) => {
          cestaDescricao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cesta-descricaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cesta-descricaos?page=0&size=20>; rel="last",<http://localhost/api/cesta-descricaos?page=0&size=20>; rel="first"',
              },
              body: [cestaDescricao],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cestaDescricaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CestaDescricao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cestaDescricao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cestaDescricaoPageUrlPattern);
      });

      it('edit button click should load edit CestaDescricao page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CestaDescricao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cestaDescricaoPageUrlPattern);
      });

      it('edit button click should load edit CestaDescricao page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CestaDescricao');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cestaDescricaoPageUrlPattern);
      });

      it('last delete button click should delete instance of CestaDescricao', () => {
        cy.intercept('GET', '/api/cesta-descricaos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cestaDescricao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cestaDescricaoPageUrlPattern);

        cestaDescricao = undefined;
      });
    });
  });

  describe('new CestaDescricao page', () => {
    beforeEach(() => {
      cy.visit(`${cestaDescricaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CestaDescricao');
    });

    it('should create an instance of CestaDescricao', () => {
      cy.get(`[data-cy="descricao"]`).type('Usability').should('have.value', 'Usability');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        cestaDescricao = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', cestaDescricaoPageUrlPattern);
    });
  });
});
