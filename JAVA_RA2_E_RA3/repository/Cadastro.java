package repository;

import exceptions.ConsultaInvalidaException;
import exceptions.PacienteNaoEncontradoException;
import model.Consulta;
import model.Medico;
import model.Paciente;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Cadastro implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public void carregarDadosCsv(
            String caminhoMedicos,
            String caminhoPacientes,
            String caminhoConsultas) throws IOException {

        carregarMedicosCsv(caminhoMedicos);
        carregarPacientesCsv(caminhoPacientes);
        carregarConsultasCsv(caminhoConsultas);
    }

    public void carregarMedicosCsv(String caminhoArquivo) throws IOException {

        for (String linha : Files.readAllLines(Paths.get(caminhoArquivo))) {

            if (linha.trim().isEmpty() || linha.startsWith("nome,")) {
                continue;
            }

            String[] dados = linha.split(",");
            String nome = dados[0].trim();
            int codigo = Integer.parseInt(dados[1].trim());

            adicionarMedico(new Medico(nome, codigo));
        }
    }

    public void carregarPacientesCsv(String caminhoArquivo) throws IOException {

        for (String linha : Files.readAllLines(Paths.get(caminhoArquivo))) {

            if (linha.trim().isEmpty() || linha.startsWith("nome,")) {
                continue;
            }

            String[] dados = linha.split(",");
            String nome = dados[0].trim();
            String cpf = dados[1].trim();

            adicionarPaciente(new Paciente(nome, cpf));
        }
    }

    public void carregarConsultasCsv(String caminhoArquivo) throws IOException {

        for (String linha : Files.readAllLines(Paths.get(caminhoArquivo))) {

            if (linha.trim().isEmpty() || linha.startsWith("data,")) {
                continue;
            }

            String[] dados = linha.split(",");
            LocalDate data = LocalDate.parse(dados[0].trim());
            LocalTime horario = LocalTime.parse(dados[1].trim());
            int codigoMedico = Integer.parseInt(dados[2].trim());
            String cpfPaciente = dados[3].trim();

            Medico medico = buscarMedicoPorCodigo(codigoMedico);
            Paciente paciente = buscarPacientePorCpf(cpfPaciente);

            if (medico == null || paciente == null) {
                throw new IllegalArgumentException("Consulta referencia medico ou paciente inexistente.");
            }

            Consulta consulta = new Consulta(data, horario, medico, paciente);

            adicionarConsulta(consulta);
            paciente.adicionarConsulta(consulta);
            medico.adicionarPaciente(paciente);
        }
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

    // Metodo 1 que repassa a excecao (throws) - nao usa try-catch internamente
    public Paciente buscarPacienteComValidacao(String cpf) throws PacienteNaoEncontradoException {

        Paciente pacienteEncontrado = buscarPacientePorCpf(cpf);

        if (pacienteEncontrado == null) {
            throw new PacienteNaoEncontradoException("Erro: Nenhum paciente encontrado com o CPF " + cpf);
        }

        return pacienteEncontrado;
    }

    // Metodo 2 que repassa a excecao (throws) - nao usa try-catch internamente
    public void validarEAdicionarConsulta(Consulta consulta) throws ConsultaInvalidaException {

        if (consulta.getMedico() == null) {
            throw new ConsultaInvalidaException("Erro: A consulta nao pode ser agendada sem um medico responsavel.");
        }

        if (consulta.getPaciente() == null) {
            throw new ConsultaInvalidaException("Erro: A consulta nao pode ser agendada sem um paciente.");
        }

        if (consulta.getData() == null || consulta.getHorario() == null) {
            throw new ConsultaInvalidaException("Erro: Data e horario da consulta sao obrigatorios.");
        }

        adicionarConsulta(consulta);
    }
}
