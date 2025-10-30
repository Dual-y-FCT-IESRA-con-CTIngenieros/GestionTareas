package com.ejemplo.appweb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Config")
public class Config {

    @Id
    private String clave;

    private String valor;

    public Config() {}

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    @Override
    public String toString() {
        return "Config{" + "clave='" + clave + '\'' + ", valor='" + valor + '\'' + '}';
    }
}
