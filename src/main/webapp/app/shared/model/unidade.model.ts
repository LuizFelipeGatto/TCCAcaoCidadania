export interface IUnidade {
  id?: number;
  nome?: string;
  cep?: string;
  cidade?: string;
  logradouro?: string;
  codigoIbge?: string;
}

export const defaultValue: Readonly<IUnidade> = {};
