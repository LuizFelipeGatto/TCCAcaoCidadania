import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICestaDescricao } from 'app/shared/model/cesta-descricao.model';
import { getEntities as getCestaDescricaos } from 'app/entities/cesta-descricao/cesta-descricao.reducer';
import { IFamilia } from 'app/shared/model/familia.model';
import { getEntities as getFamilias } from 'app/entities/familia/familia.reducer';
import { IDoacao } from 'app/shared/model/doacao.model';
import { getEntity, updateEntity, createEntity, reset } from './doacao.reducer';

export const DoacaoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cestaDescricaos = useAppSelector(state => state.cestaDescricao.entities);
  const familias = useAppSelector(state => state.familia.entities);
  const doacaoEntity = useAppSelector(state => state.doacao.entity);
  const loading = useAppSelector(state => state.doacao.loading);
  const updating = useAppSelector(state => state.doacao.updating);
  const updateSuccess = useAppSelector(state => state.doacao.updateSuccess);

  const handleClose = () => {
    navigate('/doacao' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCestaDescricaos({}));
    dispatch(getFamilias({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...doacaoEntity,
      ...values,
      cesta: cestaDescricaos.find(it => it.id.toString() === values.cesta.toString()),
      familia: familias.find(it => it.id.toString() === values.familia.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...doacaoEntity,
          cesta: doacaoEntity?.cesta?.id,
          familia: doacaoEntity?.familia?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="acaoCidadaniaApp.doacao.home.createOrEditLabel" data-cy="DoacaoCreateUpdateHeading">
            <Translate contentKey="acaoCidadaniaApp.doacao.home.createOrEditLabel">Create or edit a Doacao</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="doacao-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              {/* <ValidatedField
                label={translate('acaoCidadaniaApp.doacao.data')}
                id="doacao-data"
                name="data"
                data-cy="data"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              /> */}
              <ValidatedField
                id="doacao-cesta"
                name="cesta"
                data-cy="cesta"
                label={translate('acaoCidadaniaApp.doacao.cesta')}
                type="select"
              >
                <option value="" key="0" />
                {cestaDescricaos
                  ? cestaDescricaos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.descricao}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="doacao-familia"
                name="familia"
                data-cy="familia"
                label={translate('acaoCidadaniaApp.doacao.familia')}
                type="select"
              >
                <option value="" key="0" />
                {familias
                  ? familias.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/doacao" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DoacaoUpdate;
