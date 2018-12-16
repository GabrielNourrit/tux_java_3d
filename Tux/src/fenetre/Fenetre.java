package fenetre;

import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.EventListener;
import javax.swing.*;

public class Fenetre extends JFrame implements EventListener, ActionListener {
    private String _texte;
    private int _niveau;
    private JButton _boutonOui;
    private JButton _boutonNon;
    private JButton _bouton1;
    private JButton _bouton2;
    private JButton _bouton3;
    private JButton _bouton4;
    private JButton _bouton5;
    
    
    public Fenetre(String type) {
        this._texte = "";
        this._niveau = 0;
        //Si l'on veut une fenêtre pour demander à l'utilisateur si il veut rejouer
        if (type.contentEquals("Rejouer")) {
            //Initialisation de la fenêtre
            this.setTitle("Rejouer"); //Titre de la fenêtre
            this.setSize(300, 120); //Taille de la fenêtre
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //La fenêtre se ferme si on clique sur la croix
            this.setLocationRelativeTo(null); //Placer la fenêtre au centre de l'écran

            Box vbox = Box.createVerticalBox(); //Container Vertical qui contiendra le texte et les boutons

            JPanel panelTexte = new JPanel(); //Panneau du texte
            JPanel panelBouttons = new JPanel(); //Panneau des boutons
            vbox.add(panelTexte); //On ajoute le panneau du texte au container
            vbox.add(panelBouttons); //On ajoute le panneau des boutons au container
            
            JLabel labelTexte = new JLabel("Voulez-vous rejouer ?"); //Initialisation du texte
            panelTexte.add(labelTexte); //On ajoute le texte à son panneau
            labelTexte.setVerticalTextPosition(SwingConstants.TOP); //On place le texte en haut

            panelBouttons.setLayout(new FlowLayout()); //Layout qui contiendra nos boutons
            
            //Initialisation de nos boutons
            this._boutonOui = new JButton("Oui"); //Bouton oui
            this._boutonNon = new JButton("Non"); //Bouton non    
            panelBouttons.add(this._boutonOui); //On ajoute le bouton oui au layout
            panelBouttons.add(this._boutonNon); //On ajoute le bouton non au layout
            this._boutonOui.addActionListener(this); //On écoute les actions sur le bouton oui
            this._boutonNon.addActionListener(this);//On écoute les actions sur le bouton oui

            this.getContentPane().add(vbox); //On ajoute notre container à la fenêtre
            this.setVisible(true); //On passe la fenêtre en visible
        }
        //Si l'on veut une fenêtre pour demander à l'utilisateur le niveau
        if (type.contentEquals("Niveau")) {
            this.setTitle("Choix niveau"); //Titre de la fenêtre
            this.setSize(600, 120); //Taille de la fenêtre
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//La fenêtre se ferme si on clique sur la croix
            this.setLocationRelativeTo(null); //Placer la fenêtre au centre de l'écran

            Box vbox = Box.createVerticalBox(); //Container Vertical qui contiendra le texte et les boutons

            JPanel panelTexte = new JPanel(); //Panneau du texte
            JPanel panelBouttons = new JPanel(); //Panneau des boutons
            vbox.add(panelTexte); //On ajoute le panneau du texte au container
            vbox.add(panelBouttons); //On ajoute le panneau des boutons au container
            
            JLabel jLabel1 =new JLabel("Veuillez choisir le niveau"); //Initialisation du texte
            panelTexte.add(jLabel1); //On ajoute le texte à son panneau
            jLabel1.setVerticalTextPosition(SwingConstants.TOP);  //On place le texte en haut

            panelBouttons.setLayout(new FlowLayout());//Layout qui contiendra nos boutons
            
            //Initialisation de nos boutons
            this._bouton1 = new JButton("Niveau 1"); //Bouton niveau1
            this._bouton2 = new JButton("Niveau 2"); //Bouton niveau2
            this._bouton3 = new JButton("Niveau 3"); //Bouton niveau3
            this._bouton4 = new JButton("Niveau 4"); //Bouton niveau4
            this._bouton5 = new JButton("Niveau 5"); //Bouton niveau5
            
            //On ajoute nos boutons au layout
            panelBouttons.add(this._bouton1);
            panelBouttons.add(this._bouton2);
            panelBouttons.add(this._bouton3);
            panelBouttons.add(this._bouton4);
            panelBouttons.add(this._bouton5);
            
            //On écoute les évènements sur nos boutons
            this._bouton1.addActionListener(this);
            this._bouton2.addActionListener(this);
            this._bouton3.addActionListener(this);
            this._bouton4.addActionListener(this);
            this._bouton5.addActionListener(this);

            this.getContentPane().add(vbox); //On ajoute notre container à la fenêtre
            this.setVisible(true); //On passe la fenêtre en visible            
        }
   }

    //Méthode qui se déclenche lors d'un clic sur un bouton
    @Override
    public void actionPerformed(ActionEvent e) {
        //Si le clic était sur le bouton oui
        if (e.getSource() == this._boutonOui) {
            System.out.println("Clic Oui");
            this._texte = "oui"; //On passe le texte à oui
            //On enlève la fenêtre de l'écran
            this.setVisible(false); 
            this.dispose();
        }
        //Si le clic était sur le bouton non
        if (e.getSource() == this._boutonNon) {
            System.out.println("Clic Non");
            this._texte = "non"; //On passe le texte à non
            //On enlève la fenêtre de l'écran
            this.setVisible(false);
            this.dispose();
        }
        //Si le clic était sur le bouton 1
        if (e.getSource() == this._bouton1) {
            System.out.println("Clic 1");
            this._niveau = 1; //On passe le niveau à 1
            //On enlève la fenêtre de l'écran
            this.setVisible(false);
            this.dispose();
        }
        //Si le clic était sur le bouton 2
        if (e.getSource() == this._bouton2) {
            System.out.println("Clic 2");
            this._niveau = 2; //On passe le niveau à 2
            //On enlève la fenêtre de l'écran
            this.setVisible(false);
            this.dispose();
        }
        //Si le clic était sur le bouton 3
        if (e.getSource() == this._bouton3) {
            System.out.println("Clic 3");
            this._niveau = 3; //On passe le niveau à 3
            //On enlève la fenêtre de l'écran
            this.setVisible(false);
            this.dispose();
        }
        //Si le clic était sur le bouton 4
        if (e.getSource() == this._bouton4) {
            System.out.println("Clic 4");
            this._niveau = 4; //On passe le niveau à 4
            //On enlève la fenêtre de l'écran
            this.setVisible(false);
            this.dispose();
        }
        //Si le clic était sur le bouton 5
        if (e.getSource() == this._bouton5) {
            System.out.println("Clic 5");
            this._niveau = 5; //On passe le niveau à 5
            //On enlève la fenêtre de l'écran
            this.setVisible(false);
            this.dispose();
        }      
    }
    
    //Accesseur pour l'attribut _texte
    public String getTexte() {
        return this._texte;
    }
    //Accesseur pour l'attribut _niveau
    public int getNiveau() {
        return this._niveau;
    }
}
