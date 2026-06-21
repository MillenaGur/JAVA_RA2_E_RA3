import repository.Cadastro;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * RA3 - Programa P1
 * Le os dados dos arquivos CSV e salva o Cadastro completo
 * em formato binario (serializacao Java).
 */
public class P1Persistencia {

    private static final String ARQUIVO_BINARIO = "data/cadastro.bin";

    public static void main(String[] args) {
        Cadastro cadastro = new Cadastro();

        // Carrega CSV
        try {
            cadastro.carregarDadosCsv(
                    "data/medicos.csv",
                    "data/pacientes.csv",
                    "data/consultas.csv"
            );
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivos CSV: " + e.getMessage());
            return;
        }

        System.out.println("Dados lidos dos CSV:");
        System.out.println("  Medicos   : " + cadastro.getMedicos().size());
        System.out.println("  Pacientes : " + cadastro.getPacientes().size());
        System.out.println("  Consultas : " + cadastro.getConsultas().size());

        // Salva em binario
        salvarBinario(cadastro);
    }

    // Metodo que repassa a excecao (throws) - nao usa try-catch internamente
    public static void salvarCadastroEmArquivo(Cadastro cadastro, String caminho) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminho))) {
            oos.writeObject(cadastro);
        }
    }

    private static void salvarBinario(Cadastro cadastro) {
        try {
            salvarCadastroEmArquivo(cadastro, ARQUIVO_BINARIO);
            System.out.println("Cadastro salvo com sucesso em: " + ARQUIVO_BINARIO);
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo binario: " + e.getMessage());
        }
    }
}
