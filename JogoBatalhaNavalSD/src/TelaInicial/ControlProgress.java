/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelaInicial;


import javax.swing.JOptionPane;

/**
 *
 * @author Mauricio José
 */
public class ControlProgress implements Runnable{
    
    private PainelInicial painelInicial;
    String msgServidor;

    public ControlProgress(PainelInicial painelInicial) {
        this.painelInicial = painelInicial;
        inicializaVariaveis();
    }
    private void inicializaVariaveis()
    {
        msgServidor = "";
    }
    @Override
    public void run() {
        while(true)
        {
            //System.out.println("Mensagem: "+msgServidor);
            if (msgServidor.equals("wait")) {
                painelInicial.setEstadoServidor("wait");
                painelInicial.repaint();
                
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, "Falha na Thread Sleep");
                }
            } else {
                if (msgServidor.equals("start")) {
                    painelInicial.setEstadoServidor(msgServidor);
                    painelInicial.repaint();
                    try {
                    Thread.sleep(80);
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(null, "Falha na Thread Sleep");
                    }
                }
                else{
                    if (msgServidor.equals("desconectado")||msgServidor.equals("")) {
                        painelInicial.setEstadoServidor(msgServidor);
                        painelInicial.setDesconectado("SERVIDOR DESCONECTADO");
                        painelInicial.repaint();
                    try {
                    Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        JOptionPane.showMessageDialog(null, "Falha na Thread Sleep");
                    }
                    }
                }
            }
        }
    }
    
    //MÉTODOS GETS E SETS
    
    public String getMsgServidor() {
        return msgServidor;
    }

    public void setMsgServidor(String msgServidor) {
        this.msgServidor = msgServidor;
    }
    
}
