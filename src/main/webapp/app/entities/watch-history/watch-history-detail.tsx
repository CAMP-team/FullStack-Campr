import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './watch-history.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WatchHistoryDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const watchHistoryEntity = useAppSelector(state => state.watchHistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="watchHistoryDetailsHeading">
          <Translate contentKey="camprApp.watchHistory.detail.title">WatchHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{watchHistoryEntity.id}</dd>
          <dt>
            <span id="dateWatched">
              <Translate contentKey="camprApp.watchHistory.dateWatched">Date Watched</Translate>
            </span>
          </dt>
          <dd>
            {watchHistoryEntity.dateWatched ? (
              <TextFormat value={watchHistoryEntity.dateWatched} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="camprApp.watchHistory.appUser">App User</Translate>
          </dt>
          <dd>{watchHistoryEntity.appUser ? watchHistoryEntity.appUser.id : ''}</dd>
          <dt>
            <Translate contentKey="camprApp.watchHistory.video">Video</Translate>
          </dt>
          <dd>
            {watchHistoryEntity.videos
              ? watchHistoryEntity.videos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {watchHistoryEntity.videos && i === watchHistoryEntity.videos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/watch-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/watch-history/${watchHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WatchHistoryDetail;
