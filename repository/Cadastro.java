package repository;

import exceptions.ConsultaInvalidaException;
import exceptions.PacienteNaoEncontradoException;
import model.Consulta;
import model.Medico;
import model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class Cadastro {

    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private List<Consulta> consultas;

    public Cadastro() {

        medicos = new ArrayList<>();
        pacientes = new ArrayList<>();
        consultas = new ArrayList<>();
    }

    public void adicionarMedico(Medico medico) {
        medicos.add(medico);
    }

    public void adicionarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    public void adicionarConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public Medico buscarMedicoPorCodigo(int codigo) {

        for (Medico medico : medicos) {

            if (medico.getCodigo() == codigo) {
                return medico;
            }
        }

        return null;
    }

    public Paciente buscarPacientePorCpf(String cpf) {

        for (Paciente paciente : pacientes) {

            if (paciente.getCpf().equals(cpf)) {
                return paciente;
            }
        }

        return null;
    }

    // Método 1 que repassa a exceção (throws) e não usa try-catch internamente
    public Paciente buscarPacienteComValidacao(String cpf) throws PacienteNaoEncontradoException {
        
        Paciente pacienteEncontrado = buscarPacientePorCpf(cpf);
        
        // Uso do comando throw para instanciar a exceção
        if (pacienteEncontrado == null) {
            throw new PacienteNaoEncontradoException("Erro: Nenhum paciente encontrado com o CPF " + cpf);
        }
        
        return pacienteEncontrado;
    }

    // Método 2 que repassa a exceção (throws) e não usa try-catch internamente
    public void validarEAdicionarConsulta(Consulta consulta) throws ConsultaInvalidaException {
        
        // Validação de regras de negócio com o comando throw
        if (consulta.getMedico() == null) {
            throw new ConsultaInvalidaException("Erro: A consulta não pode ser agendada sem um médico responsável.");
        }
        
        if (consulta.getPaciente() == null) {
            throw new ConsultaInvalidaException("Erro: A consulta não pode ser agendada sem um paciente.");
        }
        
        if (consulta.getData() == null || consulta.getHorario() == null) {
            throw new ConsultaInvalidaException("Erro: Data e horário da consulta são obrigatórios.");
        }

        // Se passou por todas as validações sem lançar exceção, adiciona a consulta
        adicionarConsulta(consulta);
    }
}
