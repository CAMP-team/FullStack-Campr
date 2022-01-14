import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './user-favorites.reducer';
import { IUserFavorites } from 'app/shared/model/user-favorites.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserFavorites = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const userFavoritesList = useAppSelector(state => state.userFavorites.entities);
  const loading = useAppSelector(state => state.userFavorites.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="user-favorites-heading" data-cy="UserFavoritesHeading">
        <Translate contentKey="camprApp.userFavorites.home.title">User Favorites</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="camprApp.userFavorites.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="camprApp.userFavorites.home.createLabel">Create new User Favorites</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userFavoritesList && userFavoritesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="camprApp.userFavorites.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userFavorites.dateAdded">Date Added</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userFavorites.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userFavorites.video">Video</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userFavoritesList.map((userFavorites, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${userFavorites.id}`} color="link" size="sm">
                      {userFavorites.id}
                    </Button>
                  </td>
                  <td>
                    {userFavorites.dateAdded ? <TextFormat type="date" value={userFavorites.dateAdded} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userFavorites.user ? userFavorites.user.login : ''}</td>
                  <td>
                    {userFavorites.videos
                      ? userFavorites.videos.map((val, j) => (
                          <span key={j}>
                            <Link to={`video/${val.id}`}>{val.title}</Link>
                            {j === userFavorites.videos.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${userFavorites.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userFavorites.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${userFavorites.id}/delete`}
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
              <Translate contentKey="camprApp.userFavorites.home.notFound">No User Favorites found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserFavorites;
