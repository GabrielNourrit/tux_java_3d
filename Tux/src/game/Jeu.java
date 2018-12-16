package game;

import env3d.Env;
import fenetre.Fenetre;
import fr.ujfGrenoble.miage.tux.XMLUtil.DocumentTransform;
import game.tux.Partie;
import game.tux.Profil;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.lwjgl.input.Keyboard;
// Import
import management.DevineLeMot;
import management.Dico;
import management.LectureClavier;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Jeu extends LectureClavier {

    private final Env _env; //Environnement
    private boolean _finished; //Jeu fini ou pas
    //private final  Room _room; //Notre pièce
    private DevineLeMot _devineLeMot;
    private final Dico _dico;
    private Profil _profil;

    public Jeu() {
        // Create the new this._environment.  Must be done in the same
        // method as the game loop
        this._env = new Env();
        this._dico = new Dico("dico");
        this._dico.addWordToDico(1, "oui");
        this._dico.addWordToDico(1, "non");
        this._dico.addWordToDico(1, "yes");       
        this._dico.addWordToDico(2, "maman");
        this._dico.addWordToDico(3, "ordinateur");
        this._dico.addWordToDico(4, "polymorphisme");
        this._dico.addWordToDico(5, "anticonstitutionellement");
        // Turn off the default controls
        this._env.setDefaultControl(false);

        //initialize 
        this._finished = false;  
    }
    
    public Jeu(String filename) throws XPathExpressionException {
        this._env = new Env();
        //Création du dictionnaire
        this._dico = new Dico("src/xml/dico.xml");
        
        // Turn off the default controls
        this._env.setDefaultControl(false);
        this._env.setResolution(950, 650, 0);
        //initialize 
        this._finished = false;
        //Création du profil du joueur
        this._profil = new Profil(filename);
    }
    
    /**
    * Méthode qui permet de bouger la caméra
    * @param {int} currentKey, la touche pressée
    * @return {void}
    */
    public void moveCamera(int currentKey) {
        //Variable définissant la taille du pas
        int step = 1;
        //Si la touche Z est appuyée
        if (currentKey == Keyboard.KEY_Z) {
            /*Pour bouger la caméra en haut on déplace cameraPitch*/
            /*La nouvelle positon camera = ancienne position camera + le pas
              Si la nouvelle position est dans la pièce on bouge dans la camera*/
            if (this._env.getCameraPitch() + step <= 16.5)
                this._env.setCameraPitch(this._env.getCameraPitch()+step);
            /*Si on est en dehors de la pièce on fait rien*/
        }
        //Si la touche S est appuyée
        if (currentKey == Keyboard.KEY_S) {
            /*Pour bouger la caméra en haut on déplace cameraPitch*/
            /*La nouvelle positon camera = ancienne position camera + le pas
              Si la nouvelle position est dans la pièce on bouge dans la camera*/
            if (this._env.getCameraPitch() - step >= -27.5)
                this._env.setCameraPitch(this._env.getCameraPitch()-step);
            /*Si on est en dehors de la pièce on fait rien*/
        }
    }

  
	/**
    * Méthode de jeu
    * @param {void}
    * @return {void}
    */  
    public void jouer() throws InterruptedException, AWTException, ParserConfigurationException, SAXException, IOException, Exception {
        // The main game loop
        this._finished = false; //Le jeu n'est pas fini
        Fenetre fenetreNiveau; //Fenetre du niveau
        Partie partie; //La partie qui va être jouée
        /*Tant que le jeu n'est pas fini*/
        while (!this._finished) {
          
            //Si on appuie sur exit 
            if (this._env.getKey() == Keyboard.KEY_ESCAPE) {
                //on dit que c'est la fin du jeu
                this._finished = true;
            }
            
            fenetreNiveau = new Fenetre("Niveau"); //Création d'une nouvelle fenêtre permmettant le choix du niveau
            int niveau = fenetreNiveau.getNiveau();
            //Tant que l'utilisateur n'a pas choisi de niveau
            while ((niveau != 1) && (niveau != 2) && (niveau != 3) && (niveau != 4) && (niveau !=5)) {
                niveau = fenetreNiveau.getNiveau(); //On revérifie si l'utilisateur a choisi un niveau ou non
                System.out.flush();//vider buffer : !!!ATTENTION!!! Ne pas enveler sinon bug
            }
            String mot = this._dico.getWordFromListLevel(niveau); //On récupère un mot au hasard du niveau rentré par l'utilisateur
            //Création d'une nouvelle partie avec la date, le mot et le niveau
            partie = new Partie(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), mot, niveau); //On crée une partie avec la date du jour
            int temps = 0;
            switch(niveau) {
                case 1: temps = 60000; break;//1min
                case 2: temps = 90000; break;//1min30sec
                case 3: temps = 120000; break;//2min
                case 4: temps = 150000; break;//2min30sec
                case 5: temps = 180000; break;//3min
            }
            this._devineLeMot = new DevineLeMot(mot, this._env, temps); //On crée notre partie avec le mot et l'environnement
            this._devineLeMot.jouer(); //On lance notre partie
            
            //Une fois la partie fini on sauvegarde les données
            partie.setTemps(this._devineLeMot.getTemps()); //Temps mis par le joueur
            partie.setTrouve(this._devineLeMot.getNbLettresRestantes(), mot); //Attribut found
            this._profil.ajouterPartie(partie); //On ajoute notre partie au profile
            this._profil.sauvegarder("src/xml/profile.xml"); //On sauvegarde les données dans un fichier XML
            
            //Transformation XSLT de notre XML et sauvegarde dans un fichier XML
            Document doc = this._profil.fromXML("src/xml/profile.xml");  
            System.out.println(DocumentTransform.fromXSLTransformation("src/xml/profile.xsl",doc));
            this._profil.ecrire(DocumentTransform.fromXSLTransformation("src/xml/profile.xsl",doc), "src/xml/profile.html");
                       
            //On crée un robot pour faire relachée si une touche est encore appuyée (evite que Tux bouge tout seul au début de la partie)
          	Robot robot = new Robot(); 
            if (this._env.getKeyDown() == Keyboard.KEY_UP) {
                robot.keyRelease(KeyEvent.VK_UP); 
            }
            if (this._env.getKeyDown() == Keyboard.KEY_DOWN) {
                robot.keyRelease(KeyEvent.VK_DOWN);
            }
            if (this._env.getKeyDown() == Keyboard.KEY_LEFT) {
                robot.keyRelease(KeyEvent.VK_LEFT);
            }
            if (this._env.getKeyDown() == Keyboard.KEY_RIGHT) {
                robot.keyRelease(KeyEvent.VK_RIGHT);
            }
            
            
            Fenetre fenetre = new Fenetre("Rejouer"); //Création d'une nouvelle fenêtre permmettant de choisir de rejouer ou non
            String rejouer = fenetre.getTexte(); //On récupère le choix du joueur (oui ou non)
            //Tant que l'utilisateur n'a pas cliqué sur un bouton
            while (!rejouer.equals("oui") && !rejouer.equals("non")) {
                rejouer = fenetre.getTexte(); //On regarde si le joueur a cliqué sur le bouton
                System.out.flush();//vider buffer : !!!ATTENTION!!! Ne pas enveler sinon bug
            }
           
            //Si le joueur ne veut pas rejouer
            if (rejouer.equals("non")) {
               this. _finished = true; //Le jeu est fini
               //Ouverture du fichier du joueur dans le navigateur par défaut
               Desktop d = Desktop.getDesktop();
               d.open(new File( "src/xml/profile.html"));
            }                                 
        }        
        this._env.exit();  
    }
}