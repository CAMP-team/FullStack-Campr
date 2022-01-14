import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './user-comment.reducer';
import { IUserComment } from 'app/shared/model/user-comment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserComment = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const userCommentList = useAppSelector(state => state.userComment.entities);
  const loading = useAppSelector(state => state.userComment.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="user-comment-heading" data-cy="UserCommentHeading">
        <Translate contentKey="camprApp.userComment.home.title">User Comments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="secondary" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="camprApp.userComment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="camprApp.userComment.home.createLabel">Create new User Comment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userCommentList && userCommentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="camprApp.userComment.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userComment.commentBody">Comment Body</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userComment.commentDate">Comment Date</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userComment.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="camprApp.userComment.video">Video</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userCommentList.map((userComment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${userComment.id}`} color="link" size="sm">
                      {userComment.id}
                    </Button>
                  </td>
                  <td>{userComment.commentBody}</td>
                  <td>
                    {userComment.commentDate ? <TextFormat type="date" value={userComment.commentDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userComment.user ? userComment.user.login : ''}</td>
                  <td>{userComment.video ? <Link to={`video/${userComment.video.id}`}>{userComment.video.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${userComment.id}`} color="secondary" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userComment.id}/edit`} color="secondary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userComment.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="camprApp.userComment.home.notFound">No User Comments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserComment;
