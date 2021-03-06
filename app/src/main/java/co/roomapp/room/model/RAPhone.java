package co.roomapp.room.model;

import java.io.Serializable;
import java.util.List;
import co.roomapp.room.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import co.roomapp.room.manager.RACoreDataManager;
// KEEP INCLUDES END
/**
 * Entity mapped to table RAPHONE.
 */
public class RAPhone  implements Serializable {

    private Long id;
    private Boolean hasRoomApp;
    private String label;
    private String phone;
    private String roomappid;
    private long contactId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RAPhoneDao myDao;

    private RAContact contact;
    private Long contact__resolvedKey;

    private List<RARoomInvite> invites;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public RAPhone() {
    }

    public RAPhone(Long id) {
        this.id = id;
    }

    public RAPhone(Long id, Boolean hasRoomApp, String label, String phone, String roomappid, long contactId) {
        this.id = id;
        this.hasRoomApp = hasRoomApp;
        this.label = label;
        this.phone = phone;
        this.roomappid = roomappid;
        this.contactId = contactId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRAPhoneDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHasRoomApp() {
        return hasRoomApp;
    }

    public void setHasRoomApp(Boolean hasRoomApp) {
        this.hasRoomApp = hasRoomApp;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoomappid() {
        return roomappid;
    }

    public void setRoomappid(String roomappid) {
        this.roomappid = roomappid;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    /** To-one relationship, resolved on first access. */
    public RAContact getContact() {
        long __key = this.contactId;
        if (contact__resolvedKey == null || !contact__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RAContactDao targetDao = daoSession.getRAContactDao();
            RAContact contactNew = targetDao.load(__key);
            synchronized (this) {
                contact = contactNew;
            	contact__resolvedKey = __key;
            }
        }
        return contact;
    }

    public void setContact(RAContact contact) {
        if (contact == null) {
            throw new DaoException("To-one property 'contactId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.contact = contact;
            contactId = contact.getId();
            contact__resolvedKey = contactId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<RARoomInvite> getInvites() {
        if (invites == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RARoomInviteDao targetDao = daoSession.getRARoomInviteDao();
            List<RARoomInvite> invitesNew = targetDao._queryRAPhone_Invites(id);
            synchronized (this) {
                if(invites == null) {
                    invites = invitesNew;
                }
            }
        }
        return invites;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetInvites() {
        invites = null;
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
    public static ConcurrentHashMap getAllPhones(){
        ConcurrentHashMap phones = new ConcurrentHashMap();
        List listPhones = RACoreDataManager.getInstance().getDaoSession().getRAPhoneDao().queryBuilder().list();
        Iterator i;
        for(i = listPhones.iterator(); i.hasNext();){
            RAPhone c = (RAPhone)i.next();
            phones.putIfAbsent(c.getUniqueIdentifier(),c);
        }
        return  phones;
    }

    public String getUniqueIdentifier(){
        return this.getContact().getAbuserid()+"_"+this.getLabel()+"_"+this.getPhone();
    }
    // KEEP METHODS END

}
