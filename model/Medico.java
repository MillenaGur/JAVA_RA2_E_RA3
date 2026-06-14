package model;

import java.util.ArrayList;
import java.util.List;

public class Medico extends Pessoa {

    private int codigo;
    private List<Paciente> pacientes;

    public Medico(String nome, int codigo) {

        super(nome);

        this.codigo = codigo;
        this.pacientes = new ArrayList<>();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void adicionarPaciente(Paciente paciente) {

        if (!pacientes.contains(paciente)) {
            pacientes.add(paciente);
        }
    }

    @Override
    public String exibirDados() {

        return "Médico: " + getNome()
                + " | Código: " + codigo;
    }
}