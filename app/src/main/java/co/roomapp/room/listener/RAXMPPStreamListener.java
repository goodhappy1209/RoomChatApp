package co.roomapp.room.listener;

import android.provider.Settings;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.io.IOException;
import java.util.Date;

import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.iq.RAIQ;
import co.roomapp.room.iq.RASyncRoomIQ;
import co.roomapp.room.manager.RAXMPPManager;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAObservingService;
import co.roomapp.room.utils.RAUtils;

/**
 * Created by drottemberg on 9/16/14.
 */
public class RAXMPPStreamListener implements PacketListener,MessageListener,ConnectionListener,ConnectionCreationListener{
    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {

       if(packet instanceof IQ){


           RAIQ packetIQ = (RAIQ) packet;

           if(packetIQ.getType() == IQ.Type.RESULT){

                String xmlns = packetIQ.getNamespace();

               if("room:iq:syncroom".equals(xmlns)){
                   RASyncRoomIQ result = (RASyncRoomIQ) packetIQ;

                   RAUtils.setObjectForKey(RAConstant.kRAHasUserRequest,Boolean.toString(result.isUser_request()));
                   RAUtils.setObjectForKey(RAConstant.kLastFullSync,Long.toString(new Date().getTime()));

                   RAObservingService.getInstance().postNotification(RAConstant.kReloadListRoomNotification,null);
                   RAObservingService.getInstance().postNotification(RAConstant.kReloadCommentsObserver,null);
                   RAObservingService.getInstance().postNotification(RAConstant.kReloadTimelineObserver,null);
                   RAObservingService.getInstance().postNotification(RAConstant.kReloadRoomObserver,null);
                   RAObservingService.getInstance().postNotification(RAConstant.kRAUpdateTabChatBage,null);
                   RAObservingService.getInstance().postNotification(RAConstant.kJoinSocialRoomGoOnlineObserver,null);


               }


           }
       }


        return;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        return;
    }

    @Override
    public void connectionCreated(XMPPConnection xmppConnection) {

    }

    @Override
    public void connected(XMPPConnection xmppConnection) {
        String myJID = RAUtils.decryptObjectForKey(RAConstant.kRAOwnJID);
        String password = RAUtils.decryptObjectForKey(RAConstant.kRAOwnPassword);
        String uuid = Settings.Secure.getString(RoomApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);

        try {
            RAXMPPManager.getInstance().getXmppStream().login(myJID,password, uuid);
        } catch (Exception e) {
            RAObservingService.getInstance().postNotification(RAConstant.kXMPPAuthenticationObserver,false);
        }
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection) {
        RAObservingService.getInstance().postNotification(RAConstant.kXMPPAuthenticationObserver,true);
    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectingIn(int i) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }
}
