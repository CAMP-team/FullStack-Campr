import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IVideo } from 'app/shared/model/video.model';
import { getEntities as getVideos } from 'app/entities/video/video.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-comment.reducer';
import { IUserComment } from 'app/shared/model/user-comment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserCommentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const videos = useAppSelector(state => state.video.entities);
  const userCommentEntity = useAppSelector(state => state.userComment.entity);
  const loading = useAppSelector(state => state.userComment.loading);
  const updating = useAppSelector(state => state.userComment.updating);
  const updateSuccess = useAppSelector(state => state.userComment.updateSuccess);
  const handleClose = () => {
    props.history.push('/user-comment');
  };

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

  const saveEntity = values => {
    values.commentDate = convertDateTimeToServer(values.commentDate);

    const entity = {
      ...userCommentEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      video: videos.find(it => it.id.toString() === values.video.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          commentDate: displayDefaultDateTime(),
        }
      : {
          ...userCommentEntity,
          commentDate: convertDateTimeFromServer(userCommentEntity.commentDate),
          user: userCommentEntity?.user?.id,
          video: userCommentEntity?.video?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="camprApp.userComment.home.createOrEditLabel" data-cy="UserCommentCreateUpdateHeading">
            <Translate contentKey="camprApp.userComment.home.createOrEditLabel">Create or edit a UserComment</Translate>
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
                  id="user-comment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('camprApp.userComment.commentBody')}
                id="user-comment-commentBody"
                name="commentBody"
                data-cy="commentBody"
                type="text"
              />
              <ValidatedField
                label={translate('camprApp.userComment.commentDate')}
                id="user-comment-commentDate"
                name="commentDate"
                data-cy="commentDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-comment-user"
                name="user"
                data-cy="user"
                label={translate('camprApp.userComment.user')}
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
                id="user-comment-video"
                name="video"
                data-cy="video"
                label={translate('camprApp.userComment.video')}
                type="select"
              >
                <option value="" key="0" />
                {videos
                  ? videos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-comment" replace color="info">
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

export default UserCommentUpdate;
