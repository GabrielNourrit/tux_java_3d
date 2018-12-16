package management;

public class Camera {
    private final String _nom;
    private double _x;
    private final double _y;
    private double _z;
    private final double _pitch;
    private boolean _suivre;
    
    public Camera(String nom, double x, double y, double z, double pitch) {
        this._nom = nom;
        this._x = x;
        this._y = y;
        this._z = z;
        this._pitch = pitch;
        this._suivre = false;
    }
    
    public String getNom() {
        return this._nom;
    }
    public double getX() {
        return this._x;
    }
    public double getY() {
        return this._y;
    }
    public double getZ() {
        return this._z;
    }
    public double getPitch() {
        return this._pitch;
    }
    public boolean getSuivre() {
        return this._suivre;
    }
    
    public double setX(double x) {
        return this._x = x;
    }
    public double setZ(double z) {
        return this._z = z;
    }
    
    public void suivre() {
        this._suivre = this._nom.equals("suivre");
    }
}