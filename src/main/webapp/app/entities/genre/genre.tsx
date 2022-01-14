import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './genre.reducer';
import { IGenre } from 'app/shared/model/genre.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Genre = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const genreList = useAppSelector(state => state.genre.entities);
  const loading = useAppSelector(state => state.genre.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="genre-heading" data-cy="GenreHeading">
        <Translate contentKey="camprApp.genre.home.title">Genres</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="secondary" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="camprApp.genre.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="camprApp.genre.home.createLabel">Create new Genre</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {genreList && genreList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="camprApp.genre.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.genre.apiId">Api Id</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.genre.name">Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {genreList.map((genre, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${genre.id}`} color="link" size="sm">
                      {genre.id}
                    </Button>
                  </td>
                  <td>{genre.apiId}</td>
                  <td>{genre.name}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${genre.id}`} color="secondary" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${genre.id}/edit`} color="secondary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${genre.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="camprApp.genre.home.notFound">No Genres found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Genre;
