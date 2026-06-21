package ui;

import model.Consulta;
import model.Medico;
import model.Paciente;
import repository.Cadastro;
import util.SaidaPesquisa;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class InterfaceMedico {

    private final Cadastro cadastro;
    private final Scanner scanner;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public InterfaceMedico(Cadastro cadastro, Scanner scanner) {
        this.cadastro = cadastro;
        this.scanner = scanner;
    }

    public void iniciar() throws IOException {
        boolean executando = true;
        while (executando) {
            System.out.println("\n========== INTERFACE DO MEDICO ==========");
            System.out.println("1 - Listar todos os pacientes de um medico");
            System.out.println("2 - Consultas agendadas em um periodo");
            System.out.println("3 - Pacientes sem consulta ha mais de N meses");
            System.out.println("0 - Voltar");
            System.out.print("Opcao: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> pesquisarPacientesDeMedico();
                case "2" -> pesquisarConsultasEmPeriodo();
                case "3" -> pesquisarPacientesSemConsulta();
                case "0" -> executando = false;
                default -> System.out.println("Opcao invalida.");
            }
        }
    }

    // Pesquisa 1: todos os pacientes de um medico
    private void pesquisarPacientesDeMedico() throws IOException {
        Medico medico = selecionarMedico();
        if (medico == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Pacientes do medico ").append(medico.getNome()).append(":\n");

        List<Paciente> pacientes = medico.getPacientes();
        if (pacientes.isEmpty()) {
            sb.append("  Nenhum paciente encontrado.");
        } else {
            for (Paciente p : pacientes) {
                sb.append("  - ").append(p.exibirDados()).append("\n");
            }
        }

        SaidaPesquisa.escolherDestino(scanner, sb.toString());
    }

    // Pesquisa 2: consultas de um medico em um periodo, ordem crescente de horario
    private void pesquisarConsultasEmPeriodo() throws IOException {
        Medico medico = selecionarMedico();
        if (medico == null) return;

        LocalDate dataInicial = lerData("Data inicial (dd/MM/yyyy): ");
        LocalDate dataFinal = lerData("Data final (dd/MM/yyyy): ");

        if (dataInicial == null || dataFinal == null) return;

        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : cadastro.getConsultas()) {
            if (c.getMedico().getCodigo() == medico.getCodigo()) {
                LocalDate data = c.getData();
                if (!data.isBefore(dataInicial) && !data.isAfter(dataFinal)) {
                    resultado.add(c);
                }
            }
        }

        resultado.sort(Comparator.comparing(Consulta::getData)
                .thenComparing(Consulta::getHorario));

        StringBuilder sb = new StringBuilder();
        sb.append("Consultas do medico ").append(medico.getNome())
                .append(" de ").append(dataInicial.format(FORMATO_DATA))
                .append(" a ").append(dataFinal.format(FORMATO_DATA)).append(":\n");

        if (resultado.isEmpty()) {
            sb.append("  Nenhuma consulta encontrada no periodo.");
        } else {
            for (Consulta c : resultado) {
                sb.append("  ").append(c.getData().format(FORMATO_DATA))
                        .append(" ").append(c.getHorario())
                        .append(" | Paciente: ").append(c.getPaciente().getNome()).append("\n");
            }
        }

        SaidaPesquisa.escolherDestino(scanner, sb.toString());
    }

    // Pesquisa 3: pacientes que nao consultam o medico ha mais de N meses
    private void pesquisarPacientesSemConsulta() throws IOException {
        Medico medico = selecionarMedico();
        if (medico == null) return;

        System.out.print("Numero de meses: ");
        int meses;
        try {
            meses = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Numero de meses invalido.");
            return;
        }

        LocalDate limite = LocalDate.now().minusMonths(meses);
        List<Paciente> resultado = new ArrayList<>();

        for (Paciente paciente : medico.getPacientes()) {
            // Busca a consulta mais recente desse paciente com esse medico no passado
            LocalDate ultimaConsulta = null;
            for (Consulta c : paciente.getConsultas()) {
                if (c.getMedico().getCodigo() == medico.getCodigo()
                        && !c.getData().isAfter(LocalDate.now())) {
                    if (ultimaConsulta == null || c.getData().isAfter(ultimaConsulta)) {
                        ultimaConsulta = c.getData();
                    }
                }
            }
            if (ultimaConsulta == null || ultimaConsulta.isBefore(limite)) {
                resultado.add(paciente);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Pacientes do medico ").append(medico.getNome())
                .append(" sem consulta ha mais de ").append(meses).append(" meses:\n");

        if (resultado.isEmpty()) {
            sb.append("  Nenhum paciente nessa situacao.");
        } else {
            for (Paciente p : resultado) {
                sb.append("  - ").append(p.exibirDados()).append("\n");
            }
        }

        SaidaPesquisa.escolherDestino(scanner, sb.toString());
    }

    private Medico selecionarMedico() {
        System.out.println("Medicos disponiveis:");
        for (Medico m : cadastro.getMedicos()) {
            System.out.println("  [" + m.getCodigo() + "] " + m.getNome());
        }
        System.out.print("Informe o codigo do medico: ");
        try {
            int codigo = Integer.parseInt(scanner.nextLine().trim());
            Medico medico = cadastro.buscarMedicoPorCodigo(codigo);
            if (medico == null) {
                System.out.println("Medico nao encontrado.");
            }
            return medico;
        } catch (NumberFormatException e) {
            System.out.println("Codigo invalido.");
            return null;
        }
    }

    private LocalDate lerData(String prompt) {
        System.out.print(prompt);
        try {
            return LocalDate.parse(scanner.nextLine().trim(), FORMATO_DATA);
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida. Use o formato dd/MM/yyyy.");
            return null;
        }
    }
}
