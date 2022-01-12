import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './user-upload.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserUploadDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userUploadEntity = useAppSelector(state => state.userUpload.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userUploadDetailsHeading">
          <Translate contentKey="camprApp.userUpload.detail.title">UserUpload</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userUploadEntity.id}</dd>
          <dt>
            <span id="dateUploaded">
              <Translate contentKey="camprApp.userUpload.dateUploaded">Date Uploaded</Translate>
            </span>
          </dt>
          <dd>
            {userUploadEntity.dateUploaded ? (
              <TextFormat value={userUploadEntity.dateUploaded} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="camprApp.userUpload.appUser">App User</Translate>
          </dt>
          <dd>{userUploadEntity.appUser ? userUploadEntity.appUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-upload" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-upload/${userUploadEntity.id}/edit`} replace color="secondary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserUploadDetail;
