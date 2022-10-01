package br.org.ifsuldeminas.acaocidadania.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.org.ifsuldeminas.acaocidadania.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DoacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doacao.class);
        Doacao doacao1 = new Doacao();
        doacao1.setId(1L);
        Doacao doacao2 = new Doacao();
        doacao2.setId(doacao1.getId());
        assertThat(doacao1).isEqualTo(doacao2);
        doacao2.setId(2L);
        assertThat(doacao1).isNotEqualTo(doacao2);
        doacao1.setId(null);
        assertThat(doacao1).isNotEqualTo(doacao2);
    }
}
