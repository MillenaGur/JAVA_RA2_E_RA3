package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class SaidaPesquisa {

    public static void exibirNaTela(String resultado) {

        System.out.println(resultado);
    }

    public static void escreverEmArquivo(String resultado, String caminhoArquivo) throws IOException {

        Files.writeString(Path.of(caminhoArquivo), resultado);
    }

    public static void escolherDestino(Scanner scanner, String resultado) throws IOException {

        System.out.println("Escolha como deseja receber o resultado da pesquisa:");
        System.out.println("1 - Exibir na tela");
        System.out.println("2 - Escrever em arquivo texto");

        String opcao = scanner.nextLine();

        if (opcao.equals("2")) {
            System.out.print("Informe o nome do arquivo a ser gerado: ");
            String caminhoArquivo = scanner.nextLine();

            escreverEmArquivo(resultado, caminhoArquivo);
            return;
        }

        exibirNaTela(resultado);
    }
}
