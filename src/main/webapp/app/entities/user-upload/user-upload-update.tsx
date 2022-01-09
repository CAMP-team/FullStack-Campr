import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IAppUser } from 'app/shared/model/app-user.model';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-upload.reducer';
import { IUserUpload } from 'app/shared/model/user-upload.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserUploadUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const appUsers = useAppSelector(state => state.appUser.entities);
  const userUploadEntity = useAppSelector(state => state.userUpload.entity);
  const loading = useAppSelector(state => state.userUpload.loading);
  const updating = useAppSelector(state => state.userUpload.updating);
  const updateSuccess = useAppSelector(state => state.userUpload.updateSuccess);
  const handleClose = () => {
    props.history.push('/user-upload' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getAppUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateUploaded = convertDateTimeToServer(values.dateUploaded);

    const entity = {
      ...userUploadEntity,
      ...values,
      appUser: appUsers.find(it => it.id.toString() === values.appUser.toString()),
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
          dateUploaded: displayDefaultDateTime(),
        }
      : {
          ...userUploadEntity,
          dateUploaded: convertDateTimeFromServer(userUploadEntity.dateUploaded),
          appUser: userUploadEntity?.appUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="camprApp.userUpload.home.createOrEditLabel" data-cy="UserUploadCreateUpdateHeading">
            <Translate contentKey="camprApp.userUpload.home.createOrEditLabel">Create or edit a UserUpload</Translate>
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
                  id="user-upload-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('camprApp.userUpload.dateUploaded')}
                id="user-upload-dateUploaded"
                name="dateUploaded"
                data-cy="dateUploaded"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-upload-appUser"
                name="appUser"
                data-cy="appUser"
                label={translate('camprApp.userUpload.appUser')}
                type="select"
              >
                <option value="" key="0" />
                {appUsers
                  ? appUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-upload" replace color="info">
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

export default UserUploadUpdate;
