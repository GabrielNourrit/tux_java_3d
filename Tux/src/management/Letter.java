package management;

import env3d.EnvObject;

public class Letter extends EnvObject {
    public final char _letter; //Notre lettre
    public final double _scale; //Epaisseur de la boite
    public final double _x; //Position x
    public final double _z; //Position z
    public double _y;
        public Letter(char l, double x, double y, double z, double scale){
            this._letter = l;
            this._x = x;
            this._y = y;
            this._z = z;
            this.setX(x);
            this.setY(y);
            this.setZ(z);
            this._scale = scale;
            this.setScale(scale);
            this.setTexture("letter/" + _letter + ".png");
            this.setModel("letter/cube.obj");
        }
}
