import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './familia.reducer';

export const FamiliaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const familiaEntity = useAppSelector(state => state.familia.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="familiaDetailsHeading">
          <Translate contentKey="acaoCidadaniaApp.familia.detail.title">Familia</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{familiaEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="acaoCidadaniaApp.familia.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{familiaEntity.nome}</dd>
          <dt>
            <span id="ativa">
              <Translate contentKey="acaoCidadaniaApp.familia.ativa">Ativa</Translate>
            </span>
          </dt>
          <dd>{familiaEntity.ativa ? 'true' : 'false'}</dd>
          <dt>
            <span id="renda">
              <Translate contentKey="acaoCidadaniaApp.familia.renda">Renda</Translate>
            </span>
          </dt>
          <dd>{familiaEntity.renda}</dd>
        </dl>
        <Button tag={Link} to="/familia" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/familia/${familiaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FamiliaDetail;
