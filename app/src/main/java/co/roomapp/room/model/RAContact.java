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
 * Entity mapped to table RACONTACT.
 */
public class RAContact implements Serializable{

    private Long id;
    private String abuserid;
    private String firstname;
    private String fullname;
    private String indexname;
    private long sectionId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RAContactDao myDao;

    private RAContactSection section;
    private Long section__resolvedKey;

    private List<RAPhone> phones;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public RAContact() {
    }

    public RAContact(Long id) {
        this.id = id;
    }

    public RAContact(Long id, String abuserid, String firstname, String fullname, String indexname, long sectionId) {
        this.id = id;
        this.abuserid = abuserid;
        this.firstname = firstname;
        this.fullname = fullname;
        this.indexname = indexname;
        this.sectionId = sectionId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRAContactDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbuserid() {
        return abuserid;
    }

    public void setAbuserid(String abuserid) {
        this.abuserid = abuserid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIndexname() {
        return indexname;
    }

    public void setIndexname(String indexname) {
        this.indexname = indexname;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    /** To-one relationship, resolved on first access. */
    public RAContactSection getSection() {
        long __key = this.sectionId;
        if (section__resolvedKey == null || !section__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RAContactSectionDao targetDao = daoSession.getRAContactSectionDao();
            RAContactSection sectionNew = targetDao.load(__key);
            synchronized (this) {
                section = sectionNew;
            	section__resolvedKey = __key;
            }
        }
        return section;
    }

    public void setSection(RAContactSection section) {
        if (section == null) {
            throw new DaoException("To-one property 'sectionId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.section = section;
            sectionId = section.getId();
            section__resolvedKey = sectionId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<RAPhone> getPhones() {
        if (phones == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RAPhoneDao targetDao = daoSession.getRAPhoneDao();
            List<RAPhone> phonesNew = targetDao._queryRAContact_Phones(id);
            synchronized (this) {
                if(phones == null) {
                    phones = phonesNew;
                }
            }
        }
        return phones;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetPhones() {
        phones = null;
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
    public static ConcurrentHashMap getAllContacts(){
        ConcurrentHashMap contacts = new ConcurrentHashMap();
        List listContacts = RACoreDataManager.getInstance().getDaoSession().getRAContactDao().queryBuilder().list();
        Iterator i;
        for(i = listContacts.iterator(); i.hasNext();){
            RAContact c = (RAContact)i.next();
            contacts.putIfAbsent(c.getAbuserid(),c);
        }
        return  contacts;
    }
    // KEEP METHODS END

}
