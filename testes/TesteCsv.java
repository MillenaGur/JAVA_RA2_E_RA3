package testes;

import repository.Cadastro;

public class TesteCsv {

    public static void main(String[] args) throws Exception {

        Cadastro cadastro = new Cadastro();

        cadastro.carregarDadosCsv(
                "data/medicos.csv",
                "data/pacientes.csv",
                "data/consultas.csv"
        );

        System.out.println("Médicos carregados: " + cadastro.getMedicos().size());
        System.out.println("Pacientes carregados: " + cadastro.getPacientes().size());
        System.out.println("Consultas carregadas: " + cadastro.getConsultas().size());

        for (var consulta : cadastro.getConsultas()) {
            System.out.println(consulta);
        }
    }
}
