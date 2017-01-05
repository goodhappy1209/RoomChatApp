package co.roomapp.room.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import org.jivesoftware.smack.SmackAndroid;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import co.roomapp.room.activity.RAPaginationActivity;
import co.roomapp.room.activity.RARegisterCodeActivity;
import co.roomapp.room.activity.RARegisterPhoneActivity;
import co.roomapp.room.activity.RARegisterNameActivity;
import co.roomapp.room.activity.RATabBarActivity;
import co.roomapp.room.manager.RACoreDataManager;
import co.roomapp.room.manager.RAManager;
import co.roomapp.room.manager.RAXMPPManager;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAObservingService;

/**
 * Created by drottemberg on 9/16/14.
 */
public class RoomApplication extends Application implements Observer{

    private static RoomApplication INSTANCE;



    private Activity launcher;


    public static RoomApplication getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        INSTANCE = this;
        RACoreDataManager.getInstance();
        this.setupNotifications();

        //RACoreDataManager.getInstance().getDaoSession().getRARoomDao().deleteAll();

    }



    @Override
    public void update(Observable observable, Object o) {
        HashMap<String,Object> dict = (HashMap<String,Object>) o;
        if(dict.get(RAConstant.kRANotification).equals(RAConstant.kXMPPAuthenticationObserver)){
            this.xmppAuthenticationReceived();
        }
    }

    public void setupNotifications(){
        RAObservingService.getInstance().addObserver(RAConstant.kXMPPAuthenticationObserver,this);
    }

    public void xmppAuthenticationReceived(){
        RAObservingService.getInstance().removeObserver(RAConstant.kXMPPAuthenticationObserver,this);
//        RAManager.getInstance().syncPhoneContacts();
//        Intent intent = new Intent(this, RATabBarActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        launcher.finish();


        if(RAXMPPManager.getInstance().getXmppStream().isAuthenticated()){
            RAManager.getInstance().syncPhoneContacts();
            RAXMPPManager.getInstance().syncRooms();
            Intent intent = new Intent(this, RATabBarActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            launcher.finish();

        }

        else {
//            Intent intent = new Intent(this, RARegisterPhoneActivity.class);
//            Intent intent = new Intent(this, RAPaginationActivity.class);
//            Intent intent = new Intent(this, RARegisterCodeActivity.class);
//            Intent intent = new Intent(this, RARegisterNameActivity.class);
            Intent intent = new Intent(this, RATabBarActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            launcher.finish();
        }
    }

    public Activity getLauncher() {
        return launcher;
    }

    public void setLauncher(Activity launcher) {
        this.launcher = launcher;
    }
}
