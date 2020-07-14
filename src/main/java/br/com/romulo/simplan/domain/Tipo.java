package br.com.romulo.simplan.domain;

public enum Tipo {

    ATIVO("ATIVO"),
    CANCELADO("CANCELADO"),
    QUITADO("QUITADO"),
    INATIVO("INATIVO");

    private final String label;

    Tipo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
