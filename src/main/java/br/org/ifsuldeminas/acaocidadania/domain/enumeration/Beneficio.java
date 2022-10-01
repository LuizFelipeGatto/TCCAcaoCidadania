package br.org.ifsuldeminas.acaocidadania.domain.enumeration;

/**
 * The Beneficio enumeration.
 */
public enum Beneficio {
    BOLSA_FAMILIA("Bolsa Familia"),
    BPC("BPC"),
    APOSENTADORIA("Aposentadoria"),
    SEM_RENDA("Sem renda no momento");

    private final String value;

    Beneficio(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
