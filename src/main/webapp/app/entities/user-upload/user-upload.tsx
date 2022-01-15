import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './user-upload.reducer';
import { IUserUpload } from 'app/shared/model/user-upload.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserUpload = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const userUploadList = useAppSelector(state => state.userUpload.entities);
  const loading = useAppSelector(state => state.userUpload.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="user-upload-heading" data-cy="UserUploadHeading">
        <Translate contentKey="camprApp.userUpload.home.title">User Uploads</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="camprApp.userUpload.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="camprApp.userUpload.home.createLabel">Create new User Upload</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userUploadList && userUploadList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="camprApp.userUpload.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userUpload.dateUploaded">Date Uploaded</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userUpload.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userUploadList.map((userUpload, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${userUpload.id}`} color="link" size="sm">
                      {userUpload.id}
                    </Button>
                  </td>
                  <td>
                    {userUpload.dateUploaded ? <TextFormat type="date" value={userUpload.dateUploaded} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userUpload.user ? userUpload.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${userUpload.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userUpload.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userUpload.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="camprApp.userUpload.home.notFound">No User Uploads found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserUpload;
