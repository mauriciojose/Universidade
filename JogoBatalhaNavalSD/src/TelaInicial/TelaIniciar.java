/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelaInicial;

import java.net.Socket;
import java.util.Scanner;
import java.util.Formatter;
import ReprodutorMP3.MP3;
import TelaDoJogo.FrameJogo;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 *
 * @author Mauricio José
 */
public final class TelaIniciar extends javax.swing.JFrame implements Runnable {

    private ControlProgress cp;
    private PainelInicial painelInicial;
    
    private JScrollPane listScroller;
    private JButton jb;
    
    private Socket cliente;
    private Scanner input;
    private Formatter output;
    private String msgServidor;
    private String fileName;
    private MP3 mp3;
    private String namePlayer;
    private boolean conectou;
    
    private Font fonte;

    private JList lista;
    private DefaultListModel model;

    /**
     * Creates new form TelaIniciar
     */
    public TelaIniciar() {

        initComponents();
        inicializaVariaveis();
        update();
        startClient();
        mp3.play();
    }

    private void inicializaVariaveis() {

        painelInicial = new PainelInicial();
        painelInicial.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(painelInicial);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        fileName = "BatalhaNaval.mp3";
        mp3 = new MP3(fileName);

        conectou = true;
    }

    public void addList(){
        model = new DefaultListModel();

        lista = new JList(model);
        lista.setFont(fonte);
        lista.setBackground(new Color(1.0f, 1.0f, 1.0f,0.8f));
        
        listScroller = new JScrollPane(lista);
        listScroller.setBounds(135, 150, 140, 90);
        
        painelInicial.add(listScroller);
        addJButon();
    }
  
    public void addModel(String[] elements, String name){
        int contModel = 0;
        java.util.List<String> itemList = Arrays.asList(elements);
        //itemList = removeName(itemList, name);
       int index = itemList.indexOf(name);
        model.clear();
        for (int i = 0; i < itemList.size(); i++) {
            if (i != index) {
                model.add(contModel, itemList.get(i).trim());
                contModel++;
            }
        }
         lista.setModel(model);
    }
    
    public void addJButon()
    {
        jb = new JButton("INICIAR");
        jb.setFont(fonte);
        jb.setBounds(150, 240, 100, 40);
        jb.addActionListener( new ActionListener() {
                     public void actionPerformed(ActionEvent e) {
                         output.format("iniciar,"+lista.getSelectedValue()+"\n");
                         output.flush();
                         //System.out.println("selecionado: "+lista.getSelectedValue());
                     }
            });
        painelInicial.add(jb);
    }
    private void startClient() {

        ExecutorService es = Executors.newFixedThreadPool(1);
        es.execute(this);
    }

    public void conecta() {

        while (conectou) {

            try {
                cliente = new Socket("localhost", 12345);
                input = new Scanner(cliente.getInputStream());
                output = new Formatter(cliente.getOutputStream());

                conectou = false;
                msgServidor = "wait";
                cp.setMsgServidor(msgServidor);
                addList();
            } catch (IOException ex) {
                msgServidor = "desconectado";
                cp.setMsgServidor(msgServidor);
            }
        }
    }

    public void recebeNome() {
        while (true) {
            namePlayer = JOptionPane.showInputDialog(null, "POR FAVOR DIGITE SEU NOME!");
            
            if (namePlayer == null) {
                namePlayer = "";
            }
            if (namePlayer.equals("")) {
                //
                output.format("conectar,player\n");
                output.flush();
                //
                msgServidor = input.nextLine();
                //System.out.println("TEXTO: " + msgServidor);
                String separaTexto[] = msgServidor.split(";");
                if (separaTexto[0].equals("NomeValidoPlayer")) {
                 
                    namePlayer = separaTexto[1];
                    separaTexto = separaTexto[1].split(",");

                    break;
                } else {
                    //System.out.println("Nome InValido");
                }
            } else {
                output.format("conectar," + namePlayer + "\n");
                output.flush();
                
                msgServidor = input.nextLine();
                //System.out.println("TEXTO: " + msgServidor);
                String separaTexto[] = msgServidor.split(";");
                
                if (separaTexto[0].equals("NomeValido")) {
                    
                    separaTexto[1] = separaTexto[1].substring(1, separaTexto[1].length() - 1);
                    separaTexto[1] = separaTexto[1].replaceAll(" ","");
                    
                    //System.out.println("Replace: "+separaTexto[1]);
                    separaTexto = separaTexto[1].split(",");

                    addModel(separaTexto, namePlayer);
                    break;
                }
            }
        }
    }
    
    public void updateLista()
    {
        while (true) {      
            
            msgServidor = input.nextLine();
      
            String separaTexto[] = msgServidor.split(";");
            
            if (separaTexto[0].equals("NomeValido")) {
                   
                    separaTexto[1] = separaTexto[1].substring(1, separaTexto[1].length() - 1);
                    separaTexto[1] = separaTexto[1].replaceAll(" ","");
                    //System.out.println("Replace: "+separaTexto[1]);
                    separaTexto = separaTexto[1].split(",");
                    addModel(separaTexto,namePlayer);
                    
            }else{
                if (separaTexto[0].equals("iniciar")) {
                    output.format("iniciou,"+separaTexto[2]+"\n");
                    output.flush();
                    try {
                        painelInicial.remove(listScroller);
                        painelInicial.remove(jb);
                        painelInicial.updateUI();
                        
                        msgServidor = "start";
                        cp.setMsgServidor(msgServidor);
                        
                        
                        Thread.sleep(7000);
                        //JOptionPane.showMessageDialog(null, "JOGO INICIADO...");
                        if(separaTexto[1].equals("VezTrue"))
                        {
                            new FrameJogo(cliente,true).setVisible(true);
                        }else{
                            if (separaTexto[1].equals("VezFalse")) {
                                new FrameJogo(cliente,false).setVisible(true);
                            }
                        }
                        break;
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(null, "FALHA NA THREAD FINAL");
                    }
                }
                
            }
        }
        mp3.close();
        this.dispose();
    }

    @Override
    public void run() {
        conecta();
        recebeNome();
        updateLista();
    }

    public void update() {
        cp = new ControlProgress(painelInicial);
        ExecutorService es = Executors.newFixedThreadPool(2);
        es.execute(cp);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaIniciar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaIniciar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaIniciar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaIniciar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaIniciar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
