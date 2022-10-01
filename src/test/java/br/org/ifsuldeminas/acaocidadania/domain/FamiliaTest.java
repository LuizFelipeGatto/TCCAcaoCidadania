package br.org.ifsuldeminas.acaocidadania.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.org.ifsuldeminas.acaocidadania.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamiliaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Familia.class);
        Familia familia1 = new Familia();
        familia1.setId(1L);
        Familia familia2 = new Familia();
        familia2.setId(familia1.getId());
        assertThat(familia1).isEqualTo(familia2);
        familia2.setId(2L);
        assertThat(familia1).isNotEqualTo(familia2);
        familia1.setId(null);
        assertThat(familia1).isNotEqualTo(familia2);
    }
}
