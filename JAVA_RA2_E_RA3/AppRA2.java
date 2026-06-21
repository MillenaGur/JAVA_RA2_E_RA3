import repository.Cadastro;
import ui.InterfaceMedico;
import ui.InterfacePaciente;

import java.util.Scanner;

public class AppRA2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cadastro cadastro = new Cadastro();

        try {
            cadastro.carregarDadosCsv(
                    "data/medicos.csv",
                    "data/pacientes.csv",
                    "data/consultas.csv"
            );
            System.out.println("Dados carregados com sucesso.");
            System.out.println("  Medicos: " + cadastro.getMedicos().size());
            System.out.println("  Pacientes: " + cadastro.getPacientes().size());
            System.out.println("  Consultas: " + cadastro.getConsultas().size());
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados CSV: " + e.getMessage());
            return;
        }

        InterfaceMedico interfaceMedico = new InterfaceMedico(cadastro, scanner);
        InterfacePaciente interfacePaciente = new InterfacePaciente(cadastro, scanner);

        boolean executando = true;
        while (executando) {
            System.out.println("\n========== SISTEMA MEDICOS E PACIENTES ==========");
            System.out.println("1 - Interface do Medico");
            System.out.println("2 - Interface do Paciente");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");
            String opcao = scanner.nextLine().trim();

            try {
                switch (opcao) {
                    case "1" -> interfaceMedico.iniciar();
                    case "2" -> interfacePaciente.iniciar();
                    case "0" -> executando = false;
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        System.out.println("Encerrando sistema.");
        scanner.close();
    }
}
