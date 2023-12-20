import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="lingosphere2App.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="lingosphere2App.userProfile.name">Name</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.name}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="lingosphere2App.userProfile.email">Email</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.email}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="lingosphere2App.userProfile.password">Password</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.password}</dd>
          <dt>
            <span id="nativeLanguage">
              <Translate contentKey="lingosphere2App.userProfile.nativeLanguage">Native Language</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.nativeLanguage}</dd>
          <dt>
            <span id="targetLanguage">
              <Translate contentKey="lingosphere2App.userProfile.targetLanguage">Target Language</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.targetLanguage}</dd>
          <dt>
            <Translate contentKey="lingosphere2App.userProfile.archive">Archive</Translate>
          </dt>
          <dd>{userProfileEntity.archive ? userProfileEntity.archive.id : ''}</dd>
          <dt>
            <Translate contentKey="lingosphere2App.userProfile.favorites">Favorites</Translate>
          </dt>
          <dd>{userProfileEntity.favorites ? userProfileEntity.favorites.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
