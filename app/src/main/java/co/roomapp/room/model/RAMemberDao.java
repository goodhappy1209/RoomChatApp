package co.roomapp.room.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by a on 2/16/15.
 */
public class RAMemberDao  extends AbstractDao<RAMember, Long> implements Serializable {
    public static final String TABLENAME = "RAMEMBER";

    /**
     * Properties of entity RAMessage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Fullname = new Property(1, String.class, "fullname", false, "FULL_NAME");
        public final static Property MemberJID = new Property(2, String.class, "memberJID", false, "MEMBER_JID");
        public final static Property Username = new Property(3, String.class, "username", false, "USER_NAME");
        public final static Property RoomID = new Property(4, String.class, "roomID", false, "ROOM_ID");
        public final static Property Status = new Property(5, String.class, "status", false, "STATUS");
        public final static Property Description = new Property(6, String.class, "description", false, "DESCRIPTION");
        public final static Property Date = new Property(7, java.util.Date.class, "date", false, "DATE");
        public final static Property HasChat= new Property(8, Integer.class, "hasChat", false, "HAS_CHAT");
    };

    private DaoSession daoSession;

    public RAMemberDao(DaoConfig config) {
        super(config);
    }

    public RAMemberDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RAMEMBER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'FULLNAME' TEXT," + // 1: fullname
                "'MEMBER_JID' TEXT," + // 2: memberJID
                "'USER_NAME' TEXT," + // 3: username
                "'ROOM_ID' TEXT," + // 4: roomID
                "'STATUS' TEXT," + // 5: status
                "'DESCRIPTION' TEXT," + // 6: description
                "'DATE' DATE," + // 7: date
                "'HAS_CHAT' INTEGER);"); // 8: hasChat

    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RAMEMBER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RAMember entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String fullname = entity.getFullname();
        if (fullname != null) {
            stmt.bindString(2, fullname);
        }

        String memberJID = entity.getMemberJID();
        if (memberJID != null) {
            stmt.bindString(3, memberJID);
        }

        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(4, username);
        }

        String roomID = entity.getRoomID();
        if (roomID != null) {
            stmt.bindString(5, roomID);
        }

        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(6, status);
        }

        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }

        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(8, date.getTime());
        }

        Integer hasChat = entity.getHasChat();
        if (hasChat != null) {
            stmt.bindLong(9, hasChat);
        }

    }

    @Override
    protected void attachEntity(RAMember entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public RAMember readEntity(Cursor cursor, int offset) {
        RAMember entity = new RAMember( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // fullname
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // memberJID
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // username
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // roomID
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // status
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // description
                cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // date
                cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8) // hasChat
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RAMember entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFullname(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMemberJID(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUsername(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRoomID(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setStatus(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDescription(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDate(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setHasChat(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
    }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RAMember entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public Long getKey(RAMember entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRAChatSessionDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getRARoomDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getRAMessageDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T3", daoSession.getRANotificationDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T4", daoSession.getRAMessageDao().getAllColumns());
            builder.append(" FROM RAMESSAGE T");
            builder.append(" LEFT JOIN RACHAT_SESSION T0 ON T.'CHAT_SESSION_ID'=T0.'_id'");
            builder.append(" LEFT JOIN RAROOM T1 ON T.'ROOM_ID'=T1.'_id'");
            builder.append(" LEFT JOIN RAMESSAGE T2 ON T.'PARENT_MESSAGE_ID'=T2.'_id'");
            builder.append(" LEFT JOIN RANOTIFICATION T3 ON T.'NOTIFICATION_ID'=T3.'_id'");
            builder.append(" LEFT JOIN RAMESSAGE T4 ON T.'LAST_COMMENT_ID'=T4.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }

    protected RAMember loadCurrentDeep(Cursor cursor, boolean lock) {
        RAMember entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

//        RAChatSession chatSession = loadCurrentOther(daoSession.getRAChatSessionDao(), cursor, offset);
//        entity.setChatSession(chatSession);
//        offset += daoSession.getRAChatSessionDao().getAllColumns().length;
//
//        RARoom room = loadCurrentOther(daoSession.getRARoomDao(), cursor, offset);
//        entity.setRoom(room);
//        offset += daoSession.getRARoomDao().getAllColumns().length;
//
//        RAMessage parentMessage = loadCurrentOther(daoSession.getRAMessageDao(), cursor, offset);
//        entity.setParentMessage(parentMessage);
//        offset += daoSession.getRAMessageDao().getAllColumns().length;
//
//        RANotification notification = loadCurrentOther(daoSession.getRANotificationDao(), cursor, offset);
//        entity.setNotification(notification);
//        offset += daoSession.getRANotificationDao().getAllColumns().length;
//
//        RAMessage lastComment = loadCurrentOther(daoSession.getRAMessageDao(), cursor, offset);
//        entity.setLastComment(lastComment);

        return entity;
    }

    public RAMember loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();

        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);

        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }

    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<RAMember> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<RAMember> list = new ArrayList<RAMember>(count);

        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }

    protected List<RAMember> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }


    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<RAMember> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
}
