import { IUnidade } from 'app/shared/model/unidade.model';
import { IFamilia } from 'app/shared/model/familia.model';
import { Beneficio } from 'app/shared/model/enumerations/beneficio.model';

export interface IPessoa {
  id?: number;
  nome?: string;
  cpf?: string;
  cidade?: string;
  logradouro?: string;
  codigoIbge?: string;
  celular?: string;
  email?: string | null;
  beneficio?: Beneficio | null;
  unidade?: IUnidade | null;
  familia?: IFamilia | null;
}

export const defaultValue: Readonly<IPessoa> = {};
