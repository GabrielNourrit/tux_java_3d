package management;

import env3d.Env;
import game.Door;
import game.Room;
import game.Tux;
import java.awt.AWTException;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;

public class DevineLeMot {

    private final Env _env; //Environnement
    public Tux _tux; //Tux
    private Room _currentRoom;
    private final ArrayList<Room> _tabRoom; //Room
    private final String _mot; //Le mot joué
    private final ArrayList<Letter> _letters; //Tableau de lettre
    private int _nbLettresRestantes; //Nombre de lettres restantes
    private long _temps; //Temps mis par le joueur
    private final Chronometre _chrono; //Chronometre
    private final char[] _lettre; //Tableau de char contenant les lettres du mot
    private final ArrayList<Camera> _tabCamera;
    private Camera _camera;

    public DevineLeMot(String mot, Env env, int temps) {
        this._env = env; //Initialisation de l'environnement
        this._mot = mot; //Initialisation du mot
        this._letters = new ArrayList<Letter>(); //Initialisation de notre tableau de lettre
        this._nbLettresRestantes = this._mot.length(); //Initialisation du nombre de lettre restantes

        int taille = mot.length();
        int indice;
        ArrayList<Letter> lettre = new ArrayList<Letter>();
        char[] tmp = new char[taille];
        this._lettre = tmp;
        mot.getChars(0, mot.length(), tmp, 0); //Copie des lettres du mot dans notre tableau de char

        for (int i = 0; i < tmp.length; i++) {
            lettre.add(new Letter(tmp[i], 0, 0, 0, 0));
        }

      	//Permet de choisir quelle lettre mettre dans quelle salle
      	//Tableau de lettre pour la salle 1
        int nbLettre = (int) (Math.random() * taille);
        char[] room1 = new char[nbLettre];
        for (int i = 0; i < nbLettre; i++) {
            indice = (int) (Math.random() * (taille - i));
            room1[i] = lettre.get(indice)._letter;
            lettre.remove(indice);
        }
      	//Tableau de lettre pour la salle 2
        taille -= nbLettre;
        nbLettre = (int) (Math.random() * taille);
        char[] room2 = new char[nbLettre];
        for (int i = 0; i < nbLettre; i++) {
            indice = (int) (Math.random() * (taille - i));
            room2[i] = lettre.get(indice)._letter;
            lettre.remove(indice);
        }
      	//Tableau de lettre pour la salle 3
        taille -= nbLettre;
        char[] room3 = new char[taille];
        for (int i = 0; i < taille; i++) {
            indice = (int) (Math.random() * (taille - i));
            room3[i] = lettre.get(indice)._letter;
            lettre.remove(indice);
        }

      	//Initialisation de nos salles
        this._tabRoom = new ArrayList<Room>();
      	//Salle 1
        this._tabRoom.add(new Room(this._env, room1, this._mot, "src/xml/plateau.xml", "milieu"));
        //Ajout des portes dans la salle 1
      	this._tabRoom.get(0).creerPorte((double) (Math.random() * (this._tabRoom.get(0).depth - 2 * 5)) + 5, (double) (Math.random() * (this._tabRoom.get(0).depth - 2 * 5)) + 5);
        //Salle 2
      	this._tabRoom.add(new Room(this._env, room2, this._mot, "src/xml/plateau.xml", "gauche"));
        //Ajout des portes dans la salle 2
      	this._tabRoom.get(1).creerPorte(0, this._tabRoom.get(0)._tabDoor.get(1)._z);
        //Salle 3
      	this._tabRoom.add(new Room(this._env, room3, this._mot, "src/xml/plateau.xml", "droite"));
        //Ajout des portes dans la salle 3
      	this._tabRoom.get(2).creerPorte(this._tabRoom.get(0)._tabDoor.get(0)._z, 0);
        
      	this._env.setRoom(this._tabRoom.get(0));
        this._currentRoom = this._tabRoom.get(0); //Initialisation de l'attribut de la salle actuelle
        this._tux = new Tux(10, 3, 37); //Création de notre Tux
        this._env.addObject(this._tux); //On ajoute Tux à l'environnement
        this._tabRoom.get(0).tabPosition.add(new Position(10.0, 3.0, 37.0));

        this._chrono = new Chronometre(temps); //Création du chronomètre (60sec)

        this._tabCamera = new ArrayList<Camera>(); //Initialisation de notre tableau de lettre
        //Vu plongeante
        this._tabCamera.add(new Camera("plongeante", 20.0, 30.0, 75.0, -27.5));
        //Vue de dessus
        this._tabCamera.add(new Camera("dessus", 20.0, 60.5, 25.0, -90.0));
        //Vue qui suit Tux
        this._tabCamera.add(new Camera("suivre", 10.0, 10.0, 47.5, -25.0));
        //On se met sur la caméra ..... de base
        this._camera = this._tabCamera.get(0);
        //Initialisation de la camera
        this._env.setCameraXYZ(this._camera.getX(), this._camera.getY(), this._camera.getZ());
        this._env.setCameraPitch(this._camera.getPitch());
        //On dit si la camera doit suivre ou non Tux
        this._camera.suivre();
    }

  	/** Méthode de jeu pour notre partie
     * @param {void}
     * @return {void}
     */
    public void jouer() throws InterruptedException, AWTException {
        int i = 0; //Compteur de lettre
        int iTexte = 0;
        String texte = "Mot trouvé : ";
        int longueur = texte.length();
        StringBuilder buffer = new StringBuilder(texte);
        for (int j = 0; j < this._mot.length(); j++) {
            texte += "_ ";
        }
        this._env.setDisplayStr(texte, 320, 50, 2, 1, 1, 1, 1); //On affiche que le joueur a gagné
      	int indiceCaisseTouche; //indice de la caisse qui a été touchée
        boolean jump;
        boolean finished = false; //Boolean pour savoir si le joueur a appuyé sur escape
        //On ajoute Tux à la room
        this._env.addObject(this._tux);
        //On ajoute nos lettres à la room
        this._currentRoom.afficherLettre(_env);
        //this._env.addObject(this._letters.get(0)); //On ajoute la lettre à l'environnement  
        //On affiche les portes
        this._tabRoom.get(0).afficherPorte(this._env);
        // Start chrono
        this._chrono.start(); //On demarre le chrono
        // The main game loop
        do {

            //On test le changement de camera
            this.changeCamera(this._env.getKey());
          
            //On test la touche appuyée par le joueur
            finished = this._tux.checkUserKey(this._env.getKeyDown(), this._tabRoom.get(0), this._env, this._camera);
          
            //On regarde si Tux doit sauter
            jump = this._tux.jumpUp(this._env.getKey(), this._env);
          
            //Si tux rencontre une porte de la salle
            for (Door door : this._currentRoom._tabDoor) {
                if (collisionDoor(door, this._tux));
            }          
            
          	//Tant qu'il reste des lettres
            if (i < this._lettre.length) {
              	//Si tux rencontre la bonne lettre
                if ((indiceCaisseTouche = tuxMeetsLetter(this._lettre[i])) != -1) {
                    this._env.removeObject(this._currentRoom._letters.get(indiceCaisseTouche)); //On supprime la caisse de l'environnement
                    //On fait un echange entre les caisses (si il y a plusieurs lettres pareils dans le mot)
                    buffer = new StringBuilder(texte);
                    if (i==0) buffer.setCharAt(longueur+i, this._lettre[i]);
                    else buffer.setCharAt(longueur+i+iTexte, this._lettre[i]);
                    System.out.println(i);
                    texte = buffer.toString();

                    this._env.setDisplayStr(texte, 320, 50, 2, 1, 1, 1, 1); //On affiche le mot
                    
                    this._env.advanceOneFrame();
                    iTexte ++;
                    Letter tmp = this._currentRoom._letters.get(0);
                    this._currentRoom._letters.set(0, this._currentRoom._letters.get(indiceCaisseTouche));
                    this._currentRoom._letters.set(indiceCaisseTouche, tmp);
                    this._currentRoom._letters.remove(0);

                    //Pareil pour l'ombre de la lettre
                    this._env.removeObject(this._currentRoom._tabOmbre.get(indiceCaisseTouche));
                    tmp = this._currentRoom._tabOmbre.get(0);
                    this._currentRoom._tabOmbre.set(0, this._currentRoom._tabOmbre.get(indiceCaisseTouche));
                    this._currentRoom._tabOmbre.set(indiceCaisseTouche, tmp);
                    this._currentRoom._tabOmbre.remove(0);

                    this._nbLettresRestantes--; //On décrémente le nombre de lettre restantes
                    i++; //On incrémente notre compteur de lettre
                    //Si il reste des lettres
                    if (this._nbLettresRestantes == 0) { //this._tabRoom.get(0).afficherLettre(i, this._letters, this._lettre, this._env); //On va afficher la lettre suivante
                        finished = this._tux.checkUserKey(this._env.getKeyDown(), this._tabRoom.get(0), this._env, this._camera);//On regarde sur quelle touche appuie le joueur
                        this._env.setDisplayStr("Bravo vous avez GAGNE", 250, 350, 3, 1, 1, 1, 1); //On affiche que le joueur a gagné
                        this._chrono.stop(); //On stoppe le chrono
                    }
                }
            }
            //Si Tux a sauté on le fait redescendre
            if (jump) {
                this._tux.jumpDown(_env);
            }

            //this._currentRoom.afficherLettre(this._env);
            this._env.setDisplayStr("Temps restant : " + this._chrono._remain, 20, 20); //Affichage du temps restant
            this._env.setDisplayStr("Temps passé : " + (this._chrono.getSeconds() + 1), 750, 20); //Affichage du temps passé
            this._env.advanceOneFrame();
            
            
        } while (this._chrono.remainsTime() && this._nbLettresRestantes > 0 && !finished); // Conditions de fin : plus de temps, le joueur a gagné, le joueur a quitté
        
        if (!this._chrono.remainsTime()) {
            this._env.setDisplayStr("PERDU le mot était :", 250, 350, 3, 1, 1, 1, 1); //On affiche que le joueur a gagné
            this._env.setDisplayStr(this._mot.toUpperCase(), 350, 300, 4, 0, 0, 0, 1); //On affiche que le joueur a gagné
            this._env.advanceOneFrame(); 
        }
        Thread.sleep(2000);
        //Si le joueur a appuyé sur escape
        if (finished) {
            this._env.exit(); //On quitte le jeu
        }
        this._temps = this._chrono.getSeconds(); //Oon récupère le temps mis

    }

    /** Méthode qui permet de changer de caméra
     * @param {int} currentKey, la touche pressée
     * @return {void}
     */
    public void changeCamera(int currentKey) {
        //Taille du tableau de caméra
        int taille = this._tabCamera.size();
        //Si le joueur veut changer de caméra
        if (currentKey == Keyboard.KEY_C) {
            //On change les caméras grâce à un modulo
            int index = (this._tabCamera.indexOf(this._camera) + 1) % taille;
            //On change la camera
            this._camera = this._tabCamera.get(index);
            //Si la camera est suivre
            if (this._camera.getNom().equals("suivre")) {
                //On place la caméra sur Tux
                this._camera.setX(this._tux.getX());
                this._camera.setZ(this._tux.getZ() + 10.5);
                this._env.setCameraXYZ(this._tux.getX(), this._camera.getY(), this._tux.getZ() + 10.5);
                this._env.setCameraPitch(this._camera.getPitch());
            } //Sinon on place la caméra où il faut
            else {
                this._env.setCameraXYZ(this._camera.getX(), this._camera.getY(), this._camera.getZ());
                this._env.setCameraPitch(this._camera.getPitch());
            }
            //On dit si il faut suivre ou non Tux
            this._camera.suivre();
            this._env.advanceOneFrame();
        }
    }

  	/** Méthode qui permet de savoir la distance entre Tux et une Letter
     * @param {Tux} tux
     * @param {Letter}, la lettre
     * @return {Position}, la distance entre Tux et la Lettre (sous forme de position x,y,z)
     */
    public Position distanceLetter(Tux tux, Letter letter) {
        return new Position(tux.getX() - letter.getX(), tux.getZ() - letter.getZ(), tux.getY() - letter.getY()); //On retourne la distance (x,z) de Tux à la Lettre
    }

    
  	/** Méthode pour savoir si il y a une collision entre Tux et une Letter
     * @param {Tux} tux
     * @param {Letter}, la lettre
     * @return {boolean}, true si il y a collision, false sinon
     */
    public boolean collisionLetter(Tux tux, Letter letter) {
        return (distanceLetter(tux, letter)._x >= -tux._scale) && (distanceLetter(tux, letter)._x <= tux._scale) && (distanceLetter(tux, letter)._y >= -tux._scale) && (distanceLetter(tux, letter)._y <= tux._scale) && (distanceLetter(tux, letter)._z >= -tux._scale) && (distanceLetter(tux, letter)._z <= tux._scale);
    }

    
  	/** Méthode qui permet de savoir si Tux touche une Letter donnée
     * @param {char} lettre, la lettre à tester
     * @return {int}, l'indice de la caisse touché, -1 si aucune
     */
    public int tuxMeetsLetter(char lettre) {
        if (this._currentRoom._letters.isEmpty()) {
            return -1;
        } else {
            //On parcourt les lettres de la room
            for (int i = 0; i < this._currentRoom._letters.size(); i++) {
                //Si il y a une collision et que la lettre est la même que celle à ramasser
                if ((collisionLetter(this._tux, this._currentRoom._letters.get(i))) && (this._currentRoom._letters.get(i)._letter == lettre)) {
                    //On retourne l'indice de la lettre à supprimer
                    return i;
                }
            }
        }
        return -1;
    }

    
  	/** Méthode qui permet de savoir la distance entre Tux et une Door
     * @param {Tux} tux
     * @param {Door}, la porte
     * @return {Position}, la distance entre Tux et la Lettre (sous forme de position x,y,z)
     */
    public Position distanceDoor(Door door, Tux tux) {
        //System.out.println(tux.getZ() - door.getZ());
        return new Position(tux.getX() - door.getX(), tux.getY() - door.getY(), tux.getZ() - door.getZ()); //On retourne la distance (x,y,z) de Tux à la Lettre
    }

    
  	/** Méthode pour savoir si il y a une collision entre Tux et une Door
     * @param {Tux} tux
     * @param {Door}, la porte
     * @return {boolean}, true si il y a collision, false sinon
     */
    public boolean collisionDoor(Door door, Tux tux) {
        //Si tux est dans le bon sens pour passer la porte et qu'il touche la porte et que la porte est à droite
        if ((door._orientation.equals("coteDroit")) && (tux.getRotateY() == 90) && (distanceDoor(door, tux)._x >= -tux._scale - 1.9) && (distanceDoor(door, tux)._x <= tux._scale) && (distanceDoor(door, tux)._z >= -tux._scale) && (distanceDoor(door, tux)._z <= tux._scale)) {
            //Si on est dans la salle du milieu
            if (this._currentRoom._nom.equals("milieu")) {
                //La nouvelle room est celle de droite
                this._env.setRoom(this.getRoom("droite"));
                this._currentRoom = this.getRoom("droite");
                //On place Tux
                this._tux.setX(0);
                this._tux.setY(tux.getY());
                this._tux.setZ(door._z);
                this._env.addObject(this._tux);
                //On affiche les lettres
                this._currentRoom.afficherLettre(this._env);
                //Si la caméra est suivre on la met sur Tux
                if (this._camera.getNom().equals("suivre")) {
                    //On place la caméra sur Tux
                    this._camera.setX(this._tux.getX());
                    this._camera.setZ(this._tux.getZ() + 10.5);
                    this._env.setCameraXYZ(this._tux.getX(), this._camera.getY(), this._tux.getZ() + 10.5);
                    this._env.setCameraPitch(this._camera.getPitch());
                }
            } //Si la room est celle de gauche
            else if (this._currentRoom._nom.equals("gauche")) {
                //La nouvelle room est celle du milieu
                this._env.setRoom(this.getRoom("milieu"));
                this._currentRoom = this.getRoom("milieu");
                this._tux.setX(0);
                this._tux.setY(tux.getY());
                this._tux.setZ(door._z);
                this._env.addObject(this._tux);
                this._currentRoom.afficherLettre(this._env);
                if (this._camera.getNom().equals("suivre")) {
                    //On place la caméra sur Tux
                    this._camera.setX(this._tux.getX());
                    this._camera.setZ(this._tux.getZ() + 10.5);
                    this._env.setCameraXYZ(this._tux.getX(), this._camera.getY(), this._tux.getZ() + 10.5);
                    this._env.setCameraPitch(this._camera.getPitch());
                }
            } else {
            }
            this._currentRoom.afficherPorte(this._env);
        }

        if ((door._orientation.equals("coteGauche")) && (tux.getRotateY() == -90) && (distanceDoor(door, tux)._x <= tux._scale + 1.9) && (distanceDoor(door, tux)._x >= tux._scale) && (distanceDoor(door, tux)._z >= -tux._scale) && (distanceDoor(door, tux)._z <= tux._scale)) {
            if (this._currentRoom._nom.equals("milieu")) {
                this._env.setRoom(this.getRoom("gauche"));
                this._currentRoom = this.getRoom("gauche");
                this._tux.setX(40);
                this._tux.setY(tux.getY());
                this._tux.setZ(door._z);
                this._env.addObject(this._tux);
                this._currentRoom.afficherLettre(this._env);
                if (this._camera.getNom().equals("suivre")) {
                    //On place la caméra sur Tux
                    this._camera.setX(this._tux.getX());
                    this._camera.setZ(this._tux.getZ() + 10.5);
                    this._env.setCameraXYZ(this._tux.getX(), this._camera.getY(), this._tux.getZ() + 10.5);
                    this._env.setCameraPitch(this._camera.getPitch());
                }
            } else if (this._currentRoom._nom.equals("droite")) {
                this._env.setRoom(this.getRoom("milieu"));
                this._currentRoom = this.getRoom("milieu");
                this._tux.setX(40);
                this._tux.setY(tux.getY());
                this._tux.setZ(door._z);
                this._env.addObject(this._tux);
                this._currentRoom.afficherLettre(this._env);
                if (this._camera.getNom().equals("suivre")) {
                    //On place la caméra sur Tux
                    this._camera.setX(this._tux.getX());
                    this._camera.setZ(this._tux.getZ() + 10.5);
                    this._env.setCameraXYZ(this._tux.getX(), this._camera.getY(), this._tux.getZ() + 10.5);
                    this._env.setCameraPitch(this._camera.getPitch());
                }
            } else;
            this._currentRoom.afficherPorte(this._env);
        }
        return true;
    }

    /** Méthode qui retourne la room dont le nom est passé en paramètre
     * @param {String} nom, nom de la room
     * @return {Room}, la Room recherchée
     */
    public Room getRoom(String nom) {
        int i = 0;
        while ((i < this._tabRoom.size()) && (!this._tabRoom.get(i)._nom.equals(nom))) {
            i++;
        }
        if (this._tabRoom.get(i)._nom.equals(nom)) {
            return this._tabRoom.get(i);
        }
        return null;
    }

    
  	/** Méthode qui retourne la door dont le nom est passé en paramètrer
     * @param {String} nom, le nom de la porte
     * @param {Room}, la salle actuelle
     * @return {Door} la porte recherchée
     */
    public Door getDoor(String nom, Room room) {
        int i = 0;
        while ((i < room._tabDoor.size()) && (!room._tabDoor.get(i)._orientation.equals(nom))) {
            i++;
        }
        if (room._tabDoor.get(i)._orientation.equals(nom)) {
            return room._tabDoor.get(i);
        }
        return null;
    }

    /** Méthode qui permet de bouger la caméra
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
            if (this._env.getCameraPitch() + step <= 16.5) {
                this._env.setCameraPitch(this._env.getCameraPitch() + step);
            }
            /*Si on est en dehors de la pièce on fait rien*/
        }
        //Si la touche S est appuyée
        if (currentKey == Keyboard.KEY_S) {
            /*Pour bouger la caméra en haut on déplace cameraPitch*/
 			/*La nouvelle positon camera = ancienne position camera + le pas
              Si la nouvelle position est dans la pièce on bouge dans la camera*/
            if (this._env.getCameraPitch() - step >= -27.5) {
                this._env.setCameraPitch(this._env.getCameraPitch() - step);
            }
            /*Si on est en dehors de la pièce on fait rien*/
        }
    }

    public double getTemps() {
        return this._temps;
    }

    public int getNbLettresRestantes() {
        return this._nbLettresRestantes;
    }
}