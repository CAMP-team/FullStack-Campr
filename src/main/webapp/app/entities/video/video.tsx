import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './video.reducer';
import { IVideo } from 'app/shared/model/video.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Video = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const videoList = useAppSelector(state => state.video.entities);
  const loading = useAppSelector(state => state.video.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="video-heading" data-cy="VideoHeading">
        <Translate contentKey="camprApp.video.home.title">Videos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="secondary" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="camprApp.video.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-secondary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="camprApp.video.home.createLabel">Create new Video</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {videoList && videoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="camprApp.video.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.video.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.video.imageUrl">Image Url</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.video.videoUrl">Video Url</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.video.trailerId">Trailer Id</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.video.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.video.userUpload">User Upload</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.video.genre">Genre</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {videoList.map((video, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${video.id}`} color="link" size="sm">
                      {video.id}
                    </Button>
                  </td>
                  <td>{video.title}</td>
                  <td>{video.imageUrl}</td>
                  <td>{video.videoUrl}</td>
                  <td>{video.trailerId}</td>
                  <td>{video.description}</td>
                  <td>{video.userUpload ? <Link to={`user-upload/${video.userUpload.id}`}>{video.userUpload.id}</Link> : ''}</td>
                  <td>
                    {video.genres
                      ? video.genres.map((val, j) => (
                          <span key={j}>
                            <Link to={`genre/${val.id}`}>{val.name}</Link>
                            {j === video.genres.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${video.id}`} color="secondary" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${video.id}/edit`} color="secondary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${video.id}/delete`} color="secondary" size="sm" data-cy="entityDeleteButton">
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
            <div className="alert alert-light">
              <Translate contentKey="camprApp.video.home.notFound">No Videos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Video;
