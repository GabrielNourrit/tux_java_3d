package game.tux;

import fr.ujfGrenoble.miage.tux.XMLUtil;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public final class Profil {

    public Document _doc; //Cree un DOM à partir d'un fichier XML
    private String _nom;
    private String _dateNaissance;
    private String _avatar;
    private ArrayList<Partie> _parties;

    public Profil(String nomFichier) throws XPathExpressionException {
        //Initialisation des attributs
      	this._doc = fromXML(nomFichier); //On initialise le doc avec le fichier XML
        this._parties = new ArrayList<Partie>();

        Element racine = this._doc.getDocumentElement();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();

      	//On récupère les données du profil grâce à XPATH et DOMPARSER
        this._nom = (String) path.evaluate("/profile/name", racine, XPathConstants.STRING);
        this._avatar = (String) path.evaluate("/profile/avatar", racine, XPathConstants.STRING);
        this._dateNaissance = (String) path.evaluate("/profile/birthday", racine, XPathConstants.STRING);
        int taille = this._doc.getElementsByTagName("tux:game").getLength();
        for (int i = 0; i < taille; i++) {
            System.out.println("i : " + i + " " + this._doc.getElementsByTagName("tux:game").item(i).getTextContent());
            this._parties.add(new Partie((Element) this._doc.getElementsByTagName("tux:game").item(i)));
        }
    }

    public Profil(String nom, String dateNaissance) {
        this._nom = nom;
        this._dateNaissance = dateNaissance;
        this._parties = new ArrayList<Partie>();
        this._avatar = "";
    }

    /** Méthode qui ajoute une partie
     * @param {Partie} p, la partie à ajouter
     * @return {void}
     */
    public void ajouterPartie(Partie p) throws XPathExpressionException {
        //On crée un élement game
        Element nouveauGame = this._doc.createElement("tux:game");
        nouveauGame.setAttribute("date", p.getDate()); //On initialise l'attribut date
        nouveauGame.setAttribute("found", p.getTrouve()+"%"); //On initialise l'attrubut found
        //On crée un élément word
        Element nouveauWord = this._doc.createElement("tux:word");
        nouveauWord.setAttribute("level", ""+p.getNiveau()); //On ajoute l'attribut level
        nouveauWord.appendChild(this._doc.createTextNode(p.getMot())); //On ajoute le texte du mot
        //On crée un élément time
        Element nouveauTime = this._doc.createElement("tux:time");
        nouveauTime.appendChild(this._doc.createTextNode(""+p.getTemps())); //On ajoute le texte du temps        
        //On ajoute les éléments xord et time à game
        nouveauGame.appendChild(nouveauWord);
        nouveauGame.appendChild(nouveauTime);
        //On ajoute notre élément à games
        this._doc.getDocumentElement().getElementsByTagName("tux:games").item(0).appendChild(nouveauGame);       
        this._parties.add(p); //On ajoute la partie a notre profile
    }

    /** Méthode qui permet de sauvegarder une partie dans un XML
     * @param {String} filename, le fichier XML où sauvegarder la partie
     */
    public void sauvegarder(String filename) {
        this.toXML(filename);
    }

    /** Méthode qui récupère le dernier niveau
     * @param {void}
     * @return {void}
     */
    public int getDernierNiveau() {
        return this._parties.get(this._parties.size()).getNiveau();
    }

    /** Cree un DOM à partir d'un fichier XML
     * @param {String} nomFichier, le fichier à transformer
     * @return {Document), le XML transformé
     */
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);

        } catch (Exception ex) {
            Logger.getLogger(Profil.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /** Sauvegarde un DOM en XML
     * @param {String} nomFichier, le nom du fichier XML
     * @return {void}
     */
    public void toXML(String nomFichier) {
        try {
            XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// Takes a date in XML format (i.e. ????-??-??) and returns a date    
    /// in profile format: dd/mm/yyyy    
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour        
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois        
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année        
        date += xmlDate.substring(0, xmlDate.indexOf("-"));
        return date;
    }

    /// Takes a date in profile format: dd/mm/yyyy and returns a date   
    /// in XML format (i.e. ????-??-??)   
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année       
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois        
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour       
        date += profileDate.substring(0, profileDate.indexOf("/"));
        return date;
    }

  	/** Méthode qui réalise une transformation XSLT et la renvoie sous forme de {String}
  	 * @param {String} xmlFileName, le fichier XML à transformer
     * @param {String} xslFileName, le fichier XSLT permettant la transformation
     * @return {String}, le fichier XML transformé
     */
    public String getHTML(String xmlFileName, String xslFileName) throws JAXBException, ParserConfigurationException, SAXException, IOException, Exception {
        try {
            // Traitements éventuels sur le cabinet           
            /*            
            * ici sont réalisés les traitements souhaités pour récupérer les informations utiles            
            * ex: classement des patients par ordre alphabétique           
            * ex: classement de la liste des patients d'une infirmière selon la distance            
             */
            // On fabrique un document DOM à partir d'un objet, ici un cabinet            
            Document doc = XMLUtil.DocumentFactory.fromFile(xmlFileName);
            // on charge la feuille de transformation            
            StreamSource xslStreamSource = new StreamSource(xslFileName);
            // Crée un transformateur de Documents DOM à partir d'une fabrique de transformateurs            
            // le transformateur est créé à partir d'une source provenant de la feuille de transformation XSL,            
            // donc il transformera le Document selon cette feuille XSL           
            Transformer tf = TransformerFactory.newInstance().newTransformer(xslStreamSource);
            // On donne un nom au paramètre ; ce nom est celui utilisé dans la feuille de transformation            
            // String id = "idInfirmier";
            // On indique au processeur de document d'appliquer un paramètre auquel on donne sa valeur.            
            //  tf.setParameter(id, infirmierId);
            // On applique la transformation            
            return XMLUtil.DocumentTransform.fromTransformation(tf, doc);
        } catch (TransformerException ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

  
  	/** Méthode qui permet d'écrire dans un fichier
  	 * @param {String} texte, le texte à écrire
     * @param {String} adressedufichier, le fichier où écrire
     * @return {void}
     */
    public void ecrire(String texte, String adressedufichier) throws IOException {
        //BufferedWriter a besoin d un FileWriter, 
        //les 2 vont ensemble, on donne comme argument le nom du fichier
        //true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus 
        FileWriter fw = new FileWriter(adressedufichier);

        // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
        BufferedWriter output = new BufferedWriter(fw);

        //on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
        output.write(texte);
        //on peut utiliser plusieurs fois methode write

        output.flush();
        //ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter

        output.close();
        //et on le ferme
    }
}