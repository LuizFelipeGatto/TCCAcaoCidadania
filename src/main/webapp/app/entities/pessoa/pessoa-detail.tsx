import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pessoa.reducer';

export const PessoaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pessoaEntity = useAppSelector(state => state.pessoa.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pessoaDetailsHeading">
          <Translate contentKey="acaoCidadaniaApp.pessoa.detail.title">Pessoa</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="acaoCidadaniaApp.pessoa.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.nome}</dd>
          <dt>
            <span id="cpf">
              <Translate contentKey="acaoCidadaniaApp.pessoa.cpf">Cpf</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.cpf}</dd>
          <dt>
            <span id="cidade">
              <Translate contentKey="acaoCidadaniaApp.pessoa.cidade">Cidade</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.cidade}</dd>
          <dt>
            <span id="logradouro">
              <Translate contentKey="acaoCidadaniaApp.pessoa.logradouro">Logradouro</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.logradouro}</dd>
          <dt>
            <span id="codigoIbge">
              <Translate contentKey="acaoCidadaniaApp.pessoa.codigoIbge">Codigo Ibge</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.codigoIbge}</dd>
          <dt>
            <span id="celular">
              <Translate contentKey="acaoCidadaniaApp.pessoa.celular">Celular</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.celular}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="acaoCidadaniaApp.pessoa.email">Email</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.email}</dd>
          <dt>
            <span id="beneficio">
              <Translate contentKey="acaoCidadaniaApp.pessoa.beneficio">Beneficio</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.beneficio}</dd>
          <dt>
            <Translate contentKey="acaoCidadaniaApp.pessoa.unidade">Unidade</Translate>
          </dt>
          <dd>{pessoaEntity.unidade ? pessoaEntity.unidade.nome : ''}</dd>
          <dt>
            <Translate contentKey="acaoCidadaniaApp.pessoa.familia">Familia</Translate>
          </dt>
          <dd>{pessoaEntity.familia ? pessoaEntity.familia.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/pessoa" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pessoa/${pessoaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PessoaDetail;
