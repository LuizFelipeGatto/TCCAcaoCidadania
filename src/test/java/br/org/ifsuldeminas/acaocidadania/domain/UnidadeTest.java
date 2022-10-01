package br.org.ifsuldeminas.acaocidadania.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.org.ifsuldeminas.acaocidadania.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnidadeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Unidade.class);
        Unidade unidade1 = new Unidade();
        unidade1.setId(1L);
        Unidade unidade2 = new Unidade();
        unidade2.setId(unidade1.getId());
        assertThat(unidade1).isEqualTo(unidade2);
        unidade2.setId(2L);
        assertThat(unidade1).isNotEqualTo(unidade2);
        unidade1.setId(null);
        assertThat(unidade1).isNotEqualTo(unidade2);
    }
}
