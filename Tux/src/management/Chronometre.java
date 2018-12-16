package management;

public class Chronometre {
    private long _begin; //Début du chrono
    private long _end; //Fin du chrono
    public long _current; //Temps actuel
    public long _limite; //Limite du chrono
    public int _remain; //Temps restant

    public Chronometre(int limite) {
        //intialisation
        this._limite = limite;
    }
    
    public void start(){
        this._begin = System.currentTimeMillis(); //On récupère l'heure de début du chrono
        this._limite = this._limite + this._begin; //On initialise la limite du chrono
    }
 
    public void stop(){
        this._end = System.currentTimeMillis(); //On initialise la fin du chrono
    }
 
    public long getTime() {
        return this._end-this._begin; //On récupère le temps mis par le joueur
    }
 
    public long getMilliseconds() {
        return this._current-this._begin;
    }
 
    public int getSeconds() {
        return (int) ((this._current-this._begin) / 1000.0);
    }
 
    public double getMinutes() {
        return (this._current-this._begin) / 60000.0;
    }
 
    public double getHours() {
        return (this._current-this._begin) / 3600000.0;
    }
    
   /** Méthode qui permet de savoir si il reste du temps ou non
    * @param {void}
    * @return {boolean}, true si il reste du temps, false sinon
    */
    public boolean remainsTime() {
        this._current = System.currentTimeMillis(); //Heure actuelle
        this._remain = (int) ((this._limite-this._current)/1000.0); //Le temps restant = limite - temps actuel
        return this._remain > 0; //On retourne si il reste du temps ou non
    }
     
}