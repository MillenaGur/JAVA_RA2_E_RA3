package testes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import model.Medico;

public class Testepegar {

    // Mudamos o retorno para ArrayList de Medico (ou mantemos Object se preferir)
    public static ArrayList<Object> restaurarObjetos(String caminhoArquivo) {
        ArrayList<Object> medicos = new ArrayList<>();

        // Trocamos o FileInputStream por BufferedReader para ler arquivos de texto (CSV)
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            // Opcional: Se o seu CSV tiver um cabeçalho (ex: Nome;CRM;Especialidade),
            // descomente a linha abaixo para pular a primeira linha:
            // br.readLine();

            // Lê o arquivo linha por linha até o final
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue; // Ignora linhas vazias

                // Divide a linha usando o separador do CSV (ajuste para "," ou ";" dependendo do seu arquivo)
                String[] dados = linha.split(";");

                // --- EXEMPLO DE CRIAÇÃO DO OBJETO ---
                // Supondo que a estrutura do seu CSV seja: Nome;CRM
                String nome = dados[0];
                int codigo = Integer.parseInt(dados[1].trim());

                // Cria o objeto médico (Ajuste para o construtor da sua classe Medico)
                Medico medico = new Medico(nome, codigo);

                // Adiciona na lista
                medicos.add(medico);
            }

        } catch (IOException e) {
            System.err.println("Erro ao restaurar dados do arquivo CSV: " + e.getMessage());
        }

        return medicos;
    }
}