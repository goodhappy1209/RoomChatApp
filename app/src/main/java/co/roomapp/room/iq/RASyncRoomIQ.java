package co.roomapp.room.iq;

import org.dom4j.Element;
import org.jivesoftware.smack.packet.IQ;

import java.util.ArrayList;
import java.util.Iterator;

import co.roomapp.room.model.RARoom;

/**
 * Created by drottemberg on 1/17/15.
 */
public class RASyncRoomIQ extends RAIQ {

    boolean all;
    boolean user_request;

    ArrayList<RARoom> rooms;

    public RASyncRoomIQ(String xml) {
        super(xml);
        all = Boolean.valueOf(this.childElement.attributeValue("all","1"));
        user_request = Boolean.valueOf(this.childElement.attributeValue("user_request","0"));

        Iterator i;
        for (i = this.childElement.elementIterator("item"); i.hasNext(); ) {

            Element item = (Element)i.next();
            RARoom.elementToRoom(item);


        }
    }




    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isUser_request() {
        return user_request;
    }


    public void setUser_request(boolean user_request) {
        this.user_request = user_request;
    }

    public ArrayList<RARoom> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<RARoom> rooms) {
        this.rooms = rooms;
    }


}
