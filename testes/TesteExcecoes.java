package testes;

import exceptions.ConsultaInvalidaException;
import exceptions.PacienteNaoEncontradoException;
import model.Consulta;
import model.Medico;
import model.Paciente;
import repository.Cadastro;

import java.time.LocalDate;
import java.time.LocalTime;

public class TesteExcecoes {

    public static void main(String[] args) {
        
        Cadastro cadastro = new Cadastro();
        
        System.out.println("--- TESTE 1: Buscando paciente inexistente ---");
        try {
            // Tentando buscar um CPF que não foi cadastrado
            cadastro.buscarPacienteComValidacao("00000000000");
            System.out.println("Paciente encontrado com sucesso!"); // Essa linha não vai rodar
            
        } catch (PacienteNaoEncontradoException e) {
            // Aqui capturamos e mostramos a mensagem do throw
            System.out.println("Exceção capturada: " + e.getMessage());
        }

        System.out.println("\n--- TESTE 2: Adicionando consulta inválida ---");
        try {
            // Criando uma consulta sem médico e sem paciente (nulos) para forçar o erro
            Consulta consultaRuim = new Consulta(LocalDate.now(), LocalTime.now(), null, null);
            
            cadastro.validarEAdicionarConsulta(consultaRuim);
            System.out.println("Consulta adicionada com sucesso!"); // Essa linha não vai rodar
            
        } catch (ConsultaInvalidaException e) {
            // Aqui capturamos e mostramos a mensagem do throw
            System.out.println("Exceção capturada: " + e.getMessage());
        }
        
        System.out.println("\n--- TESTE 3: Adicionando consulta válida ---");
        try {
            Medico medico = new Medico("Dr. João", 123);
            Paciente paciente = new Paciente("Maria", "11122233344");
            Consulta consultaBoa = new Consulta(LocalDate.now(), LocalTime.now(), medico, paciente);
            
            cadastro.validarEAdicionarConsulta(consultaBoa);
            System.out.println("Sucesso: A consulta foi adicionada sem disparar exceções!");
            
        } catch (ConsultaInvalidaException e) {
            System.out.println("Exceção capturada: " + e.getMessage());
        }
    }
}