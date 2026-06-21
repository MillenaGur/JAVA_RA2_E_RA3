package ui;

import model.Consulta;
import model.Medico;
import model.Paciente;
import repository.Cadastro;
import util.SaidaPesquisa;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InterfacePaciente {

    private final Cadastro cadastro;
    private final Scanner scanner;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public InterfacePaciente(Cadastro cadastro, Scanner scanner) {
        this.cadastro = cadastro;
        this.scanner = scanner;
    }

    public void iniciar() throws IOException {
        boolean executando = true;
        while (executando) {
            System.out.println("\n========== INTERFACE DO PACIENTE ==========");
            System.out.println("1 - Listar todos os medicos de um paciente");
            System.out.println("2 - Consultas realizadas com um medico especifico");
            System.out.println("3 - Consultas futuras agendadas");
            System.out.println("0 - Voltar");
            System.out.print("Opcao: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> pesquisarMedicosDePaciente();
                case "2" -> pesquisarConsultasRealizadas();
                case "3" -> pesquisarConsultasFuturas();
                case "0" -> executando = false;
                default -> System.out.println("Opcao invalida.");
            }
        }
    }

    // Pesquisa 1: todos os medicos de um paciente (passado ou futuro)
    private void pesquisarMedicosDePaciente() throws IOException {
        Paciente paciente = selecionarPaciente();
        if (paciente == null) return;

        List<Medico> medicos = new ArrayList<>();
        for (Consulta c : paciente.getConsultas()) {
            if (!medicos.contains(c.getMedico())) {
                medicos.add(c.getMedico());
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Medicos do paciente ").append(paciente.getNome()).append(":\n");

        if (medicos.isEmpty()) {
            sb.append("  Nenhum medico encontrado.");
        } else {
            for (Medico m : medicos) {
                sb.append("  - ").append(m.exibirDados()).append("\n");
            }
        }

        SaidaPesquisa.escolherDestino(scanner, sb.toString());
    }

    // Pesquisa 2: consultas realizadas (passado) de um paciente com um medico
    private void pesquisarConsultasRealizadas() throws IOException {
        Paciente paciente = selecionarPaciente();
        if (paciente == null) return;

        System.out.println("Medicos disponiveis:");
        for (Medico m : cadastro.getMedicos()) {
            System.out.println("  [" + m.getCodigo() + "] " + m.getNome());
        }
        System.out.print("Informe o codigo do medico: ");
        int codigoMedico;
        try {
            codigoMedico = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Codigo invalido.");
            return;
        }

        Medico medico = cadastro.buscarMedicoPorCodigo(codigoMedico);
        if (medico == null) {
            System.out.println("Medico nao encontrado.");
            return;
        }

        LocalDate hoje = LocalDate.now();
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : paciente.getConsultas()) {
            if (c.getMedico().getCodigo() == codigoMedico && !c.getData().isAfter(hoje)) {
                resultado.add(c);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Consultas realizadas do paciente ").append(paciente.getNome())
                .append(" com o medico ").append(medico.getNome()).append(":\n");

        if (resultado.isEmpty()) {
            sb.append("  Nenhuma consulta realizada encontrada.");
        } else {
            for (Consulta c : resultado) {
                sb.append("  ").append(c.getData().format(FORMATO_DATA))
                        .append(" as ").append(c.getHorario()).append("\n");
            }
        }

        SaidaPesquisa.escolherDestino(scanner, sb.toString());
    }

    // Pesquisa 3: consultas futuras agendadas do paciente
    private void pesquisarConsultasFuturas() throws IOException {
        Paciente paciente = selecionarPaciente();
        if (paciente == null) return;

        LocalDate hoje = LocalDate.now();
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : paciente.getConsultas()) {
            if (c.getData().isAfter(hoje)) {
                resultado.add(c);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Consultas futuras do paciente ").append(paciente.getNome()).append(":\n");

        if (resultado.isEmpty()) {
            sb.append("  Nenhuma consulta futura agendada.");
        } else {
            for (Consulta c : resultado) {
                sb.append("  ").append(c.getData().format(FORMATO_DATA))
                        .append(" as ").append(c.getHorario())
                        .append(" | Medico: ").append(c.getMedico().getNome()).append("\n");
            }
        }

        SaidaPesquisa.escolherDestino(scanner, sb.toString());
    }

    private Paciente selecionarPaciente() {
        System.out.println("Pacientes disponiveis:");
        for (Paciente p : cadastro.getPacientes()) {
            System.out.println("  [" + p.getCpf() + "] " + p.getNome());
        }
        System.out.print("Informe o CPF do paciente: ");
        String cpf = scanner.nextLine().trim();
        Paciente paciente = cadastro.buscarPacientePorCpf(cpf);
        if (paciente == null) {
            System.out.println("Paciente nao encontrado.");
        }
        return paciente;
    }
}
