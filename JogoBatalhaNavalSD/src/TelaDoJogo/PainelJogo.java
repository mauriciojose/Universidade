/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelaDoJogo;

import ReprodutorMP3.MP3;
import TelaGanhou.TelaGanhou;
import TelaPerdeu.TelaPerdeu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Mauricio José
 */
public class PainelJogo extends javax.swing.JPanel implements Runnable {

    private LabelJogo lj;
    private MP3 mp3;
    private TrataEvento trataEvento;

    private JLabel mar;
    private JLabel pontos;
    private JLabel vez;

    private HashMap<JLabel, Integer> map;
    private ArrayList<JLabel> labelPainel;
    private ArrayList<JLabel> labelMeuPainel;

    private ArrayList<Integer> barcos;
    private ArrayList<Integer> cliques;

    private ArrayList<Integer> barco5;
    private ArrayList<Integer> barco4;
    private ArrayList<Integer> barco3;
    private ArrayList<Integer> barco2;
    private ArrayList<Integer> barco1;

    private int barcoCinco = 1;
    private int barcoQuatro = 1;
    private int barcoTres = 1;
    private int barcoDois = 1;

    private int movX;
    private int movY;
    private int xPainel;
    private int yPainel;

    private int ponto;

    private int xMira;
    private int yMira;

    private int[] geraBarcos;

    BufferedImage imageFundo;
    ImageIcon Imagemar;
    ImageIcon Imagemira;
    ImageIcon imageMissil;
    ImageIcon imageBarco;
    ImageIcon ImageExplosao;
    ImageIcon imageExplosaoAgua;
    Image newimg;

    private Socket connection;
    private Scanner input;
    private Formatter output;
    private boolean minhaVez;

    private int xImage, yImage;

    public PainelJogo(Socket connection, boolean vez) {
        this.connection = connection;
        this.minhaVez = vez;

        this.setLayout(null);
        this.setVisible(true);

        inicia();
        desenhaPontosAndVez();
        desenhaCenario(true, xPainel, yPainel);
        xPainel = 450;
        yPainel = 200;
        movX = 450;
        movY = 200;
        desenhaCenario(false, xPainel, yPainel);
        startClient();
    }

    public PainelJogo() {
        this.setLayout(null);
        this.setVisible(true);

        inicia();
        desenhaPontosAndVez();
        desenhaCenario(true, xPainel, yPainel);
        xPainel = 450;
        yPainel = 200;
        movX = 450;
        movY = 200;
        desenhaCenario(false, xPainel, yPainel);
    }

    private void inicia() {

        lj = new LabelJogo();
        trataEvento = new TrataEvento();

        mar = new JLabel();
        pontos = new JLabel();
        vez = new JLabel();

        map = new HashMap<>();
        labelPainel = new ArrayList();
        labelMeuPainel = new ArrayList();

        barcos = new ArrayList();
        barco5 = new ArrayList();
        barco4 = new ArrayList();
        barco3 = new ArrayList();
        barco2 = new ArrayList();
        barco1 = new ArrayList();

        cliques = new ArrayList();

        movX = 20;
        movY = 200;
        xPainel = movX;
        yPainel = movY;

        ponto = 0;

        //imageFundo = ImageIO.read(this.getClass().getResource("FUNDO.jpg"));
        Imagemar = new ImageIcon(this.getClass().getResource("mar.jpg"));
        newimg = Imagemar.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        Imagemar = new ImageIcon(newimg);

        Imagemira = new ImageIcon(this.getClass().getResource("mira.png"));
        newimg = Imagemira.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        Imagemira = new ImageIcon(newimg);

        imageMissil = new ImageIcon(this.getClass().getResource("missil.jpg"));
        newimg = imageMissil.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        imageMissil = new ImageIcon(newimg);

        ImageExplosao = new ImageIcon(this.getClass().getResource("explosaoPOSCLIQUE.jpg"));
        newimg = ImageExplosao.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageExplosao = new ImageIcon(newimg);

        imageExplosaoAgua = new ImageIcon(this.getClass().getResource("aguaPOSCLIQUE.jpg"));
        newimg = imageExplosaoAgua.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        imageExplosaoAgua = new ImageIcon(newimg);

    }

    @Override
    public void paintComponent(Graphics g) {
        /*
         * A imagem vai ser desenhada em x=0, y=0 e usando o tamanho real da imagem
         * é fundamental que seja chamado o método drawImage antes de se chamar o
         * método paintComponent da superclasse.
         */
        super.paintComponents(g);
        try {
            imageFundo = ImageIO.read(this.getClass().getResource("FUNDO.jpg"));
        } catch (IOException ex) {
            System.out.println("ERRO AO CARREGAR IMAGEM");
        }
        g.drawImage(imageFundo, 0, 0, 860, 750, this);

        /*
         * Ao desenharmos primeiro a imagem garantimos que qualquer componente
         * que seja adicionado ao painel fique por cima da imagem, criando assim
         * o efeito de imagem de background que pretendemos.
         *
         * Se trocarmos a ordem, os efeitos podem não ser os esperados já que a
         * imagem vai ser desenhada em cima dos componentes que estão neste JPanel.
         * Se não existirem componente então a ordem não é relevante.
         */
//        super.paintComponent(g);
    }

    public void geraBarcos() {
        Random gerador = new Random();
        int numero;
        int percorre = 0;

        while (percorre < 9) {

            switch (percorre) {
                case 0:
                    numero = (gerador.nextInt(10)) * 10;
                    numero += gerador.nextInt(6) + 1;
                    //System.out.println("Numero sorteado: " + numero);
                    barcos.add(numero + 0);
                    barcos.add(numero + 1);
                    barcos.add(numero + 2);
                    barcos.add(numero + 3);
                    barcos.add(numero + 4);

                    barco5.add(numero + 0);
                    barco5.add(numero + 1);
                    barco5.add(numero + 2);
                    barco5.add(numero + 3);
                    barco5.add(numero + 4);

                    percorre++;
                    break;
                case 1:
                    numero = (gerador.nextInt(10)) * 10;
                    numero += gerador.nextInt(7) + 1;
                    //System.out.println("Numero sorteado: "+numero);
                    if (!barcos.contains(numero)) {
                        barcos.add(numero + 0);
                        barcos.add(numero + 1);
                        barcos.add(numero + 2);
                        barcos.add(numero + 3);

                        barco4.add(numero + 0);
                        barco4.add(numero + 1);
                        barco4.add(numero + 2);
                        barco4.add(numero + 3);

                        percorre++;
                    }

                    break;
                case 2:
                    numero = (gerador.nextInt(10)) * 10;
                    numero += gerador.nextInt(8) + 1;
                    //System.out.println("Numero sorteado: "+numero);
                    if (!barcos.contains(numero)) {
                        barcos.add(numero + 0);
                        barcos.add(numero + 1);
                        barcos.add(numero + 2);

                        barco3.add(numero + 0);
                        barco3.add(numero + 1);
                        barco3.add(numero + 2);

                        percorre++;
                    }
                    break;
                case 3:
                    numero = (gerador.nextInt(10)) * 10;
                    numero += gerador.nextInt(9) + 1;
                    //System.out.println("Numero sorteado: "+numero);
                    if (!barcos.contains(numero)) {
                        barcos.add(numero + 0);
                        barcos.add(numero + 1);

                        barco2.add(numero + 0);
                        barco2.add(numero + 1);

                        percorre++;
                    }
                    break;
                default:
                    numero = gerador.nextInt(100) + 1;
                    //System.out.println("Numero sorteado: "+numero);
                    if (!barcos.contains(numero)) {
                        barcos.add(numero);

                        barco1.add(numero);

                        percorre++;
                    }
            }
//            numero = gerador.nextInt(100);
//            barcos.add(numero + 1);
        }

    }

    private void desenhaPontosAndVez() {
        vez.setBounds(170, 165, 220, 25);
        vez.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 20));
        vez.setBackground(Color.WHITE);
        vez.setForeground(Color.WHITE);
        vez.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        if (minhaVez) {
            vez.setText("MINHA VEZ...");
        } else {
            vez.setText("NÃO É A SUA VEZ...");
        }
        add(vez);

//        pontos.setBounds(10, 165, 150, 25);
//        pontos.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 20));
//        pontos.setBackground(Color.WHITE);
//        pontos.setForeground(Color.WHITE);
//        pontos.setBorder(BorderFactory.createLineBorder(Color.WHITE));
//        pontos.setText("PONTOS: " + 0);
//        add(pontos);
    }

    private void desenhaCenario(boolean clique, int x, int y) {

        if (clique) {
            for (int linha = 0; linha < 100; linha += 10) {
                for (int coluna = 0; coluna < 10; coluna++) {

                    mar = lj.criaLabel(Imagemar, xPainel, yPainel);
                    add(mar);

                    labelPainel.add(mar);

                    map.put(mar, linha + coluna + 1);

                    labelPainel.get(linha + coluna).addMouseListener(trataEvento);
                    xPainel += 40;
                }
                xPainel = movX;
                yPainel += 40;
            }
        } else {
            geraBarcos();
            for (int linha = 0; linha < 100; linha += 10) {
                for (int coluna = 0; coluna < 10; coluna++) {

                    if (!barcos.contains(linha + coluna + 1)) {
                        mar = lj.criaLabel(Imagemar, xPainel, yPainel);
                        add(mar);
                        labelMeuPainel.add(mar);
                    } else {
                        verificaBarcos(linha + coluna + 1);

                    }

                    xPainel += 40;
                }
                xPainel = movX;
                yPainel += 40;
            }
        }
    }

    public void verificaBarcos(int pos) {
        if (barco5.contains(pos)) {
            imageBarco = new ImageIcon(this.getClass().getResource("Nav5" + barcoCinco + ".png"));
            newimg = imageBarco.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            imageBarco = new ImageIcon(newimg);

            desenhaBarco();
            barcoCinco++;
        } else if (barco4.contains(pos)) {
            imageBarco = new ImageIcon(this.getClass().getResource("Nav4" + barcoQuatro + ".png"));
            newimg = imageBarco.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            imageBarco = new ImageIcon(newimg);

            desenhaBarco();
            barcoQuatro++;
        } else if (barco3.contains(pos)) {
            imageBarco = new ImageIcon(this.getClass().getResource("Nav3" + barcoTres + ".png"));
            newimg = imageBarco.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            imageBarco = new ImageIcon(newimg);

            desenhaBarco();
            barcoTres++;
        } else if (barco2.contains(pos)) {
            imageBarco = new ImageIcon(this.getClass().getResource("Nav2" + barcoDois + ".png"));
            newimg = imageBarco.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            imageBarco = new ImageIcon(newimg);

            desenhaBarco();
            barcoDois++;
        } else {
            imageBarco = new ImageIcon(this.getClass().getResource("Nav1.png"));
            newimg = imageBarco.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            imageBarco = new ImageIcon(newimg);
            
            desenhaBarco();
            
        }
    }
    
    public void desenhaBarco(){
        
        mar = lj.criaLabel(imageBarco, xPainel, yPainel);
        add(mar);
        labelMeuPainel.add(mar);
    }

    public void startClient() {
        try {
            //connection = new Socket("10.11.150.222", 12345);
            input = new Scanner(connection.getInputStream());
            output = new Formatter(connection.getOutputStream());
            //output.format("connect");
        } catch (IOException ex) {
            System.out.println("SERVIDOR FORA DO AR");
            JOptionPane.showMessageDialog(null, "SERVIDOR DESCONECTADO");
            System.exit(0);
        }
        ExecutorService es = Executors.newFixedThreadPool(1);
        es.execute(this);
    }

    @Override
    public void run() {

        while (true) {
            String mensagem = input.nextLine();

            //System.out.println("MENSAGEM: "+mensagem);
            String frutas[] = mensagem.split(";");

            if (frutas[0].equals("jogada")) {
                if (barcos.contains(Integer.parseInt(frutas[1]))) {

                    //System.out.println("OLHA A EXPLOSAO");
                    String filename = "Bombasexplodindo.mp3";
                    mp3 = new MP3(filename);
                    mp3.play();
                    setIconLabel(labelMeuPainel.get(Integer.parseInt(frutas[1]) - 1), ImageExplosao);
                    
                    int remove = barcos.indexOf(Integer.parseInt(frutas[1]));
                    barcos.remove(remove);
                    
                    if (barcos.isEmpty()) {
                        output.format(frutas[1] + ",ganhou" + "\n");
                        output.flush();
                        new TelaPerdeu().setVisible(true);
                        minhaVez = false;
                    }else{
                        output.format(frutas[1] + ",acertou" + "\n");
                        output.flush();
                    }
                    

                } else {
                    //System.out.println("TIRO NA AGUA");
                    setIconLabel(labelMeuPainel.get(Integer.parseInt(frutas[1]) - 1), imageExplosaoAgua);
                    output.format(frutas[1] + ",agua" + "\n");
                    output.flush();
                }
                //}
            } else if (frutas[0].equals("acertou")) {
                String filename = "Bombasexplodindo.mp3";
                mp3 = new MP3(filename);
                mp3.play();
                ponto++;
                //System.out.println("OLHA A EXPLOSAO");
                setIconLabel(labelPainel.get(Integer.parseInt(frutas[1]) - 1), ImageExplosao);

                //pontos.setText("PONTOS: " + ponto);
                cliques.add(map.get(labelPainel.get(Integer.parseInt(frutas[1]) - 1)));

            } else if (frutas[0].equals("agua")) {
                //System.out.println("TIRO NA AGUA");
                setIconLabel(labelPainel.get(Integer.parseInt(frutas[1]) - 1), imageExplosaoAgua);

                cliques.add(map.get(labelPainel.get(Integer.parseInt(frutas[1]) - 1)));

                minhaVez = false;
                vez.setText("NÃO É A MINHA VEZ");
                output.format(frutas[1] + ",passavez" + "\n");
                output.flush();
            } else if (frutas[0].equals("passavez")) {
                minhaVez = true;
                vez.setText("MINHA VEZ");
            } else if (frutas[0].equals("ganhou")) {
                new TelaGanhou().setVisible(true);
                minhaVez = false;
            }

        }
    }

    class TrataEvento extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent arg0) {

        }

        @Override
        public void mousePressed(MouseEvent arg0) {
            if (minhaVez) {
                JLabel label = (JLabel) arg0.getSource();

                if (!cliques.contains(map.get(label))) {
                    System.out.println("VOCE CLICOU NO: " + map.get(label));
                    String filename = "lancamissil.mp3";
                    mp3 = new MP3(filename);
                    mp3.play();
                    setIconLabel(label, imageMissil);
                }

            }

        }

        @Override
        public void mouseReleased(MouseEvent arg0) {

            if (minhaVez) {
                JLabel label = (JLabel) arg0.getSource();

                if (!cliques.contains(map.get(label))) {
                    //System.out.println("ENVIA");
                    output.format(map.get(label) + ",jogada" + "\n");
                    output.flush();
                    mp3.close();

                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            if (!cliques.contains(map.get(label))) {
                setIconLabel(label, Imagemira);
            }

        }

        @Override
        public void mouseExited(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            if (!cliques.contains(map.get(label))) {
                setIconLabel(label, Imagemar);
            }

        }
    }

    public void setIconLabel(JLabel label, ImageIcon image) {
        label.setIcon(null);
        label.setOpaque(true);
        label.setIcon(image);
    }
}
