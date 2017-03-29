package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Mauricio José
 */
public class Servidor extends Thread {

    private static ArrayList<Formatter> jogadores = new ArrayList();
    private static ArrayList<String> nomes = new ArrayList();
    private static ArrayList<String> nomesAuxiliar = new ArrayList();
    private String nomeJogador;
    private Formatter output;
    private Formatter outputAdversario;
    private Socket connection;
    public boolean inicia = true;
    private static int contConexoes;

    static HashMap<String, Formatter> map = new HashMap<>();
    //static HashMap<String, Servidor> Serv = new HashMap<>();

    public Servidor(Socket connection) {
        this.connection = connection;
    }

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket conexao;
        Scanner input;
        Formatter output;
        final int porta = 12345;
        //CLIENTES = new Vector();

        Thread t;
        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("SERVIDOR CONECTADO");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "FALHA AO INICIAR SERVIDOR");
            System.exit(0);
        }

        while (true) {

            if (contConexoes < 40) {
                try {

                    conexao = serverSocket.accept();
                    contConexoes++;
                    //System.out.println("CONEXAO ACEITA...");

                    t = new Servidor(conexao);
                    t.start();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "ERRO AO SE CONECTAR COM CLIENTE");
                }
            }
        }
    }

    @Override
    public void run() {
        try {

            output = new Formatter(this.connection.getOutputStream());

            BufferedReader entrada = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            autentica(entrada, output);
            Inicia(entrada, output);

            while (true) {

                try {
                    String texto = entrada.readLine();
                    System.out.println("TEXTO: " + texto);
                    String separaTexto[] = texto.split(",");
                    if (separaTexto[0].equals("sair")) {
                        
                        int index = nomes.indexOf(separaTexto[1]);
                        nomes.remove(index);
                        map.remove(separaTexto[1], output);
                        
                        outputAdversario.format("saiu;\n");
                        outputAdversario.flush();
                        
                        output.format("saidaAceita;\n");
                        output.flush();
                        
                        connection.close();
                        
                    } else {
                        
                        if (separaTexto[0].equals("sairIni")) {
                            int index = nomes.indexOf(separaTexto[1]);
                            nomes.remove(index);
                            map.remove(separaTexto[1], output);

                            jogadores.remove(output);
                            nomesAuxiliar.remove(nomeJogador);
                            
                            connection.close();

                        } else {
                            if (separaTexto[0].equals("desconectar")) {
                            int index = nomes.indexOf(separaTexto[1]);
                            nomes.remove(index);
                            map.remove(separaTexto[1], output);
                            
                            connection.close();

                            } else {
                                outputAdversario.format(separaTexto[1] + ";" + separaTexto[0] + "\n");
                                outputAdversario.flush();

                            }

                        }
                        
                    }
                    //System.out.println("ENVIANDO...:" + separaTexto[0]);
                    

                } catch (IOException e) {
                    try {
                        connection.close();
                        break;
                    } catch (IOException ex) {
                        System.out.println("Falha ao fechar conexao...");
                    }
                }
            }
            //System.out.println("Deus é o maior");
        } catch (IOException ex) {
            try {
                connection.close();
            } catch (IOException ex1) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
    }

    public void autentica(BufferedReader entrada, Formatter output) throws IOException {

        while (true) {
            String textoCliente = entrada.readLine();
            //System.out.println("TEXTO: " + textoCliente);
            String separaTexto[] = textoCliente.split(",");
            if (separaTexto[0].equals("conectar") && !nomes.contains(separaTexto[1])) {

                nomeJogador = separaTexto[1];
                
                if (separaTexto[1].equals("player")) {
                    
                    nomeJogador = nomeJogador+contConexoes;
                    //System.out.println("Servidor.Servidor.autentica(): "+nomeJogador);
                    nomes.add(nomeJogador);
                    jogadores.add(output);
                    System.out.println(nomeJogador);
                    map.put(nomeJogador, output);

                    nomesAuxiliar.add(nomeJogador);
                    output.format("NomeValidoPlayer;"+nomeJogador+"\n");
                    output.flush();
                    sendToAll();
                    break;
                } else {
 
                    nomes.add(separaTexto[1]);
                    jogadores.add(output);
                    System.out.println(nomeJogador);
                    map.put(nomeJogador, output);

                    nomesAuxiliar.add(separaTexto[1]);

                    sendToAll();
                    break;
                }

            } else {
                output.format("NomeInvalido;\n");
                output.flush();
            }
        }
    }

    public void Inicia(BufferedReader entrada, Formatter output) throws IOException {

        while (true) {
            String textoCliente = entrada.readLine();
            //System.out.println("Cliente: " + nomeJogador + "TEXTO: " + textoCliente);
            String separaTexto[] = textoCliente.split(",");
            if (separaTexto[0].equals("iniciar") && map.containsKey(separaTexto[1])) {
                Random random = new Random(99);
                if ((random.nextInt() + 1) % 2 == 0) {
                    //System.out.println("TRUE");
                    output.format("iniciar;" + "VezTrue;" + separaTexto[1] + "\n");
                    output.flush();
                    map.get(separaTexto[1]).format("iniciar;" + "VezFalse;" + nomeJogador + "\n");
                    map.get(separaTexto[1]).flush();
                } else {
                    //System.out.println("FALSE");
                    output.format("iniciar;" + "VezFalse;" + separaTexto[1] + "\n");
                    output.flush();
                    map.get(separaTexto[1]).format("iniciar;" + "VezTrue;" + nomeJogador + "\n");
                    map.get(separaTexto[1]).flush();
                }

                jogadores.remove(map.get(separaTexto[1]));
                nomesAuxiliar.remove(separaTexto[1]);

                jogadores.remove(output);
                nomesAuxiliar.remove(nomeJogador);

//                map.remove(separaTexto[1], map.get(separaTexto[1]));
//                map.remove(nomeJogador, output);                
                sendToAll();
            } else if (separaTexto[0].equals("iniciou")) {
                outputAdversario = map.get(separaTexto[1]);
                map.remove(separaTexto[1], map.get(separaTexto[1]));
                break;
            }
        }
    }

    public void sendToAll() throws IOException {
        int envia = 0;
        while (envia < jogadores.size()) {
   
            jogadores.get(envia).format("NomeValido;" + nomesAuxiliar.toString() + "\n");
            jogadores.get(envia).flush();
            envia++;
        }
    }
}
