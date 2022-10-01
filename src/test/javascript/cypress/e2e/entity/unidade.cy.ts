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

describe('Unidade e2e test', () => {
  const unidadePageUrl = '/unidade';
  const unidadePageUrlPattern = new RegExp('/unidade(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const unidadeSample = {
    nome: 'Gana synergistic Santa',
    cep: 'Feito Música',
    cidade: 'Queijo',
    logradouro: 'Officer transmit',
    codigoIbge: 'withdrawal Livros high-level',
  };

  let unidade;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/unidades+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/unidades').as('postEntityRequest');
    cy.intercept('DELETE', '/api/unidades/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (unidade) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/unidades/${unidade.id}`,
      }).then(() => {
        unidade = undefined;
      });
    }
  });

  it('Unidades menu should load Unidades page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('unidade');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Unidade').should('exist');
    cy.url().should('match', unidadePageUrlPattern);
  });

  describe('Unidade page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(unidadePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Unidade page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/unidade/new$'));
        cy.getEntityCreateUpdateHeading('Unidade');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', unidadePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/unidades',
          body: unidadeSample,
        }).then(({ body }) => {
          unidade = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/unidades+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/unidades?page=0&size=20>; rel="last",<http://localhost/api/unidades?page=0&size=20>; rel="first"',
              },
              body: [unidade],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(unidadePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Unidade page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('unidade');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', unidadePageUrlPattern);
      });

      it('edit button click should load edit Unidade page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Unidade');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', unidadePageUrlPattern);
      });

      it('edit button click should load edit Unidade page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Unidade');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', unidadePageUrlPattern);
      });

      it('last delete button click should delete instance of Unidade', () => {
        cy.intercept('GET', '/api/unidades/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('unidade').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', unidadePageUrlPattern);

        unidade = undefined;
      });
    });
  });

  describe('new Unidade page', () => {
    beforeEach(() => {
      cy.visit(`${unidadePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Unidade');
    });

    it('should create an instance of Unidade', () => {
      cy.get(`[data-cy="nome"]`).type('Distrito bypassing').should('have.value', 'Distrito bypassing');

      cy.get(`[data-cy="cep"]`).type('Architect').should('have.value', 'Architect');

      cy.get(`[data-cy="cidade"]`).type('Honduras Principal Savings').should('have.value', 'Honduras Principal Savings');

      cy.get(`[data-cy="logradouro"]`).type('seamless Macao Kroon').should('have.value', 'seamless Macao Kroon');

      cy.get(`[data-cy="codigoIbge"]`).type('Catarina Inteligente').should('have.value', 'Catarina Inteligente');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        unidade = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', unidadePageUrlPattern);
    });
  });
});
