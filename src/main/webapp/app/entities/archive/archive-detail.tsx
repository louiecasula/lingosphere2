import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './archive.reducer';

export const ArchiveDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const archiveEntity = useAppSelector(state => state.archive.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="archiveDetailsHeading">
          <Translate contentKey="lingosphere2App.archive.detail.title">Archive</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{archiveEntity.id}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="lingosphere2App.archive.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>{archiveEntity.timestamp ? <TextFormat value={archiveEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="proficiencyLvl">
              <Translate contentKey="lingosphere2App.archive.proficiencyLvl">Proficiency Lvl</Translate>
            </span>
          </dt>
          <dd>{archiveEntity.proficiencyLvl}</dd>
        </dl>
        <Button tag={Link} to="/archive" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/archive/${archiveEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArchiveDetail;
