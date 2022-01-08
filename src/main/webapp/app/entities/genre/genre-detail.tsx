import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './genre.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenreDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const genreEntity = useAppSelector(state => state.genre.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="genreDetailsHeading">
          <Translate contentKey="camprApp.genre.detail.title">Genre</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{genreEntity.id}</dd>
          <dt>
            <span id="apiId">
              <Translate contentKey="camprApp.genre.apiId">Api Id</Translate>
            </span>
          </dt>
          <dd>{genreEntity.apiId}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="camprApp.genre.name">Name</Translate>
            </span>
          </dt>
          <dd>{genreEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/genre" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/genre/${genreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GenreDetail;
