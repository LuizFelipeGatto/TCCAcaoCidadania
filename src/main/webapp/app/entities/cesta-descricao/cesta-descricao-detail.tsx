import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cesta-descricao.reducer';

export const CestaDescricaoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cestaDescricaoEntity = useAppSelector(state => state.cestaDescricao.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cestaDescricaoDetailsHeading">
          <Translate contentKey="acaoCidadaniaApp.cestaDescricao.detail.title">CestaDescricao</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cestaDescricaoEntity.id}</dd>
          <dt>
            <span id="descricao">
              <Translate contentKey="acaoCidadaniaApp.cestaDescricao.descricao">Descrição</Translate>
            </span>
          </dt>
          <dd>{cestaDescricaoEntity.descricao}</dd>
        </dl>
        <Button tag={Link} to="/cesta-descricao" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cesta-descricao/${cestaDescricaoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CestaDescricaoDetail;
