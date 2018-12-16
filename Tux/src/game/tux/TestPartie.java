package game.tux;

import fr.ujfGrenoble.miage.tux.XMLUtil;
import fr.ujfGrenoble.miage.tux.XMLUtil.DocumentFactory;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class TestPartie {
    
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File fileXML = new File("src/xml/profile.xml");
        Document xml = builder.parse(fileXML);
        
        Element racine = xml.getDocumentElement();
        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();
        
        String expression = "/profile/games/game[1]";
        Node str = (Node) path.evaluate(expression, racine, XPathConstants.NODE);
        Element str1 = (Element) str;
        Partie partie = new Partie(str1);
        System.out.println(partie.toString()); 
        Document doc = DocumentFactory.fromFile("src/xml/profile1.xml");
        Profil profil = new Profil("src/xml/profile.xml");
        //Document doc = profil.fromXML("src/xml/profile.xml");
        profil.toXML("src/xml/profile2.xml");
        System.out.println(XMLUtil.DocumentTransform.fromXSLTransformation("src/xml/profile.xsl",doc));
    }
    
   
}
