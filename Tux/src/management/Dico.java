package management;

import fr.ujfGrenoble.miage.tux.XMLUtil;
import game.tux.Profil;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Dico {

    private final ArrayList<String> _listLevel1; //dicitionnaire de mot de niveau 1
    private final ArrayList<String> _listLevel2; //dicitionnaire de mot de niveau 2
    private final ArrayList<String> _listLevel3; //dicitionnaire de mot de niveau 3
    private final ArrayList<String> _listLevel4; //dicitionnaire de mot de niveau 4
    private final ArrayList<String> _listLevel5; //dicitionnaire de mot de niveau 5
    private final String _pathToDicoFile;

    public Dico(String pathToDicoFile) {
        this._pathToDicoFile = pathToDicoFile;
        Document doc = fromXML(this._pathToDicoFile);
        //Initialisation de nos dicos
        this._listLevel1 = new ArrayList();
        this._listLevel2 = new ArrayList();
        this._listLevel3 = new ArrayList();
        this._listLevel4 = new ArrayList();
        this._listLevel5 = new ArrayList();

        NodeList noeud =  doc.getElementsByTagName("tux:mot");
        int taille = noeud.getLength();
        for (int i = 0; i < taille; i++) {
            switch(Integer.parseInt(noeud.item(i).getAttributes().item(0).getTextContent())){
                case 1:
                    this._listLevel1.add(noeud.item(i).getTextContent()); //on ajoute le mot au dico de niveau 1
                    break;
                case 2:
                    this._listLevel2.add(noeud.item(i).getTextContent()); //on ajoute le mot au dico de niveau 2
                    break;
                case 3:
                    this._listLevel3.add(noeud.item(i).getTextContent()); //on ajoute le mot au dico de niveau 3
                    break;
                case 4:
                    this._listLevel4.add(noeud.item(i).getTextContent()); //on ajoute le mot au dico de niveau 4
                    break;
                case 5:
                    this._listLevel5.add(noeud.item(i).getTextContent()); //on ajoute le mot au dico de niveau 5
                    break;
            }
        }
    }

    /** Méthode permettant de récupérer un mot au hasard dans le dictionnaire
     * @param {int} level, le niveau que l'on veut pour notre mot
     * @return {String}, le mot récupéré
     */
    public String getWordFromListLevel(int level) {
        int nb;
        String mot = "";
        switch (level) {
            case 1:
                nb = (int) (Math.random() * this._listLevel1.size()); //On choisi un indice aléatoire entre 0 et la taille du dico
                mot = this._listLevel1.get(nb); //On récupère le mot à notre indice
                break;
            case 2:
                nb = (int) (Math.random() * this._listLevel2.size()); //On choisi un indice aléatoire entre 0 et la taille du dico
                mot = this._listLevel2.get(nb); //On récupère le mot à notre indice
                break;
            case 3:
                nb = (int) (Math.random() * this._listLevel3.size()); //On choisi un indice aléatoire entre 0 et la taille du dico
                mot = this._listLevel3.get(nb); //On récupère le mot à notre indice
                break;
            case 4:
                nb = (int) (Math.random() * this._listLevel4.size()); //On choisi un indice aléatoire entre 0 et la taille du dico
                mot = this._listLevel4.get(nb); //On récupère le mot à notre indice
                break;
            case 5:
                nb = (int) (Math.random() * this._listLevel5.size()); //On choisi un indice aléatoire entre 0 et la taille du dico
                mot = this._listLevel5.get(nb); //On récupère le mot à notre indice
                break;
        }
        return mot;
    }

    /** Méthode permettant d'ajouter un mot au dictionnaire
     * @param {int} level, le niveau du mot
     * @param {String} word, le mot à ajouter
     * @return {boolean}, true si le mot a été ajouté, false sinon
     */
    public boolean addWordToDico(int level, String word) {
        boolean ajout = false; //boolean si on a ajouté ou pas le mot
        switch (level) {
            case 1:
                this._listLevel1.add(word); //on ajoute le mot au dico de niveau 1
                ajout = true; //On dit que le mot a été ajouté
                break;
            case 2:
                this._listLevel2.add(word); //on ajoute le mot au dico de niveau 2
                ajout = true; //On dit que le mot a été ajouté
                break;
            case 3:
                this._listLevel3.add(word); //on ajoute le mot au dico de niveau 3
                ajout = true; //On dit que le mot a été ajouté
                break;
            case 4:
                this._listLevel4.add(word); //on ajoute le mot au dico de niveau 4
                ajout = true; //On dit que le mot a été ajouté
                break;
            case 5:
                this._listLevel5.add(word); //on ajoute le mot au dico de niveau 5
                ajout = true; //On dit que le mot a été ajouté
                break;
        }
        return ajout;
    }

    public static Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getPathToDicoFile() {
        return this._pathToDicoFile;
    }
}
