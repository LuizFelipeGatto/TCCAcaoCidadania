package br.org.ifsuldeminas.acaocidadania.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.org.ifsuldeminas.acaocidadania.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CestaDescricaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CestaDescricao.class);
        CestaDescricao cestaDescricao1 = new CestaDescricao();
        cestaDescricao1.setId(1L);
        CestaDescricao cestaDescricao2 = new CestaDescricao();
        cestaDescricao2.setId(cestaDescricao1.getId());
        assertThat(cestaDescricao1).isEqualTo(cestaDescricao2);
        cestaDescricao2.setId(2L);
        assertThat(cestaDescricao1).isNotEqualTo(cestaDescricao2);
        cestaDescricao1.setId(null);
        assertThat(cestaDescricao1).isNotEqualTo(cestaDescricao2);
    }
}
