package testes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import util.SaidaPesquisa;

public class TesteSaidaPesquisa {

    public static void main(String[] args) throws Exception {

        String resultado = "Paciente: Maria Souza\nPaciente: Pedro Santos";
        String caminhoArquivo = "resultado-pesquisa-teste.txt";

        SaidaPesquisa.escreverEmArquivo(resultado, caminhoArquivo);

        String conteudo = Files.readString(Path.of(caminhoArquivo));

        if (!conteudo.equals(resultado)) {
            throw new IllegalStateException("O conteúdo gravado no arquivo está incorreto.");
        }

        Files.deleteIfExists(Path.of(caminhoArquivo));

        Scanner scanner = new Scanner("2\n" + caminhoArquivo + "\n");
        SaidaPesquisa.escolherDestino(scanner, resultado);

        conteudo = Files.readString(Path.of(caminhoArquivo));

        if (!conteudo.equals(resultado)) {
            throw new IllegalStateException("O conteúdo gravado pela escolha do usuário está incorreto.");
        }

        Files.deleteIfExists(Path.of(caminhoArquivo));

        System.out.println("Resultado gravado em arquivo com sucesso.");
    }
}
