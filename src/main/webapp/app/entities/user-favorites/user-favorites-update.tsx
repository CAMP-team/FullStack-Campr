import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IVideo } from 'app/shared/model/video.model';
import { getEntities as getVideos } from 'app/entities/video/video.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-favorites.reducer';
// import { IUserFavorites } from 'app/shared/model/user-favorites.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserFavoritesUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  // might need this variable to tell if it is new
  // but the question is new in comparison to what?
  // if the whole faves is new or if the latest addition is a new video?
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const videos = useAppSelector(state => state.video.entities);
  // do i need to select all users and all videos before getting userFaves?
  const userFavoritesEntity = useAppSelector(state => state.userFavorites.entity);
  const loading = useAppSelector(state => state.userFavorites.loading);
  const updating = useAppSelector(state => state.userFavorites.updating);
  const updateSuccess = useAppSelector(state => state.userFavorites.updateSuccess);
  // this just redirects users to another page
  const handleClose = () => {
    props.history.push('/user-favorites');
  };
  // find out what use effect is (runs every time the render is changed)
  // how does isNew work here?

  // what is props.match.params.id doing in the context of getEntity of userFavorites

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getVideos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);
  // important: this is what creates a new favorite
  const saveEntity = values => {
    values.dateAdded = convertDateTimeToServer(values.dateAdded);
    // what do these ellipses mean?
    // what is mapIdList
    // what is values.videos
    // what exactly is values; is it just the parameter name for saveEntity
    // values seems like it is the output of the form that the user fills out
    // what are the necessary fill ins for entity
    const entity = {
      ...userFavoritesEntity,
      ...values,
      videos: mapIdList(values.videos),
      user: users.find(it => it.id.toString() === values.user.toString()),
    };
    // dispatch(createEntity(entity)) will add video to favorites ?
    if (isNew) {
      dispatch(createEntity(entity));
      // dispatch(deleteEntity(entity)) will remove video from favorites;
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateAdded: displayDefaultDateTime(),
        }
      : {
          ...userFavoritesEntity,
          dateAdded: convertDateTimeFromServer(userFavoritesEntity.dateAdded),
          user: userFavoritesEntity?.user?.id,
          videos: userFavoritesEntity?.videos?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="camprApp.userFavorites.home.createOrEditLabel" data-cy="UserFavoritesCreateUpdateHeading">
            <Translate contentKey="camprApp.userFavorites.home.createOrEditLabel">Create or edit a UserFavorites</Translate>
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
                  id="user-favorites-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('camprApp.userFavorites.dateAdded')}
                id="user-favorites-dateAdded"
                name="dateAdded"
                data-cy="dateAdded"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-favorites-user"
                name="user"
                data-cy="user"
                label={translate('camprApp.userFavorites.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('camprApp.userFavorites.video')}
                id="user-favorites-video"
                data-cy="video"
                type="select"
                multiple
                name="videos"
              >
                <option value="" key="0" />
                {videos
                  ? videos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-favorites" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="secondary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
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

export default UserFavoritesUpdate;
