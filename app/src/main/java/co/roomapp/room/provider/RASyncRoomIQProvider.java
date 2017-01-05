package co.roomapp.room.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import co.roomapp.room.iq.RASyncRoomIQ;
import co.roomapp.room.utils.RAUtils;
import co.roomapp.room.utils.XMPPIQ;

/**
 * Created by drottemberg on 1/16/15.
 */
public class RASyncRoomIQProvider implements IQProvider {


    @Override
    public IQ parseIQ(XmlPullParser parser) throws Exception {


        String xml = RAUtils.parserToXML(parser);


        RASyncRoomIQ raSyncRoomIQ = new RASyncRoomIQ(xml);


        String all = parser.getAttributeValue("","all");
        String user_request = parser.getAttributeValue("","user_request");


        raSyncRoomIQ.setNamespace(parser.getNamespace());
        raSyncRoomIQ.setAll(Boolean.valueOf(all));
        raSyncRoomIQ.setUser_request(Boolean.valueOf(user_request));



        return  raSyncRoomIQ;
    }




}
