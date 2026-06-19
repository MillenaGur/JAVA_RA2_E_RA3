package testes;

import javax.swing.*;
import java.io.IOException;

public class InterfaceGrafica {

    public static void main(String[] args) throws IOException {
        //janela
        JFrame frame = new JFrame("Minha Janela Java");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);


        //Titulo
        JLabel titulo = new JLabel("Mostrar resultados:");
        titulo.setBounds(20, 10, 120, 30);

        //botao paciente
        JButton botao_p = new JButton("Dados paciente");
        botao_p.setBounds(50, 50, 140, 40);

        //botao medico
        JButton botao_m = new JButton("Dados médicos");
        botao_m.setBounds(200, 50, 140, 40);

        //botao consulta
        JButton botao_c = new JButton("Dados consultas");
        botao_c.setBounds(350, 50, 140, 40);


        //-------------------------------------------
        // ação dos botão

        //botao ação pasciente
        botao_p.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "botao paciente");
        });

        //botao ação medico
        botao_m.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "botao medico");
        });

        //botao ação consulta
        botao_c.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "botao consulta");
        });

        //-------------------------------------------
        //linhas pra divisão
        JLabel linha1 = new JLabel("-----------------------------------------------------------------");
        linha1.setBounds(20, 100, 300, 30);

        JLabel linha2 = new JLabel("-----------------------------------------------------------------");
        linha2.setBounds(20, 250, 300, 30);

        //-------------------------------------------
        //-------------------------------------------

        //titulo consultar
        JLabel nomec = new JLabel("Consultar Paciente:");
        nomec.setBounds(20, 150, 120, 30);

        //buscar pasciente
        JTextField buscar = new JTextField(20);
        buscar.setBounds(50, 200, 120, 30);

        //botao para buscar pasciente
        JButton botao_pes = new JButton("Consultar dados");
        botao_pes.setBounds(200, 200, 140, 40);

        //-------------------------------------------
        //-------------------------------------------

        //titulo
        JLabel nomem = new JLabel("Consultar Medico:");
        nomem.setBounds(20, 270, 120, 30);

        //consultar medico
        JTextField buscam = new JTextField(20);
        buscam.setBounds(50, 300, 120, 30);

        //botao consultar medico
        JButton botao_pesm = new JButton("Consultar dados");
        botao_pesm.setBounds(200, 300, 140, 40);

        //-------------------------------------------
        // ação dos botoes

        //ação botao consultar paciente
        botao_pes.addActionListener(e -> {

            String texto = buscar.getText();
            System.out.println(texto);

        });

        //ação do botão consultar medicos
        botao_pesm.addActionListener(e -> {

            String texto = buscam.getText();
            System.out.println(texto);

        });


        // Adicionando botão
        frame.add(botao_p);
        frame.add(botao_m);
        frame.add(botao_c);

        //botao da consulta
        frame.add(botao_pes);
        frame.add(botao_pesm);


        //inserir texto
        frame.add(buscar);
        frame.add(buscam);
        frame.setVisible(true);

        //titulos
        frame.add(titulo);
        frame.add(nomec);
        frame.add(nomem);

        //linhas
        frame.add(linha1);
        frame.add(linha2);


    }
}
