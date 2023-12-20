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
import { IWord } from 'app/shared/model/word.model';
import { getEntity, updateEntity, createEntity, reset } from './word.reducer';

export const WordUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const archives = useAppSelector(state => state.archive.entities);
  const favorites = useAppSelector(state => state.favorites.entities);
  const wordEntity = useAppSelector(state => state.word.entity);
  const loading = useAppSelector(state => state.word.loading);
  const updating = useAppSelector(state => state.word.updating);
  const updateSuccess = useAppSelector(state => state.word.updateSuccess);

  const handleClose = () => {
    navigate('/word');
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
      ...wordEntity,
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
          ...wordEntity,
          archive: wordEntity?.archive?.id,
          favorites: wordEntity?.favorites?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lingosphere2App.word.home.createOrEditLabel" data-cy="WordCreateUpdateHeading">
            <Translate contentKey="lingosphere2App.word.home.createOrEditLabel">Create or edit a Word</Translate>
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
                  id="word-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lingosphere2App.word.language')}
                id="word-language"
                name="language"
                data-cy="language"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.word.wordText')}
                id="word-wordText"
                name="wordText"
                data-cy="wordText"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.word.partOfSpeech')}
                id="word-partOfSpeech"
                name="partOfSpeech"
                data-cy="partOfSpeech"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.word.pronunciation')}
                id="word-pronunciation"
                name="pronunciation"
                data-cy="pronunciation"
                type="text"
              />
              <ValidatedField label={translate('lingosphere2App.word.audio')} id="word-audio" name="audio" data-cy="audio" type="text" />
              <ValidatedField
                label={translate('lingosphere2App.word.definition')}
                id="word-definition"
                name="definition"
                data-cy="definition"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.word.exampleSentence')}
                id="word-exampleSentence"
                name="exampleSentence"
                data-cy="exampleSentence"
                type="text"
              />
              <ValidatedField
                label={translate('lingosphere2App.word.etymology')}
                id="word-etymology"
                name="etymology"
                data-cy="etymology"
                type="text"
              />
              <ValidatedField
                id="word-archive"
                name="archive"
                data-cy="archive"
                label={translate('lingosphere2App.word.archive')}
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
                id="word-favorites"
                name="favorites"
                data-cy="favorites"
                label={translate('lingosphere2App.word.favorites')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/word" replace color="info">
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

export default WordUpdate;
