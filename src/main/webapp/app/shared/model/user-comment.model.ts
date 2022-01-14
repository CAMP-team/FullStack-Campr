import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IVideo } from 'app/shared/model/video.model';

export interface IUserComment {
  id?: number;
  commentBody?: string | null;
  commentDate?: string | null;
  user?: IUser | null;
  video?: IVideo | null;
}

export const defaultValue: Readonly<IUserComment> = {};
