package game;

import env3d.EnvObject;

/*Classe représentant les portes*/
public class Door extends EnvObject {
    public double _x; //Position x de la porte
    public double _y; //Position y de la porte
    public double _z; //Position z de la porte
    public double _scale; //Echelle de la porte
    public String _orientation; //Orientation de la porte
    public Door(double x, double y, double z, String orientation) {
        this._x = x;
        this._y = y;
        this._z = z;
        this._scale = 5.0;
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setScale(this._scale);
      	//On initialise la porte en fonction de son orientation
        if (orientation.equals("coteDroit")) {
            this.setRotateY(90);
            this._orientation = "coteDroit";
        }
        if (orientation.equals("coteGauche")) {
            this.setRotateY(90);
            this._orientation = "coteGauche";
        }
            
        this.setTexture("textures/door.png");
        this.setModel("models/boxDude/box-dude.obj");
    }
}