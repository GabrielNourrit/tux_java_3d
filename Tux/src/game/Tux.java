package game;

import env3d.Env;
import env3d.EnvObject;
import management.Camera;
import org.lwjgl.input.Keyboard;

/*Classe définissant le personnage Tux*/
public class Tux extends EnvObject{

    public double _scale = 3.0; //Attribut définissant l'épaisseur de Tux
    public Tux(double x, double y, double z) {
        //On initialise les coordonnées (x,y,z) de Tux
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        //On initialise l'épaisseur de Tux
        this.setScale(this._scale);
        //On initialise les textures et le modele de Tux
        /*this.setTexture("models/tux/tux.png");
        this.setModel("models/tux/tux.obj");*/
        this.setTexture("models/tux/tux_happy.png");
        this.setModel("models/tux/tux.obj");
        this.setRotateY(90);
    }
    
    /** Méthode qui fait sauter tux
     * @param {int} currentKey, la touche pressée
     * @param {Env} env, l'environnement en cours
     * @return {boolean}, true si Tux a sauté, false sinon
     */
    public boolean jumpUp(int currentKey, Env env) {
        double step = 0.5; //Vitesse de montée
        double hauteur = 13; //hauteur du saut
        boolean jump = false;
        //Si le joueur a appuyé sur espace
        if (currentKey == Keyboard.KEY_SPACE) {
            //On fait monter Tux
            for (int i = 0; i < hauteur; i++) {
                this.setY(this.getY() + step);
                env.advanceOneFrame(); 
            }
            jump = true; //On dit que Tux a sauté
        }
        return jump;
    }
    
    /** Méthode qui permet de faire descendre Tux après son saut
     * @param {Env} env, l'environnement en cours
     * @return {void}
     */
    public void jumpDown(Env env) {
        double step = 0.5;
        double hauteur = 13;
        for (int i = 0; i < hauteur; i++) {
            this.setY(this.getY() - step);
            env.advanceOneFrame();
        }
    }
    
    /** Méthode qui regarde quelle touche a été appuyée et agit en conséquence
     * @param {int} currentKey, la touche pressée
     * @param {Room} room, la salle en cours
     * @param {Env} env, l'environnement en cours
     * @param {Camera} camera, la camera en cours
     * @return {boolean}, true si le joueur a appuyé sur echap false sinon
     */
    public boolean checkUserKey(int currentKey, Room room, Env env, Camera camera) {     
        boolean finished = false; 
        double step = 0.4; // Variable définissant la taille du pas
        /*Si la flèche du haut est appuyée*/
        if (currentKey == Keyboard.KEY_UP) {
            /*On fait retourner Tux dans la bonne direction*/
            this.setRotateY(180);
            /*Pour bouger en bas on déplace Tux sur l'axe Z*/
            /*La nouvelle positon axe Z = ancienne position axe Z + le pas
              Si la nouvelle position est dans le plateau on bouge*/
            if ((this.getZ() + step) >= (0 + (this._scale/2 + 1))){
                this.setZ(this.getZ() - step);
                if (camera.getSuivre()) {
                    camera.setZ(camera.getZ()-step);
                    env.setCameraXYZ(camera.getX(), camera.getY(), camera.getZ());
                    env.setCameraPitch(camera.getPitch());
                    //env.setCameraYaw(0);
                }
            }
            /*Si on est en dehors du plateau on fait rien*/
        }
        /*Si la flèche du bas est appuyée*/
        if (currentKey == Keyboard.KEY_DOWN) {
            /*On fait retourner Tux dans la bonne direction*/
            this.setRotateY(0);
            /*Pour bouger en bas on déplace Tux sur l'axe Z*/
            /*La nouvelle positon axe Z = ancienne position axe Z - le pas
              Si la nouvelle position est dans le plateau on bouge*/
            if ((this.getZ() - step) <= (room.depth - (this._scale/2))) {
                this.setZ(this.getZ() + step);
                if (camera.getSuivre()) {
                    camera.setZ(camera.getZ()+step);
                    env.setCameraXYZ(camera.getX(), camera.getY(), camera.getZ());
                    env.setCameraPitch(camera.getPitch());
                    //env.setCameraYaw(180);
                }
            }
                
            /*Si on est en dehors du plateau on fait rien*/
        }
        /*Si la flèche de droite est appuyée*/
        if (currentKey == Keyboard.KEY_RIGHT) {
            this.setRotateY(90);
            /*Pour bouger à droite on déplace Tux sur l'axe X*/
            /*La nouvelle positon axe X = ancienne position axe X + le pas
              Si la nouvelle position est dans le plateau on bouge*/
            if ((this.getX() + step) <= (room.width - (this._scale/2 + 1))) {
                this.setX(this.getX() + step);
                if (camera.getSuivre()) {
                    camera.setX(camera.getX()+step);
                    env.setCameraXYZ(camera.getX(), camera.getY(), camera.getZ());
                    env.setCameraPitch(camera.getPitch());
                    //env.setCameraYaw(-90);
                }
            }
                
            /*Si on est en dehors du plateau on fait rien*/
        }
        /*Si la flèche de gauche est appuyée*/
        if (currentKey == Keyboard.KEY_LEFT) {
            this.setRotateY(-90);
            /*Pour bouger à droite on déplace Tux sur l'axe X*/
            /*La nouvelle positon axe X = ancienne position axe X - le pas
              Si la nouvelle position est dans le plateau on bouge*/
            if ((this.getX() - step) >= (0 + (this._scale/2 + 1))) {
                //System.out.println(this._scale/2);
                this.setX(this.getX() - step);
                if (camera.getSuivre()) {
                    camera.setX(camera.getX()-step);
                    env.setCameraXYZ(camera.getX(), camera.getY(), camera.getZ());
                    env.setCameraPitch(camera.getPitch());
                    //env.setCameraYaw(90);
                }
            }    
            /*Si on est en dehors du plateau on fait rien*/
        }

        step = 1;
        //Si la touche Z est appuyée
        if (currentKey == Keyboard.KEY_Z) {
            /*Pour bouger la caméra en haut on déplace cameraPitch*/
            /*La nouvelle positon camera = ancienne position camera + le pas
              Si la nouvelle position est dans la pièce on bouge dans la camera*/
            if (env.getCameraPitch() + 1 <= 16.5)
                env.setCameraPitch(env.getCameraPitch()+step);
            /*Si on est en dehors de la pièce on fait rien*/
        }
        //Si la touche S est appuyée
        if (currentKey == Keyboard.KEY_S) {
            /*Pour bouger la caméra en haut on déplace cameraPitch*/
            /*La nouvelle positon camera = ancienne position camera + le pas
              Si la nouvelle position est dans la pièce on bouge dans la camera*/
            if (env.getCameraPitch() - 1 >= -27.5)
               env.setCameraPitch(env.getCameraPitch()-step);
            /*Si on est en dehors de la pièce on fait rien*/
        }
      	//Bouger la camera
        if (currentKey == Keyboard.KEY_Q) {
            if (env.getCameraPitch() >= -26.5)
                env.setCameraYaw(env.getCameraYaw()+step);
        }
        if (currentKey == Keyboard.KEY_D) {
            if (env.getCameraPitch() >= -26.5)
                env.setCameraYaw(env.getCameraYaw()-step);
        }
        if (env.getKey() == Keyboard.KEY_ESCAPE) {
                //on dit que c'est la fin du jeu
                finished = true;
        }
        
        return finished;
    }
}