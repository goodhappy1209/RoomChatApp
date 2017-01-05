package co.roomapp.room.model;

import java.io.Serializable;
import java.util.Date;

import de.greenrobot.dao.DaoException;

/**
 * Created by a on 2/16/15.
 */

public class RAMember implements Serializable{

    private Long id;
    private String fullname;
    private String memberJID;
    private String username;
    private String roomID;
    private String status;
    private String description;
    private Date date;
    private Integer hasChat;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RAMemberDao myDao;


    public RAMember() {

    }

    public RAMember(Long id) {
        this.id = id;
    }

    public RAMember(Long id, String fullname, String JID, String username, String roomID, String status, String description, Date date, Number hastChat)
    {
        this.id = id;
        this.fullname = fullname;
        this.memberJID = JID;
        this.roomID = roomID;
        this.status = status;
        this.description = description;
        this.date = date;
        this.hasChat = hasChat;
    }

    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRAMemberDao() : null;
    }

    public Long getId() {return this.id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {return this.fullname;}

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMemberJID() {return this.memberJID;}

    public void setMemberJID(String JID) {
        this.memberJID = JID;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomID() {
        return this.roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getHasChat() {
        return this.hasChat;
    }

    public void setHasChat(Integer hasChat) {
        this.hasChat = hasChat;
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
}
