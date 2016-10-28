/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starwarsrevised;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author TreyAmador
 */
public class StarWarsX extends JFrame {
    private MainPanel mainPanel;
    public StarWarsX() {
        this.setTitle("Star Wars");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new MainPanel();
        add(mainPanel);
        this.setVisible(true);
        setResizable(false);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new StarWarsX();
    }
    
}