package co.roomapp.room.model;

import java.io.Serializable;

import co.roomapp.room.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table RANOTIFICATION.
 */
public class RANotification implements Serializable {

    private Long id;
    private String body;
    private String fromJID;
    private Boolean isRead;
    private String messageRoomappID;
    private java.util.Date notificationDate;
    private String parentID;
    private Integer type;
    private Long messageId;
    private Long roomId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RANotificationDao myDao;

    private RAMessage message;
    private Long message__resolvedKey;

    private RARoom room;
    private Long room__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public RANotification() {
    }

    public RANotification(Long id) {
        this.id = id;
    }

    public RANotification(Long id, String body, String fromJID, Boolean isRead, String messageRoomappID, java.util.Date notificationDate, String parentID, Integer type, Long messageId, Long roomId) {
        this.id = id;
        this.body = body;
        this.fromJID = fromJID;
        this.isRead = isRead;
        this.messageRoomappID = messageRoomappID;
        this.notificationDate = notificationDate;
        this.parentID = parentID;
        this.type = type;
        this.messageId = messageId;
        this.roomId = roomId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRANotificationDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFromJID() {
        return fromJID;
    }

    public void setFromJID(String fromJID) {
        this.fromJID = fromJID;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getMessageRoomappID() {
        return messageRoomappID;
    }

    public void setMessageRoomappID(String messageRoomappID) {
        this.messageRoomappID = messageRoomappID;
    }

    public java.util.Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(java.util.Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    /** To-one relationship, resolved on first access. */
    public RAMessage getMessage() {
        Long __key = this.messageId;
        if (message__resolvedKey == null || !message__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RAMessageDao targetDao = daoSession.getRAMessageDao();
            RAMessage messageNew = targetDao.load(__key);
            synchronized (this) {
                message = messageNew;
            	message__resolvedKey = __key;
            }
        }
        return message;
    }

    public void setMessage(RAMessage message) {
        synchronized (this) {
            this.message = message;
            messageId = message == null ? null : message.getId();
            message__resolvedKey = messageId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public RARoom getRoom() {
        Long __key = this.roomId;
        if (room__resolvedKey == null || !room__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RARoomDao targetDao = daoSession.getRARoomDao();
            RARoom roomNew = targetDao.load(__key);
            synchronized (this) {
                room = roomNew;
            	room__resolvedKey = __key;
            }
        }
        return room;
    }

    public void setRoom(RARoom room) {
        synchronized (this) {
            this.room = room;
            roomId = room == null ? null : room.getId();
            room__resolvedKey = roomId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
