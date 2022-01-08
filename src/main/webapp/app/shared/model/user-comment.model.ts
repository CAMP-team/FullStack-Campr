import dayjs from 'dayjs';
import { IAppUser } from 'app/shared/model/app-user.model';
import { IVideo } from 'app/shared/model/video.model';

export interface IUserComment {
  id?: number;
  commentBody?: string | null;
  commentDate?: string | null;
  appUser?: IAppUser | null;
  video?: IVideo | null;
}

export const defaultValue: Readonly<IUserComment> = {};
