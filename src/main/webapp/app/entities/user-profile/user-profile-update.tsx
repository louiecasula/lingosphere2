import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArchive } from 'app/shared/model/archive.model';
import { getEntities as getArchives } from 'app/entities/archive/archive.reducer';
import { IFavorites } from 'app/shared/model/favorites.model';
import { getEntities as getFavorites } from 'app/entities/favorites/favorites.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntity, updateEntity, createEntity, reset } from './user-profile.reducer';

export const UserProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const archives = useAppSelector(state => state.archive.entities);
  const favorites = useAppSelector(state => state.favorites.entities);
  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  const loading = useAppSelector(state => state.userProfile.loading);
  const updating = useAppSelector(state => state.userProfile.updating);
  const updateSuccess = useAppSelector(state => state.userProfile.updateSuccess);

  const handleClose = () => {
    navigate('/user-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getArchives({}));
    dispatch(getFavorites({}));
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

    const entity = {
      ...userProfileEntity,
      ...values,
      archive: archives.find(it => it.id.toString() === values.archive.toString()),
      favorites: favorites.find(it => it.id.toString() === values.favorites.toString()),
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
          ...userProfileEntity,
          archive: userProfileEntity?.archive?.id,
          favorites: userProfileEntity?.favorites?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lingosphere2App.userProfile.home.createOrEditLabel" data-cy="UserProfileCreateUpdateHeading">
            <Translate contentKey="lingosphere2App.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
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
                  id="user-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lingosphere2App.userProfile.name')}
                id="user-profile-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.userProfile.email')}
                id="user-profile-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.userProfile.password')}
                id="user-profile-password"
                name="password"
                data-cy="password"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.userProfile.nativeLanguage')}
                id="user-profile-nativeLanguage"
                name="nativeLanguage"
                data-cy="nativeLanguage"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.userProfile.targetLanguage')}
                id="user-profile-targetLanguage"
                name="targetLanguage"
                data-cy="targetLanguage"
                type="text"
              />
              <ValidatedField
                id="user-profile-archive"
                name="archive"
                data-cy="archive"
                label={translate('lingosphere2App.userProfile.archive')}
                type="select"
              >
                <option value="" key="0" />
                {archives
                  ? archives.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-profile-favorites"
                name="favorites"
                data-cy="favorites"
                label={translate('lingosphere2App.userProfile.favorites')}
                type="select"
              >
                <option value="" key="0" />
                {favorites
                  ? favorites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-profile" replace color="info">
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

export default UserProfileUpdate;
