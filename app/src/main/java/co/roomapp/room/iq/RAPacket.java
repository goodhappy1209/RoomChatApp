package co.roomapp.room.iq;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.jivesoftware.smack.packet.Packet;
import org.xmpp.packet.JID;

import javax.xml.parsers.DocumentBuilder;

/**
 * Created by drottemberg on 1/19/15.
 */
public class RAPacket extends Packet{


    protected static final DocumentFactory docFactory = DocumentFactory.getInstance();
    protected Element element;
    protected JID toJID;
    protected JID fromJID;

    public RAPacket(String xml){
        Document document = docFactory.createDocument(xml);
        this.element = document.getRootElement();
    }


    @Override
    public CharSequence toXML() {
        return element.asXML();
    }
}
