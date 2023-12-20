import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './word.reducer';

export const WordDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const wordEntity = useAppSelector(state => state.word.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="wordDetailsHeading">
          <Translate contentKey="lingosphere2App.word.detail.title">Word</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{wordEntity.id}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="lingosphere2App.word.language">Language</Translate>
            </span>
          </dt>
          <dd>{wordEntity.language}</dd>
          <dt>
            <span id="wordText">
              <Translate contentKey="lingosphere2App.word.wordText">Word Text</Translate>
            </span>
          </dt>
          <dd>{wordEntity.wordText}</dd>
          <dt>
            <span id="partOfSpeech">
              <Translate contentKey="lingosphere2App.word.partOfSpeech">Part Of Speech</Translate>
            </span>
          </dt>
          <dd>{wordEntity.partOfSpeech}</dd>
          <dt>
            <span id="pronunciation">
              <Translate contentKey="lingosphere2App.word.pronunciation">Pronunciation</Translate>
            </span>
          </dt>
          <dd>{wordEntity.pronunciation}</dd>
          <dt>
            <span id="audio">
              <Translate contentKey="lingosphere2App.word.audio">Audio</Translate>
            </span>
          </dt>
          <dd>{wordEntity.audio}</dd>
          <dt>
            <span id="definition">
              <Translate contentKey="lingosphere2App.word.definition">Definition</Translate>
            </span>
          </dt>
          <dd>{wordEntity.definition}</dd>
          <dt>
            <span id="exampleSentence">
              <Translate contentKey="lingosphere2App.word.exampleSentence">Example Sentence</Translate>
            </span>
          </dt>
          <dd>{wordEntity.exampleSentence}</dd>
          <dt>
            <span id="etymology">
              <Translate contentKey="lingosphere2App.word.etymology">Etymology</Translate>
            </span>
          </dt>
          <dd>{wordEntity.etymology}</dd>
          <dt>
            <Translate contentKey="lingosphere2App.word.archive">Archive</Translate>
          </dt>
          <dd>{wordEntity.archive ? wordEntity.archive.id : ''}</dd>
          <dt>
            <Translate contentKey="lingosphere2App.word.favorites">Favorites</Translate>
          </dt>
          <dd>{wordEntity.favorites ? wordEntity.favorites.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/word" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/word/${wordEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WordDetail;
