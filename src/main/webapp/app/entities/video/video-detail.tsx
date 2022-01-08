import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './video.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VideoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const videoEntity = useAppSelector(state => state.video.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="videoDetailsHeading">
          <Translate contentKey="camprApp.video.detail.title">Video</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{videoEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="camprApp.video.title">Title</Translate>
            </span>
          </dt>
          <dd>{videoEntity.title}</dd>
          <dt>
            <span id="imageUrl">
              <Translate contentKey="camprApp.video.imageUrl">Image Url</Translate>
            </span>
          </dt>
          <dd>{videoEntity.imageUrl}</dd>
          <dt>
            <span id="videoUrl">
              <Translate contentKey="camprApp.video.videoUrl">Video Url</Translate>
            </span>
          </dt>
          <dd>{videoEntity.videoUrl}</dd>
          <dt>
            <span id="trailerId">
              <Translate contentKey="camprApp.video.trailerId">Trailer Id</Translate>
            </span>
          </dt>
          <dd>{videoEntity.trailerId}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="camprApp.video.description">Description</Translate>
            </span>
          </dt>
          <dd>{videoEntity.description}</dd>
          <dt>
            <Translate contentKey="camprApp.video.userUpload">User Upload</Translate>
          </dt>
          <dd>{videoEntity.userUpload ? videoEntity.userUpload.id : ''}</dd>
          <dt>
            <Translate contentKey="camprApp.video.genre">Genre</Translate>
          </dt>
          <dd>
            {videoEntity.genres
              ? videoEntity.genres.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {videoEntity.genres && i === videoEntity.genres.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/video" replace color="secondary" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/video/${videoEntity.id}/edit`} replace color="secondary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VideoDetail;
