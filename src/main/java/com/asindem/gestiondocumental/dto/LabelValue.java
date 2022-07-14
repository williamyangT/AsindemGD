package com.asindem.gestiondocumental.dto;

import lombok.Data;

public @Data
class LabelValue {
    private String label;
    private Cliente value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Cliente getValue() {
        return value;
    }

    public void setValue(Cliente value) {
        this.value = value;
    }
}

