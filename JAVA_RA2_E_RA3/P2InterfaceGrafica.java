import model.Consulta;
import model.Medico;
import model.Paciente;
import repository.Cadastro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * RA3 - Programa P2
 * Restaura o Cadastro do arquivo binario e disponibiliza
 * interface grafica (Swing) para pesquisas, com opcao de
 * exportar resultados para CSV ou TXT.
 */
public class P2InterfaceGrafica {

    private static final String ARQUIVO_BINARIO = "data/cadastro.bin";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Cadastro cadastro;
    private JFrame frame;
    private JTextArea areaResultado;
    private JTabbedPane abas;

    // --- ENTRADA ---

    public static void main(String[] args) {
        SwingUtilities.invokeLater(P2InterfaceGrafica::new);
    }

    public P2InterfaceGrafica() {
        cadastro = restaurarCadastro();
        if (cadastro == null) {
            JOptionPane.showMessageDialog(null,
                    "Nao foi possivel carregar o arquivo binario.\nExecute P1Persistencia primeiro.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        construirInterface();
    }

    // Metodo que repassa a excecao (throws) - requisito RA2/RA3
    public static Cadastro carregarCadastroDeArquivo(String caminho) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminho))) {
            return (Cadastro) ois.readObject();
        }
    }

    private Cadastro restaurarCadastro() {
        try {
            Cadastro c = carregarCadastroDeArquivo(ARQUIVO_BINARIO);
            System.out.println("Cadastro restaurado: "
                    + c.getMedicos().size() + " medicos, "
                    + c.getPacientes().size() + " pacientes, "
                    + c.getConsultas().size() + " consultas.");
            return c;
        } catch (Exception e) {
            System.out.println("Erro ao restaurar: " + e.getMessage());
            return null;
        }
    }

    // --- INTERFACE GRAFICA ---

    private void construirInterface() {
        frame = new JFrame("Sistema Medicos e Pacientes - RA3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 620);
        frame.setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(8, 8));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Abas de pesquisa
        abas = new JTabbedPane();
        abas.addTab("Pacientes do Medico", criarAbaPacientesDeMedico());
        abas.addTab("Consultas por Periodo", criarAbaConsultasPeriodo());
        abas.addTab("Pacientes Inativos", criarAbaPacientesInativos());
        abas.addTab("Medicos do Paciente", criarAbaMedicosDePaciente());
        abas.addTab("Consultas Realizadas", criarAbaConsultasRealizadas());
        abas.addTab("Consultas Futuras", criarAbaConsultasFuturas());

        // Area de resultado
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultado da Pesquisa"));
        scroll.setPreferredSize(new Dimension(800, 200));

        // Botoes de exportacao
        JPanel painelExport = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExportTxt = new JButton("Exportar como TXT");
        JButton btnExportCsv = new JButton("Exportar como CSV");
        btnExportTxt.addActionListener(e -> exportarResultado("txt"));
        btnExportCsv.addActionListener(e -> exportarResultado("csv"));
        painelExport.add(btnExportTxt);
        painelExport.add(btnExportCsv);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(scroll, BorderLayout.CENTER);
        painelInferior.add(painelExport, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, abas, painelInferior);
        split.setResizeWeight(0.55);
        painelPrincipal.add(split, BorderLayout.CENTER);

        frame.add(painelPrincipal);
        frame.setVisible(true);
    }

    // --- ABA 1: Pacientes de um medico ---
    private JPanel criarAbaPacientesDeMedico() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JComboBox<Medico> cbMedico = new JComboBox<>(cadastro.getMedicos().toArray(new Medico[0]));
        cbMedico.setRenderer((list, value, index, sel, focus) ->
                new JLabel(value == null ? "" : value.getNome() + " [" + value.getCodigo() + "]"));

        JButton btn = new JButton("Pesquisar");
        btn.addActionListener(e -> {
            Medico med = (Medico) cbMedico.getSelectedItem();
            if (med == null) return;
            StringBuilder sb = new StringBuilder("Pacientes do medico " + med.getNome() + ":\n");
            if (med.getPacientes().isEmpty()) {
                sb.append("  Nenhum paciente.");
            } else {
                for (Paciente pac : med.getPacientes()) {
                    sb.append("  - ").append(pac.exibirDados()).append("\n");
                }
            }
            areaResultado.setText(sb.toString());
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Medico:"));
        topo.add(cbMedico);
        topo.add(btn);
        p.add(topo, BorderLayout.NORTH);
        return p;
    }

    // --- ABA 2: Consultas de medico em periodo ---
    private JPanel criarAbaConsultasPeriodo() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JComboBox<Medico> cbMedico = new JComboBox<>(cadastro.getMedicos().toArray(new Medico[0]));
        cbMedico.setRenderer((list, value, index, sel, focus) ->
                new JLabel(value == null ? "" : value.getNome() + " [" + value.getCodigo() + "]"));

        JTextField txtInicio = new JTextField("01/01/2026", 10);
        JTextField txtFim = new JTextField("31/12/2026", 10);

        JButton btn = new JButton("Pesquisar");
        btn.addActionListener(e -> {
            Medico med = (Medico) cbMedico.getSelectedItem();
            if (med == null) return;
            LocalDate inicio, fim;
            try {
                inicio = LocalDate.parse(txtInicio.getText().trim(), FMT);
                fim = LocalDate.parse(txtFim.getText().trim(), FMT);
            } catch (DateTimeParseException ex) {
                mostrarErro("Data invalida. Use dd/MM/yyyy.");
                return;
            }
            List<Consulta> lista = new ArrayList<>();
            for (Consulta c : cadastro.getConsultas()) {
                if (c.getMedico().getCodigo() == med.getCodigo()
                        && !c.getData().isBefore(inicio) && !c.getData().isAfter(fim)) {
                    lista.add(c);
                }
            }
            lista.sort(Comparator.comparing(Consulta::getData).thenComparing(Consulta::getHorario));
            StringBuilder sb = new StringBuilder("Consultas de " + med.getNome()
                    + " de " + inicio.format(FMT) + " a " + fim.format(FMT) + ":\n");
            if (lista.isEmpty()) {
                sb.append("  Nenhuma consulta no periodo.");
            } else {
                for (Consulta c : lista) {
                    sb.append("  ").append(c.getData().format(FMT)).append(" ")
                            .append(c.getHorario()).append(" | ").append(c.getPaciente().getNome()).append("\n");
                }
            }
            areaResultado.setText(sb.toString());
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Medico:"));
        topo.add(cbMedico);
        topo.add(new JLabel("De:"));
        topo.add(txtInicio);
        topo.add(new JLabel("Ate:"));
        topo.add(txtFim);
        topo.add(btn);
        p.add(topo, BorderLayout.NORTH);
        return p;
    }

    // --- ABA 3: Pacientes inativos (sem consulta ha N meses) ---
    private JPanel criarAbaPacientesInativos() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JComboBox<Medico> cbMedico = new JComboBox<>(cadastro.getMedicos().toArray(new Medico[0]));
        cbMedico.setRenderer((list, value, index, sel, focus) ->
                new JLabel(value == null ? "" : value.getNome() + " [" + value.getCodigo() + "]"));

        JSpinner spinMeses = new JSpinner(new SpinnerNumberModel(6, 1, 120, 1));

        JButton btn = new JButton("Pesquisar");
        btn.addActionListener(e -> {
            Medico med = (Medico) cbMedico.getSelectedItem();
            if (med == null) return;
            int meses = (int) spinMeses.getValue();
            LocalDate limite = LocalDate.now().minusMonths(meses);
            List<Paciente> resultado = new ArrayList<>();
            for (Paciente pac : med.getPacientes()) {
                LocalDate ultima = null;
                for (Consulta c : pac.getConsultas()) {
                    if (c.getMedico().getCodigo() == med.getCodigo() && !c.getData().isAfter(LocalDate.now())) {
                        if (ultima == null || c.getData().isAfter(ultima)) ultima = c.getData();
                    }
                }
                if (ultima == null || ultima.isBefore(limite)) resultado.add(pac);
            }
            StringBuilder sb = new StringBuilder("Pacientes de " + med.getNome()
                    + " sem consulta ha mais de " + meses + " meses:\n");
            if (resultado.isEmpty()) {
                sb.append("  Nenhum paciente nessa situacao.");
            } else {
                for (Paciente pac : resultado) sb.append("  - ").append(pac.exibirDados()).append("\n");
            }
            areaResultado.setText(sb.toString());
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Medico:"));
        topo.add(cbMedico);
        topo.add(new JLabel("Meses:"));
        topo.add(spinMeses);
        topo.add(btn);
        p.add(topo, BorderLayout.NORTH);
        return p;
    }

    // --- ABA 4: Medicos de um paciente ---
    private JPanel criarAbaMedicosDePaciente() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JComboBox<Paciente> cbPaciente = new JComboBox<>(cadastro.getPacientes().toArray(new Paciente[0]));
        cbPaciente.setRenderer((list, value, index, sel, focus) ->
                new JLabel(value == null ? "" : value.getNome() + " [" + value.getCpf() + "]"));

        JButton btn = new JButton("Pesquisar");
        btn.addActionListener(e -> {
            Paciente pac = (Paciente) cbPaciente.getSelectedItem();
            if (pac == null) return;
            List<Medico> medicos = new ArrayList<>();
            for (Consulta c : pac.getConsultas()) {
                if (!medicos.contains(c.getMedico())) medicos.add(c.getMedico());
            }
            StringBuilder sb = new StringBuilder("Medicos do paciente " + pac.getNome() + ":\n");
            if (medicos.isEmpty()) {
                sb.append("  Nenhum medico encontrado.");
            } else {
                for (Medico m : medicos) sb.append("  - ").append(m.exibirDados()).append("\n");
            }
            areaResultado.setText(sb.toString());
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Paciente:"));
        topo.add(cbPaciente);
        topo.add(btn);
        p.add(topo, BorderLayout.NORTH);
        return p;
    }

    // --- ABA 5: Consultas realizadas de paciente com medico ---
    private JPanel criarAbaConsultasRealizadas() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JComboBox<Paciente> cbPaciente = new JComboBox<>(cadastro.getPacientes().toArray(new Paciente[0]));
        cbPaciente.setRenderer((list, value, index, sel, focus) ->
                new JLabel(value == null ? "" : value.getNome() + " [" + value.getCpf() + "]"));

        JComboBox<Medico> cbMedico = new JComboBox<>(cadastro.getMedicos().toArray(new Medico[0]));
        cbMedico.setRenderer((list, value, index, sel, focus) ->
                new JLabel(value == null ? "" : value.getNome() + " [" + value.getCodigo() + "]"));

        JButton btn = new JButton("Pesquisar");
        btn.addActionListener(e -> {
            Paciente pac = (Paciente) cbPaciente.getSelectedItem();
            Medico med = (Medico) cbMedico.getSelectedItem();
            if (pac == null || med == null) return;
            LocalDate hoje = LocalDate.now();
            List<Consulta> lista = new ArrayList<>();
            for (Consulta c : pac.getConsultas()) {
                if (c.getMedico().getCodigo() == med.getCodigo() && !c.getData().isAfter(hoje)) {
                    lista.add(c);
                }
            }
            StringBuilder sb = new StringBuilder("Consultas realizadas de " + pac.getNome()
                    + " com " + med.getNome() + ":\n");
            if (lista.isEmpty()) {
                sb.append("  Nenhuma consulta realizada.");
            } else {
                for (Consulta c : lista) {
                    sb.append("  ").append(c.getData().format(FMT)).append(" as ").append(c.getHorario()).append("\n");
                }
            }
            areaResultado.setText(sb.toString());
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Paciente:"));
        topo.add(cbPaciente);
        topo.add(new JLabel("Medico:"));
        topo.add(cbMedico);
        topo.add(btn);
        p.add(topo, BorderLayout.NORTH);
        return p;
    }

    // --- ABA 6: Consultas futuras de um paciente ---
    private JPanel criarAbaConsultasFuturas() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        JComboBox<Paciente> cbPaciente = new JComboBox<>(cadastro.getPacientes().toArray(new Paciente[0]));
        cbPaciente.setRenderer((list, value, index, sel, focus) ->
                new JLabel(value == null ? "" : value.getNome() + " [" + value.getCpf() + "]"));

        JButton btn = new JButton("Pesquisar");
        btn.addActionListener(e -> {
            Paciente pac = (Paciente) cbPaciente.getSelectedItem();
            if (pac == null) return;
            LocalDate hoje = LocalDate.now();
            List<Consulta> lista = new ArrayList<>();
            for (Consulta c : pac.getConsultas()) {
                if (c.getData().isAfter(hoje)) lista.add(c);
            }
            StringBuilder sb = new StringBuilder("Consultas futuras de " + pac.getNome() + ":\n");
            if (lista.isEmpty()) {
                sb.append("  Nenhuma consulta futura agendada.");
            } else {
                for (Consulta c : lista) {
                    sb.append("  ").append(c.getData().format(FMT)).append(" as ")
                            .append(c.getHorario()).append(" | Medico: ").append(c.getMedico().getNome()).append("\n");
                }
            }
            areaResultado.setText(sb.toString());
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Paciente:"));
        topo.add(cbPaciente);
        topo.add(btn);
        p.add(topo, BorderLayout.NORTH);
        return p;
    }

    // --- EXPORTACAO DE RESULTADOS (requisito RA3-4) ---
    private void exportarResultado(String formato) {
        String texto = areaResultado.getText();
        if (texto == null || texto.isBlank()) {
            mostrarErro("Nenhum resultado para exportar. Faca uma pesquisa primeiro.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("resultado." + formato));
        int opcao = chooser.showSaveDialog(frame);
        if (opcao != JFileChooser.APPROVE_OPTION) return;

        File arquivo = chooser.getSelectedFile();
        try {
            if ("csv".equals(formato)) {
                escreverCsv(texto, arquivo);
            } else {
                Files.writeString(arquivo.toPath(), texto);
            }
            JOptionPane.showMessageDialog(frame, "Resultado salvo em:\n" + arquivo.getAbsolutePath());
        } catch (IOException e) {
            mostrarErro("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    // Metodo que repassa a excecao (throws) - requisito RA2
    private void escreverCsv(String texto, File arquivo) throws IOException {
        // Converte linhas de resultado para formato CSV simples
        StringBuilder csv = new StringBuilder("linha,conteudo\n");
        String[] linhas = texto.split("\n");
        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i].replace("\"", "\"\""); // escapa aspas
            csv.append(i + 1).append(",\"").append(linha).append("\"\n");
        }
        Files.writeString(arquivo.toPath(), csv.toString());
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
