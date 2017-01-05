package co.roomapp.room.utils;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by drottemberg on 1/13/15.
 */
public class XMPPIQ extends IQ{

    private org.xmpp.packet.IQ iq;

    public XMPPIQ(){

        this.iq = new org.xmpp.packet.IQ();

        if(iq.getType() == org.xmpp.packet.IQ.Type.get){
            this.setType(Type.GET);
        }

        if(iq.getType() == org.xmpp.packet.IQ.Type.set){
            this.setType(Type.SET);
        }

        if(iq.getType() == org.xmpp.packet.IQ.Type.error){
            this.setType(Type.ERROR);
        }

        if(iq.getType() == org.xmpp.packet.IQ.Type.result){
            this.setType(Type.RESULT);
        }
    }

    public XMPPIQ(org.xmpp.packet.IQ iq){
        this.iq = iq;
        this.setPacketID(iq.getID());

        if(iq.getType() == org.xmpp.packet.IQ.Type.get){
            this.setType(Type.GET);
        }

        if(iq.getType() == org.xmpp.packet.IQ.Type.set){
            this.setType(Type.SET);
        }

        if(iq.getType() == org.xmpp.packet.IQ.Type.error){
            this.setType(Type.ERROR);
        }

        if(iq.getType() == org.xmpp.packet.IQ.Type.result){
            this.setType(Type.RESULT);
        }


    }

    @Override
    public CharSequence getChildElementXML() {
        return iq.getChildElement().asXML();
    }






}
