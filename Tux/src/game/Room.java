package game;

import env3d.Env;
import java.util.ArrayList;
import management.Dico;
import management.Letter;
import management.Position;
import org.w3c.dom.Document;

public final class Room {

    public int width; //axe x
    public int height; //axe y
    public int depth; //axe z
    public Env _env;
    public String _nom; //Nom de la room (gauche, droite, milieu)
    private final String textureBottom;
    private final String textureNorth;
    private final String textureEast;
    private final String textureWest;
    private String textureTop;
    private String textureSouth;
    public final ArrayList<Position> tabPosition; //Tableau contenant les positions des objets
    public ArrayList<Door> _tabDoor; //Tableau contenant les portes
    public ArrayList<Letter> _tabOmbre; //Tableau contenant les ombres
    public ArrayList<Letter> _letters; //Tableau contenant les lettres
    public int _i;

    public Room(Env env, char[] lettre, String mot, String filename, String nom) {      
      	//On crée notre Document contenant notre dico
      	Document doc = Dico.fromXML(filename);
      	
        //Initialisation des attributs
      	this.tabPosition = new ArrayList<Position>(); 
        this._letters = new ArrayList<Letter>(); 
        this._tabOmbre = new ArrayList<Letter>();
      	this._tabDoor = new ArrayList<Door>();
        this._nom = nom;
        
      	//Initialisation des attributs grâce au parsage DOM du fichier XML
        this.width = Integer.parseInt(doc.getElementsByTagName("width").item(0).getTextContent());
        this.height = Integer.parseInt(doc.getElementsByTagName("height").item(0).getTextContent());
        this.depth = Integer.parseInt(doc.getElementsByTagName("depth").item(0).getTextContent());
        this.textureBottom = doc.getElementsByTagName("textureBottom").item(0).getTextContent();
        this.textureNorth = doc.getElementsByTagName("textureNorth").item(0).getTextContent();
        this.textureEast = doc.getElementsByTagName("textureEast").item(0).getTextContent();
        this.textureWest = doc.getElementsByTagName("textureWest").item(0).getTextContent();

        this._env = env;
        double scale = 2.0; //Epaisseur de nos lettres
        //Si le mot n'est pas vide
        if (lettre.length != 0) {
            //On parcourt toutes les lettres de notre mot
            for (int i = 0; i < lettre.length; i++) {
                //Création de nos coordonnées de façon aléatoire
                double tabY[] = {2.0, 8.0}; //Table permettant de choisir la hauteur de la case de façon aléatoire
                int h = (int) (Math.random() * 2);
                double y = tabY[h];
                double x = (double) (Math.random() * (this.width - 2 * scale)) + scale; //épaisseur_lettre < x < width-epaisseur_lettre
                double z = (double) (Math.random() * (this.depth - 2 * scale)) + scale; //épaisseur_lettre < z < depth-épaisseur_lettre
                for (int j = 0; j < tabPosition.size(); j++) {
                    //On test si notre caisse se chevauche avec une autre
                    while ((x > tabPosition.get(j)._x - (scale + 1)) && (x < tabPosition.get(j)._x + (scale + 1)) && (z > tabPosition.get(j)._z - (scale + 1)) && (z < tabPosition.get(j)._z + (scale + 1))) {
                        //Si notre caisse se chevauche avec une autre on recalcule
                        x = (double) (Math.random() * (this.width - 2 * scale)) + scale;
                        z = (double) (Math.random() * (this.depth - 2 * scale)) + scale;
                    }
                }
                this.tabPosition.add(new Position(x, y, z)); //On ajoute la position de la lettre au tableau des positions   

                this._letters.add(new Letter(lettre[i], x, y, z, scale)); //On ajoute notre lettre au tableau de Lettre
                Letter ombre = new Letter(lettre[i], x, -1.9, z, scale); //On crée notre ombre correspondant à notre lettre
                ombre.setTexture("textures/floor/rock6.png"); //Texture de notre ombre
                this._tabOmbre.add(ombre); //On ajoute l'ombre à notre tableau d'ombre
            }
            //On ajoute nos lettres et nos ombres à l'environnement
            for (int u = 0; u < this._letters.size(); u++) {
                env.addObject(this._letters.get(u)); //La lettre
                env.addObject(this._tabOmbre.get(u)); //L'ombre
            }
        }
        
    }

    /** Méthode qui crée les portes
     * @param {double} z1
     * @param {double} z2
     * @return {void}
     */
    public void creerPorte(double z1, double z2) {
        //Si on est sur la salle du milieu
        double x1 = 40 + 1.9;
        if (this._nom.equals("milieu")) {
            
            this._tabDoor.add(new Door(x1, 5, z1, "coteDroit"));
            this._tabDoor.add(new Door(-1.7, 5, z2, "coteGauche"));
        }
        //Si on est sur la salle de gauche
        if (this._nom.equals("gauche")) {
            this._tabDoor.add(new Door(x1, 5, z2, "coteDroit"));
        }
        //Si on est sur la salle de droite
        if (this._nom.equals("droite")) {
            this._tabDoor.add(new Door(-1.7, 5, z1, "coteGauche"));
        }
    }

    /** Méthode qui affiche les portes
     * @param {Env} env, l'environnement en cours
     * @return {void}
     */
    public void afficherPorte(Env env) {
      	//On parcourt toutes les portes
        for (Door door : this._tabDoor) {
            env.addObject(door); //On ajoute la porte à l'environnement
        }
    }

    /** Méthode qui affiche les lettres et les ombres correspondantes
     * @param {Env} env, l'environnement en cours
     * @return {void}
     */
    public void afficherLettre(Env env) {
      	//On parcourt toutes nos lettres
        for (int i = 0; i < this._letters.size(); i++) {
          	//On ajoute la lettre et son ombre associée à l'environnement
            env.addObject(this._tabOmbre.get(i));
            env.addObject(this._letters.get(i));
        }
    }
}