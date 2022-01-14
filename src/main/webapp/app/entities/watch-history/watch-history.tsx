import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './watch-history.reducer';
import { IWatchHistory } from 'app/shared/model/watch-history.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WatchHistory = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const watchHistoryList = useAppSelector(state => state.watchHistory.entities);
  const loading = useAppSelector(state => state.watchHistory.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="watch-history-heading" data-cy="WatchHistoryHeading">
        <Translate contentKey="camprApp.watchHistory.home.title">Watch Histories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="camprApp.watchHistory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="camprApp.watchHistory.home.createLabel">Create new Watch History</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {watchHistoryList && watchHistoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="camprApp.watchHistory.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.watchHistory.dateWatched">Date Watched</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.watchHistory.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.watchHistory.video">Video</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {watchHistoryList.map((watchHistory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${watchHistory.id}`} color="link" size="sm">
                      {watchHistory.id}
                    </Button>
                  </td>
                  <td>
                    {watchHistory.dateWatched ? <TextFormat type="date" value={watchHistory.dateWatched} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{watchHistory.user ? watchHistory.user.login : ''}</td>
                  <td>
                    {watchHistory.videos
                      ? watchHistory.videos.map((val, j) => (
                          <span key={j}>
                            <Link to={`video/${val.id}`}>{val.title}</Link>
                            {j === watchHistory.videos.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${watchHistory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${watchHistory.id}/edit`} color="secondary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${watchHistory.id}/delete`}
                        color="secondary"
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
              <Translate contentKey="camprApp.watchHistory.home.notFound">No Watch Histories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default WatchHistory;
