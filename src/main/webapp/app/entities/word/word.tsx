import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './word.reducer';

export const Word = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const wordList = useAppSelector(state => state.word.entities);
  const loading = useAppSelector(state => state.word.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="word-heading" data-cy="WordHeading">
        <Translate contentKey="lingosphere2App.word.home.title">Words</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="lingosphere2App.word.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/word/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="lingosphere2App.word.home.createLabel">Create new Word</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {wordList && wordList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="lingosphere2App.word.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('language')}>
                  <Translate contentKey="lingosphere2App.word.language">Language</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('language')} />
                </th>
                <th className="hand" onClick={sort('wordText')}>
                  <Translate contentKey="lingosphere2App.word.wordText">Word Text</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('wordText')} />
                </th>
                <th className="hand" onClick={sort('partOfSpeech')}>
                  <Translate contentKey="lingosphere2App.word.partOfSpeech">Part Of Speech</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('partOfSpeech')} />
                </th>
                <th className="hand" onClick={sort('pronunciation')}>
                  <Translate contentKey="lingosphere2App.word.pronunciation">Pronunciation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pronunciation')} />
                </th>
                <th className="hand" onClick={sort('audio')}>
                  <Translate contentKey="lingosphere2App.word.audio">Audio</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('audio')} />
                </th>
                <th className="hand" onClick={sort('definition')}>
                  <Translate contentKey="lingosphere2App.word.definition">Definition</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('definition')} />
                </th>
                <th className="hand" onClick={sort('exampleSentence')}>
                  <Translate contentKey="lingosphere2App.word.exampleSentence">Example Sentence</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('exampleSentence')} />
                </th>
                <th className="hand" onClick={sort('etymology')}>
                  <Translate contentKey="lingosphere2App.word.etymology">Etymology</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('etymology')} />
                </th>
                <th>
                  <Translate contentKey="lingosphere2App.word.archive">Archive</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="lingosphere2App.word.favorites">Favorites</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {wordList.map((word, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/word/${word.id}`} color="link" size="sm">
                      {word.id}
                    </Button>
                  </td>
                  <td>{word.language}</td>
                  <td>{word.wordText}</td>
                  <td>{word.partOfSpeech}</td>
                  <td>{word.pronunciation}</td>
                  <td>{word.audio}</td>
                  <td>{word.definition}</td>
                  <td>{word.exampleSentence}</td>
                  <td>{word.etymology}</td>
                  <td>{word.archive ? <Link to={`/archive/${word.archive.id}`}>{word.archive.id}</Link> : ''}</td>
                  <td>{word.favorites ? <Link to={`/favorites/${word.favorites.id}`}>{word.favorites.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/word/${word.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/word/${word.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/word/${word.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="lingosphere2App.word.home.notFound">No Words found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Word;
