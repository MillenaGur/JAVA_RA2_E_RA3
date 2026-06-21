package model;

import interfaces.Exibivel;
import java.io.Serializable;

public abstract class Pessoa
        implements Exibivel, Serializable {

    private String nome;

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public abstract String exibirDados();
}