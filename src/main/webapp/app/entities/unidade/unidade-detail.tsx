import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './unidade.reducer';

export const UnidadeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const unidadeEntity = useAppSelector(state => state.unidade.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="unidadeDetailsHeading">
          <Translate contentKey="acaoCidadaniaApp.unidade.detail.title">Unidade</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{unidadeEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="acaoCidadaniaApp.unidade.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{unidadeEntity.nome}</dd>
          <dt>
            <span id="cep">
              <Translate contentKey="acaoCidadaniaApp.unidade.cep">Cep</Translate>
            </span>
          </dt>
          <dd>{unidadeEntity.cep}</dd>
          <dt>
            <span id="cidade">
              <Translate contentKey="acaoCidadaniaApp.unidade.cidade">Cidade</Translate>
            </span>
          </dt>
          <dd>{unidadeEntity.cidade}</dd>
          <dt>
            <span id="logradouro">
              <Translate contentKey="acaoCidadaniaApp.unidade.logradouro">Logradouro</Translate>
            </span>
          </dt>
          <dd>{unidadeEntity.logradouro}</dd>
          <dt>
            <span id="codigoIbge">
              <Translate contentKey="acaoCidadaniaApp.unidade.codigoIbge">Codigo Ibge</Translate>
            </span>
          </dt>
          <dd>{unidadeEntity.codigoIbge}</dd>
        </dl>
        <Button tag={Link} to="/unidade" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/unidade/${unidadeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UnidadeDetail;
