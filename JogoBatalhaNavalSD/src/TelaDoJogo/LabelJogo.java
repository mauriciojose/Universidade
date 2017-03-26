/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelaDoJogo;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Mauricio José
 */
public class LabelJogo{
    JLabel label;
    public LabelJogo() {
    }
    public JLabel criaLabel(ImageIcon logo,int x,int y)
    {
        //System.out.println("X: "+x+" Y: "+y);
        label = new JLabel();
        label.setOpaque(true);
        label.setIcon(logo);
        label.setBackground(Color.GRAY);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setBounds(x, y, 40, 40); //método setBounds
        return label;
    }
    
}
