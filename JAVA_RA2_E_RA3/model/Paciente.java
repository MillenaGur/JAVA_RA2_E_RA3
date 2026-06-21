package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Pessoa {

    private static final long serialVersionUID = 1L;

    private String cpf;
    private List<Consulta> consultas;

    public Paciente(String nome, String cpf) {

        super(nome);

        this.cpf = cpf;
        this.consultas = new ArrayList<>();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void adicionarConsulta(Consulta consulta) {

        consultas.add(consulta);
    }

    @Override
    public String exibirDados() {

        return "Paciente: " + getNome()
                + " | CPF: " + cpf;
    }
}
