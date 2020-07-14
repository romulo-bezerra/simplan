package br.com.romulo.simplan.domain;

public enum Status {

    ATIVO("ATIVO"),
    CANCELADO("CANCELADO"),
    QUITADO("QUITADO"),
    INATIVO("INATIVO");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
