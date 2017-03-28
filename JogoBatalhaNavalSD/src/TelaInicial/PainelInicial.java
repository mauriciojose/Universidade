/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelaInicial;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author Mauricio José
 */
public class PainelInicial extends javax.swing.JPanel{
    
    private String aguardando;
    private String percorreAguardando;
    private int tamanhoAguardando;
    private int contAguardando;
    private int efeitoDesaparecer;
    
    private BufferedImage imagemFundo;
    
    private float alphaQuadrado;
    private float alphaAguardando;
    private float alphaDesaparecer;
    
    private AlphaComposite alcomQuadrado;
    private AlphaComposite alcomAguardando;
    private AlphaComposite alcomDesaparecer;
    
    private String estadoServidor;
    private String desconectado;
    private Font fonte;

    JList lista;
    DefaultListModel model;
    
    public PainelInicial() {
        setLayout(null);
        iniciaVariaveis();
    }
    
    
    private void iniciaVariaveis()
    {
        estadoServidor = "wait";
        aguardando = "AGUARDANDO...";
        percorreAguardando = "";
        tamanhoAguardando = aguardando.length()-1;
        
        contAguardando = 0;
        efeitoDesaparecer = 0;
        
        alphaQuadrado = 0.8f;
        alcomQuadrado = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alphaQuadrado);
        
        alphaAguardando = 0.8f;
        alcomAguardando = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alphaAguardando);
        
        alphaDesaparecer = 1.0f;
        alcomDesaparecer = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaDesaparecer);
        
        try {
            imagemFundo = ImageIO.read(this.getClass().getResource("SPLASH.jpg"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"IMAGEM DE FUNDO NÃO ENCONTRADA");
        }
        desconectado = "PLAYER 2 DESCONECTADO";
        fonte = new Font("Verdana", 3, 13);      
        //addList();
    }
    @Override 
    public void paintComponent(Graphics g)
    {
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (estadoServidor.equals("desconectado")) {
            g.drawImage(imagemFundo, 0, 0, this.getWidth(), this.getHeight(), this);
            desenhaQuadrado(g);
            desenhaAguardando(g);
        } else {
            if (estadoServidor.equals("wait")) {
            
                g.drawImage(imagemFundo, 0, 0, this.getWidth(), this.getHeight(), this);
                
            }else {
                if (estadoServidor.equals("start")) {
                
                    g.drawImage(imagemFundo, 0, 0, this.getWidth(), this.getHeight(), this);
                    desenhaQuadrado(g);
                    desenhaIniciando(g);
                }
            }
            
        }
    }
    public void desenhaQuadrado(Graphics g)
    {
        Graphics2D desenhaQuadrado = (Graphics2D) g;
        desenhaQuadrado.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        desenhaQuadrado.translate(this.getWidth() / 4, this.getHeight() / 2);
        desenhaQuadrado.setComposite(alcomQuadrado);
        desenhaQuadrado.setColor(Color.WHITE);
        desenhaQuadrado.fillRoundRect(10, 30, 200, 70, 300, 10);
    }
    public void desenhaAguardando(Graphics g)
    {
        Graphics2D desenhaString = (Graphics2D) g;
        
        desenhaString.setComposite(alcomAguardando);
        desenhaString.setColor(Color.BLACK);
        desenhaString.setFont(fonte);
        percorreAguardando = percorreAguardando + aguardando.charAt(contAguardando);
        desenhaString.drawString(percorreAguardando, 45, 47);//criar variaveis para esses valores
        desenhaString.setColor(Color.RED);
        desenhaString.setFont(new Font("Verdana", 3, 13));
        desenhaString.drawString(desconectado, -20 + 35, 7 + 70);
        //if percorrendo aguardando
        if (contAguardando == tamanhoAguardando) {
            contAguardando = 0;
            percorreAguardando = "";
        } else {
            contAguardando++;
        }
    }
     public void desenhaIniciando(Graphics g)
    {
        Graphics2D desenhaString = (Graphics2D) g;
        if (efeitoDesaparecer < 9) {
            desenhaStringIniciar(desenhaString, true);
            efeitoDesaparecer++;
        } else {
            if (efeitoDesaparecer < 18) {
                desenhaStringIniciar(desenhaString, false);
                efeitoDesaparecer++;
            }
            else{
                alphaDesaparecer = alphaDesaparecer+0.1f;
                desenhaStringIniciar(desenhaString, true);
                efeitoDesaparecer = 0;
            }
        }
    }
    public void desenhaStringIniciar(Graphics2D desenhaString, boolean desaparece)
    {   
        if (desaparece) {
            alphaDesaparecer = alphaDesaparecer - 0.1f;
        }
        else{alphaDesaparecer = alphaDesaparecer + 0.1f;}
        
        alcomDesaparecer = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaDesaparecer);
        desenhaString.setComposite(alcomDesaparecer);
        desenhaString.setColor(Color.BLACK);
        desenhaString.setFont(fonte);
        desenhaString.drawString("INICIANDO...", 50, 47);
//        desenhaString.setColor(Color.RED);
//        desenhaString.drawString("PLAYER 2 CONECTADO", -20+48, 7+70);
    }
    
    //MÉTODOS GETTERS E SETTERS
    public void setEstadoServidor(String estadoServidor) {
        this.estadoServidor = estadoServidor;
    }

    public void setDesconectado(String desconectado) {
        this.desconectado = desconectado;
        
        
    }
    
}
