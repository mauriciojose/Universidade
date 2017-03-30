/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReprodutorMP3;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.swing.JOptionPane;
import javazoom.jl.player.Player;


public class MP3 {
    private String filename;
    private Player player; 

    // constructor that takes the name of an MP3 file
    public MP3(String filename) {
        this.filename = filename;
    }

    public void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public void play() {
        try {
            //File file = new File(this.getClass().getResource(filename).toURI());
            InputStream is= getClass().getResourceAsStream(filename);
            //FileInputStream fis     =  new FileInputStream(is);
            BufferedInputStream bis = new BufferedInputStream(is);
            player = new Player(bis);
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Problem playing file " + filename);
            JOptionPane.showMessageDialog(null, e);
//            System.out.println("Problem playing file " + filename);
//            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { player.play(); }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();



    }
    public static void main(String[] args) {
        String filename = "C:\\Users\\Mauricio José\\Desktop\\Batalha Naval.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.play();

        // do whatever computation you like, while music plays
        int N = 4000;
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += Math.sin(i + j);
            }
        }
        System.out.println(sum);

        // when the computation is done, stop playing it
        mp3.close();

        // play from the beginning
        mp3 = new MP3(filename);
        mp3.play();

    }

    }