package game;

import java.awt.AWTException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class LanceurDeJeu {
    public static void main(String[] args) throws  InterruptedException, AWTException, ParserConfigurationException, SAXException, IOException, Exception {
        //Instanciate a new Jeu
        Jeu jeu = new Jeu("src/xml/profile.xml");
        
        //Play the game
        jeu.jouer();
    }
    
}
