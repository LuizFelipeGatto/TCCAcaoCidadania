import dayjs from 'dayjs';
import { ICestaDescricao } from 'app/shared/model/cesta-descricao.model';
import { IFamilia } from 'app/shared/model/familia.model';

export interface IDoacao {
  id?: number;
  data?: string;
  cesta?: ICestaDescricao | null;
  familia?: IFamilia | null;
}

export const defaultValue: Readonly<IDoacao> = {};
