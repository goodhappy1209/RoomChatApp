package co.roomapp.room.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import co.roomapp.room.model.RAAttachmentDao;
import co.roomapp.room.model.RABlockMemberDao;
import co.roomapp.room.model.RAChatSessionDao;
import co.roomapp.room.model.RAContactDao;
import co.roomapp.room.model.RAContactSectionDao;
import co.roomapp.room.model.RAMessageDao;
import co.roomapp.room.model.RANotificationDao;
import co.roomapp.room.model.RAPhoneDao;
import co.roomapp.room.model.RARoomInviteDao;
import co.roomapp.room.model.RARoomDao;
import co.roomapp.room.model.RAMemberDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster implements Serializable {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        RAAttachmentDao.createTable(db, ifNotExists);
        RABlockMemberDao.createTable(db, ifNotExists);
        RAChatSessionDao.createTable(db, ifNotExists);
        RAContactDao.createTable(db, ifNotExists);
        RAContactSectionDao.createTable(db, ifNotExists);
        RAMessageDao.createTable(db, ifNotExists);
        RANotificationDao.createTable(db, ifNotExists);
        RAPhoneDao.createTable(db, ifNotExists);
        RARoomInviteDao.createTable(db, ifNotExists);
        RARoomDao.createTable(db, ifNotExists);
        RAMemberDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        RAAttachmentDao.dropTable(db, ifExists);
        RABlockMemberDao.dropTable(db, ifExists);
        RAChatSessionDao.dropTable(db, ifExists);
        RAContactDao.dropTable(db, ifExists);
        RAContactSectionDao.dropTable(db, ifExists);
        RAMessageDao.dropTable(db, ifExists);
        RANotificationDao.dropTable(db, ifExists);
        RAPhoneDao.dropTable(db, ifExists);
        RARoomInviteDao.dropTable(db, ifExists);
        RARoomDao.dropTable(db, ifExists);
        RAMemberDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(RAAttachmentDao.class);
        registerDaoClass(RABlockMemberDao.class);
        registerDaoClass(RAChatSessionDao.class);
        registerDaoClass(RAContactDao.class);
        registerDaoClass(RAContactSectionDao.class);
        registerDaoClass(RAMessageDao.class);
        registerDaoClass(RANotificationDao.class);
        registerDaoClass(RAPhoneDao.class);
        registerDaoClass(RARoomInviteDao.class);
        registerDaoClass(RARoomDao.class);
        registerDaoClass(RAMemberDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}