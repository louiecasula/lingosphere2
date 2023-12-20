import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IFavorites } from 'app/shared/model/favorites.model';
import { getEntity, updateEntity, createEntity, reset } from './favorites.reducer';

export const FavoritesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const favoritesEntity = useAppSelector(state => state.favorites.entity);
  const loading = useAppSelector(state => state.favorites.loading);
  const updating = useAppSelector(state => state.favorites.updating);
  const updateSuccess = useAppSelector(state => state.favorites.updateSuccess);

  const handleClose = () => {
    navigate('/favorites');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.timestamp = convertDateTimeToServer(values.timestamp);
    if (values.proficiencyLvl !== undefined && typeof values.proficiencyLvl !== 'number') {
      values.proficiencyLvl = Number(values.proficiencyLvl);
    }

    const entity = {
      ...favoritesEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          timestamp: displayDefaultDateTime(),
        }
      : {
          ...favoritesEntity,
          timestamp: convertDateTimeFromServer(favoritesEntity.timestamp),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lingosphere2App.favorites.home.createOrEditLabel" data-cy="FavoritesCreateUpdateHeading">
            <Translate contentKey="lingosphere2App.favorites.home.createOrEditLabel">Create or edit a Favorites</Translate>
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
                  id="favorites-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lingosphere2App.favorites.timestamp')}
                id="favorites-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('lingosphere2App.favorites.proficiencyLvl')}
                id="favorites-proficiencyLvl"
                name="proficiencyLvl"
                data-cy="proficiencyLvl"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/favorites" replace color="info">
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

export default FavoritesUpdate;
