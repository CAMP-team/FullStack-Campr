import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUserUpload } from 'app/shared/model/user-upload.model';
import { getEntities as getUserUploads } from 'app/entities/user-upload/user-upload.reducer';
import { IGenre } from 'app/shared/model/genre.model';
import { getEntities as getGenres } from 'app/entities/genre/genre.reducer';
import { IWatchHistory } from 'app/shared/model/watch-history.model';
import { getEntities as getWatchHistories } from 'app/entities/watch-history/watch-history.reducer';
import { IUserFavorites } from 'app/shared/model/user-favorites.model';
import { getEntities as getUserFavorites } from 'app/entities/user-favorites/user-favorites.reducer';
import { getEntity, updateEntity, createEntity, reset } from './video.reducer';
import { IVideo } from 'app/shared/model/video.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VideoUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const userUploads = useAppSelector(state => state.userUpload.entities);
  const genres = useAppSelector(state => state.genre.entities);
  const watchHistories = useAppSelector(state => state.watchHistory.entities);
  const userFavorites = useAppSelector(state => state.userFavorites.entities);
  const videoEntity = useAppSelector(state => state.video.entity);
  const loading = useAppSelector(state => state.video.loading);
  const updating = useAppSelector(state => state.video.updating);
  const updateSuccess = useAppSelector(state => state.video.updateSuccess);
  const handleClose = () => {
    props.history.push('/video');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUserUploads({}));
    dispatch(getGenres({}));
    dispatch(getWatchHistories({}));
    dispatch(getUserFavorites({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...videoEntity,
      ...values,
      genres: mapIdList(values.genres),
      userUpload: userUploads.find(it => it.id.toString() === values.userUpload.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...videoEntity,
          userUpload: videoEntity?.userUpload?.id,
          genres: videoEntity?.genres?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="camprApp.video.home.createOrEditLabel" data-cy="VideoCreateUpdateHeading">
            <Translate contentKey="camprApp.video.home.createOrEditLabel">Create or edit a Video</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="video-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('camprApp.video.title')} id="video-title" name="title" data-cy="title" type="text" />
              <ValidatedField
                label={translate('camprApp.video.imageUrl')}
                id="video-imageUrl"
                name="imageUrl"
                data-cy="imageUrl"
                type="text"
              />
              <ValidatedField
                label={translate('camprApp.video.videoUrl')}
                id="video-videoUrl"
                name="videoUrl"
                data-cy="videoUrl"
                type="text"
              />
              <ValidatedField
                label={translate('camprApp.video.trailerId')}
                id="video-trailerId"
                name="trailerId"
                data-cy="trailerId"
                type="text"
              />
              <ValidatedField
                label={translate('camprApp.video.description')}
                id="video-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="video-userUpload"
                name="userUpload"
                data-cy="userUpload"
                label={translate('camprApp.video.userUpload')}
                type="select"
              >
                <option value="" key="0" />
                {userUploads
                  ? userUploads.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('camprApp.video.genre')}
                id="video-genre"
                data-cy="genre"
                type="select"
                multiple
                name="genres"
              >
                <option value="" key="0" />
                {genres
                  ? genres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/video" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VideoUpdate;
