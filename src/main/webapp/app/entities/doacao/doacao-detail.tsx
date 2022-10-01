import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './doacao.reducer';

export const DoacaoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const doacaoEntity = useAppSelector(state => state.doacao.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="doacaoDetailsHeading">
          <Translate contentKey="acaoCidadaniaApp.doacao.detail.title">Doacao</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{doacaoEntity.id}</dd>
          <dt>
            <span id="data">
              <Translate contentKey="acaoCidadaniaApp.doacao.data">Data</Translate>
            </span>
          </dt>
          <dd>{doacaoEntity.data ? <TextFormat value={doacaoEntity.data} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="acaoCidadaniaApp.doacao.cesta">Cesta</Translate>
          </dt>
          <dd>{doacaoEntity.cesta ? doacaoEntity.cesta.descricao : ''}</dd>
          <dt>
            <Translate contentKey="acaoCidadaniaApp.doacao.familia">Familia</Translate>
          </dt>
          <dd>{doacaoEntity.familia ? doacaoEntity.familia.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/doacao" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/doacao/${doacaoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DoacaoDetail;
