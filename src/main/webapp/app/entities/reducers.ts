import familia from 'app/entities/familia/familia.reducer';
import cestaDescricao from 'app/entities/cesta-descricao/cesta-descricao.reducer';
import unidade from 'app/entities/unidade/unidade.reducer';
import doacao from 'app/entities/doacao/doacao.reducer';
import pessoa from 'app/entities/pessoa/pessoa.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  familia,
  cestaDescricao,
  unidade,
  doacao,
  pessoa,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
