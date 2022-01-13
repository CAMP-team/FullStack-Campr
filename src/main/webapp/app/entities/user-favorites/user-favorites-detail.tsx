import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './user-favorites.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserFavoritesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userFavoritesEntity = useAppSelector(state => state.userFavorites.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userFavoritesDetailsHeading">
          <Translate contentKey="camprApp.userFavorites.detail.title">UserFavorites</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userFavoritesEntity.id}</dd>
          <dt>
            <span id="dateAdded">
              <Translate contentKey="camprApp.userFavorites.dateAdded">Date Added</Translate>
            </span>
          </dt>
          <dd>
            {userFavoritesEntity.dateAdded ? (
              <TextFormat value={userFavoritesEntity.dateAdded} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="camprApp.userFavorites.appUser">App User</Translate>
          </dt>
          <dd>{userFavoritesEntity.appUser ? userFavoritesEntity.appUser.id : ''}</dd>
          <dt>
            <Translate contentKey="camprApp.userFavorites.video">Video</Translate>
          </dt>
          <dd>
            {userFavoritesEntity.videos
              ? userFavoritesEntity.videos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {userFavoritesEntity.videos && i === userFavoritesEntity.videos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-favorites" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-favorites/${userFavoritesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserFavoritesDetail;
