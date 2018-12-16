package game.tux;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Partie {
    private final String _date;
    private final String _mot;
    private final int _niveau;
    private double _trouve;
    private double _temps;
    
    public Partie(String date, String mot, int niveau) {
        this._date = date;
        this._mot = mot;
        this._niveau = niveau;
    }
    
    public Partie(Element partieElt) throws XPathExpressionException {
        /*System.out.println("mot : " + partieElt.getElementsByTagName("tux:word").item(0).getTextContent());
        System.out.println("temps : " + partieElt.getElementsByTagName("tux:time").item(0).getTextContent());
        System.out.println("date : " + partieElt.getAttribute("date"));
        System.out.println("Niveau : " + partieElt.getElementsByTagName("tux:word").item(0).getAttributes().item(0).getTextContent());
        System.out.println("Trouve : " + partieElt.getAttribute("found"));*/
        
        //On récupère les éléments grace à DOMParser
        this._date = "date : " + partieElt.getAttribute("date");
        this._mot = partieElt.getElementsByTagName("tux:word").item(0).getTextContent();
        this._niveau = Integer.parseInt(partieElt.getElementsByTagName("tux:word").item(0).getAttributes().item(0).getTextContent());
        this._temps = Double.parseDouble(partieElt.getElementsByTagName("tux:time").item(0).getTextContent());  
        if (partieElt.getAttribute("found").length() == 2)
            this._trouve = Double.parseDouble(partieElt.getAttribute("found").substring(0, 1));
        if (partieElt.getAttribute("found").length() == 3)
            this._trouve = Double.parseDouble(partieElt.getAttribute("found").substring(0, 2));
    }
    
    /** Méthode qui retourne une partie sous forme d'élément DOM
     * @param {Document} doc, le document où l'on veut récupérer la partie
     * @return {Element}, la partie sous forme d'élément
     */
    public Element getPartie(Document doc) throws XPathExpressionException {
        Element root = doc.getDocumentElement(); //On récupère la racine du document
        //Création de xpath
        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();
        
        String expression = "/profile/games/game[1]"; //Notre expression xpath
        Node noeud = (Node) path.evaluate(expression, root, XPathConstants.NODE); //On récupère notre noeud 
        return (Element) noeud;
    }
    
    /** Méthode qui initialise l'élément trouvé
     * @param {int} nbLettresRestantes, le nombre de lettre restantes
     * @param {String} mot, le mot de la partie
     * @return {void}
     */
    public void setTrouve(int nbLettresRestantes, String mot) {
        int nbLettres = mot.length(); //Nombre de lettres dans notre mot
      	int nbLettresTrouves = nbLettres - nbLettresRestantes;
        if (nbLettres == 0)
            System.out.println("Erreur mot");
        else {
            //Si l'utilisateur a trouvé toutes les lettres found = 100%
            if (nbLettresRestantes == 0)
                 this._trouve = 100;
            else {
                //Sinon on calcule le %
                this._trouve = (nbLettresTrouves*100/nbLettres);
            }
        }
    }
    
    public void setTemps(double temps) {
        this._temps = temps;
    }    
    public int getNiveau() {
        return this._niveau;
    }
    public String getDate() {
        return this._date;
    }
    public String getMot() {
        return this._mot;
    }
    public double getTrouve() {
        return this._trouve;
    }
    public double getTemps() {
        return this._temps;
    }
    
    @Override
    public String toString() {
       return "Mot : " + this._mot + " Niveau : " + this._niveau + " Temps : " + this._temps + " Trouve : " + this._trouve + " Date : " + this._date;
    } 
}