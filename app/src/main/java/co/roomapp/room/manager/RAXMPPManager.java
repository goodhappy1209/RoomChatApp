package co.roomapp.room.manager;

import android.os.StrictMode;
import android.provider.Settings;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.listener.RAXMPPStreamListener;
import co.roomapp.room.model.RARoom;
import co.roomapp.room.provider.RASyncRoomIQProvider;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAObservingService;
import co.roomapp.room.utils.RAUtils;
import co.roomapp.room.utils.XMPPIQ;

/**
 * Created by drottemberg on 9/16/14.
 */
public class RAXMPPManager {

    private final static RAXMPPManager instance = new RAXMPPManager();
    private RAXMPPStreamListener xmppStreamListener;



    private XMPPConnection xmppStream;

    public static RAXMPPManager getInstance(){
        return instance;
    }

    private RAXMPPManager() {
        this.xmppStreamListener = new RAXMPPStreamListener();

    }



    public void setupStream(){

        SmackAndroid.init(RoomApplication.getInstance());

       ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(RAConstant.HOST,RAConstant.PORT_XMPP,RAConstant.HOSTNAME);
       // connectionConfiguration.setSASLAuthenticationEnabled(ConnectionConfiguration.SecurityMode.);
        //connectionConfiguration.setSecurityMode(true);
        connectionConfiguration.setRosterLoadedAtLogin(false);
        connectionConfiguration.setReconnectionAllowed(true);
        connectionConfiguration.setSendPresence(false);
        connectionConfiguration.setCompressionEnabled(true);
        connectionConfiguration.setDebuggerEnabled(true);
        connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);


        this.xmppStream = new XMPPTCPConnection(connectionConfiguration);
        this.xmppStream.addConnectionListener(this.xmppStreamListener);
        this.xmppStream.addPacketListener(this.xmppStreamListener, new PacketFilter() {
            @Override
            public boolean accept(Packet packet) {
                return true;
            }
        });

        ProviderManager.addIQProvider("query", "room:iq:syncroom",
                new RASyncRoomIQProvider());


    }

    public boolean connect(){
        if(this.xmppStream.isConnected()){
            return true;
        }

        String myJID = RAUtils.decryptObjectForKey(RAConstant.kRAOwnJID);
        String password = RAUtils.decryptObjectForKey(RAConstant.kRAOwnPassword);
        String uuid = Settings.Secure.getString(RoomApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);

        if (myJID == null || password == null) {
            RAObservingService.getInstance().postNotification(RAConstant.kXMPPAuthenticationObserver,false);
            return false;
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

         //XMPPConnectTask xmppConnectTask = new XMPPConnectTask();
            //xmppConnectTask.execute();

            //this.xmppStream.connect();
            //this.xmppStream.login(myJID,password,uuid);


        try {
            RAXMPPManager.getInstance().getXmppStream().connect();
        } catch (Exception e) {
            //e.printStackTrace();
            RAObservingService.getInstance().postNotification(RAConstant.kXMPPAuthenticationObserver,false);
            return false;
        }
        return true;
    }


    public RAXMPPStreamListener getXmppStreamListener() {
        return xmppStreamListener;
    }

    public void setXmppStreamListener(RAXMPPStreamListener xmppStreamListener) {
        this.xmppStreamListener = xmppStreamListener;
    }

    public XMPPConnection getXmppStream() {
        return xmppStream;
    }

    public void setXmppStream(XMPPConnection xmppStream) {
        this.xmppStream = xmppStream;
    }



    public void syncRooms(){

        XMPPIQ iq = RARoom.getAllRoomIQ();

        try {
            this.getXmppStream().sendPacket(iq);
        } catch (SmackException.NotConnectedException e) {
            return ;
        }

    }


   /* private class XMPPConnectTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            try {



                RAXMPPManager.getInstance().getXmppStream().connect();
                String myJID = RAUtils.decryptObjectForKey(RAConstant.kRAOwnJID);
                String password = RAUtils.decryptObjectForKey(RAConstant.kRAOwnPassword);
                String uuid = Settings.Secure.getString(RoomApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);

                RAXMPPManager.getInstance().getXmppStream().login("admin", "123456", uuid);


            } catch (Exception e) {
                //e.printStackTrace();
                RAObservingService.getInstance().postNotification(RAConstant.kXMPPAuthenticationObserver,false);
            }
            RAObservingService.getInstance().postNotification(RAConstant.kXMPPAuthenticationObserver,true);
        }
    }
*/



}

