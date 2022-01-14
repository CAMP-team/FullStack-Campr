import { IVideo } from 'app/shared/model/video.model';

export interface IGenre {
  id?: number;
  apiId?: number | null;
  name?: string | null;
  videos?: IVideo[] | null;
}

export const defaultValue: Readonly<IGenre> = {};
