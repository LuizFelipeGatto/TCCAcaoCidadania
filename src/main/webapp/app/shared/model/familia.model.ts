export interface IFamilia {
  id?: number;
  nome?: string;
  ativa?: boolean;
  renda?: number;
}

export const defaultValue: Readonly<IFamilia> = {
  ativa: false,
};
