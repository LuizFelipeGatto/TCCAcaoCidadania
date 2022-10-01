import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUnidade } from 'app/shared/model/unidade.model';
import { getEntities as getUnidades } from 'app/entities/unidade/unidade.reducer';
import { IFamilia } from 'app/shared/model/familia.model';
import { getEntities as getFamilias } from 'app/entities/familia/familia.reducer';
import { IPessoa } from 'app/shared/model/pessoa.model';
import { Beneficio } from 'app/shared/model/enumerations/beneficio.model';
import { getEntity, updateEntity, createEntity, reset } from './pessoa.reducer';

export const PessoaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const unidades = useAppSelector(state => state.unidade.entities);
  const familias = useAppSelector(state => state.familia.entities);
  const pessoaEntity = useAppSelector(state => state.pessoa.entity);
  const loading = useAppSelector(state => state.pessoa.loading);
  const updating = useAppSelector(state => state.pessoa.updating);
  const updateSuccess = useAppSelector(state => state.pessoa.updateSuccess);
  const beneficioValues = Object.keys(Beneficio);

  const handleClose = () => {
    navigate('/pessoa' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUnidades({}));
    dispatch(getFamilias({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...pessoaEntity,
      ...values,
      unidade: unidades.find(it => it.id.toString() === values.unidade.toString()),
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
          beneficio: 'BOLSA_FAMILIA',
          ...pessoaEntity,
          unidade: pessoaEntity?.unidade?.id,
          familia: pessoaEntity?.familia?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="acaoCidadaniaApp.pessoa.home.createOrEditLabel" data-cy="PessoaCreateUpdateHeading">
            <Translate contentKey="acaoCidadaniaApp.pessoa.home.createOrEditLabel">Create or edit a Pessoa</Translate>
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
                  id="pessoa-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('acaoCidadaniaApp.pessoa.nome')}
                id="pessoa-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('acaoCidadaniaApp.pessoa.cpf')}
                id="pessoa-cpf"
                name="cpf"
                data-cy="cpf"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 14, message: translate('entity.validation.minlength', { min: 14 }) },
                  maxLength: { value: 14, message: translate('entity.validation.maxlength', { max: 14 }) },
                }}
              />
              <ValidatedField
                label={translate('acaoCidadaniaApp.pessoa.cidade')}
                id="pessoa-cidade"
                name="cidade"
                data-cy="cidade"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 150, message: translate('entity.validation.maxlength', { max: 150 }) },
                }}
              />
              <ValidatedField
                label={translate('acaoCidadaniaApp.pessoa.logradouro')}
                id="pessoa-logradouro"
                name="logradouro"
                data-cy="logradouro"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('acaoCidadaniaApp.pessoa.codigoIbge')}
                id="pessoa-codigoIbge"
                name="codigoIbge"
                data-cy="codigoIbge"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('acaoCidadaniaApp.pessoa.celular')}
                id="pessoa-celular"
                name="celular"
                data-cy="celular"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('acaoCidadaniaApp.pessoa.email')}
                id="pessoa-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('acaoCidadaniaApp.pessoa.beneficio')}
                id="pessoa-beneficio"
                name="beneficio"
                data-cy="beneficio"
                type="select"
              >
                {beneficioValues.map(beneficio => (
                  <option value={beneficio} key={beneficio}>
                    {translate('acaoCidadaniaApp.Beneficio.' + beneficio)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="pessoa-unidade"
                name="unidade"
                data-cy="unidade"
                label={translate('acaoCidadaniaApp.pessoa.unidade')}
                type="select"
              >
                <option value="" key="0" />
                {unidades
                  ? unidades.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="pessoa-familia"
                name="familia"
                data-cy="familia"
                label={translate('acaoCidadaniaApp.pessoa.familia')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pessoa" replace color="info">
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

export default PessoaUpdate;
