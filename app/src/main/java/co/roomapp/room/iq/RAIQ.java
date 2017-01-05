package co.roomapp.room.iq;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jivesoftware.smack.packet.IQ;

import java.io.StringReader;
import java.util.List;

/**
 * Created by drottemberg on 1/19/15.
 */
public class RAIQ extends IQ {

    protected static final DocumentFactory docFactory = DocumentFactory.getInstance();
    protected Element childElement;
    protected String namespace = "";



    public RAIQ(String xml){
        super();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new StringReader(xml.replace("&","&amp;")));
            this.childElement = document.getRootElement();
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public Element getChildElement()
   {
     return this.childElement;
   }


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public CharSequence getChildElementXML() {

        return this.getChildElement().asXML();
    }
}
