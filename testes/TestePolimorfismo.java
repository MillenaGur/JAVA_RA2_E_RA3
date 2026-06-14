package testes;

import model.Medico;
import model.Paciente;
import model.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class TestePolimorfismo {

    public static void main(String[] args) {

        List<Pessoa> pessoas = new ArrayList<>();

        pessoas.add(
                new Medico(
                        "Carlos Silva",
                        1001
                )
        );

        pessoas.add(
                new Paciente(
                        "Maria Souza",
                        "12345678901"
                )
        );

        pessoas.add(
                new Medico(
                        "Ana Lima",
                        1002
                )
        );

        pessoas.add(
                new Paciente(
                        "Pedro Santos",
                        "98765432100"
                )
        );

        for (Pessoa pessoa : pessoas) {

            System.out.println(
                    pessoa.exibirDados()
            );
        }
    }
}