package co.roomapp.room.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.provider.Settings;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.File;

import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.listener.RAXMPPStreamListener;
import co.roomapp.room.model.DaoMaster;
import co.roomapp.room.model.DaoSession;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAObservingService;
import co.roomapp.room.utils.RAUtils;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by drottemberg on 9/16/14.
 */
public class RACoreDataManager {

    private final static RACoreDataManager instance = new RACoreDataManager();


    private DaoSession daoSession;



    public static RACoreDataManager getInstance(){
        return instance;
    }

    private RACoreDataManager() {
        this.setupDatabase();

    }

    private void setupDatabase() {
        try {
            //RoomApplication.getInstance().getApplicationContext().deleteDatabase("roomapp-db");
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(RoomApplication.getInstance().getApplicationContext(), "roomapp-db", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            this.daoSession = daoMaster.newSession();


        }catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }


    public DaoSession getDaoSession() {
        if(daoSession == null) setupDatabase();
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

}

