import dayjs from 'dayjs';
import { IWord } from 'app/shared/model/word.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IFavorites {
  id?: number;
  timestamp?: dayjs.Dayjs | null;
  proficiencyLvl?: number | null;
  words?: IWord[] | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IFavorites> = {};
