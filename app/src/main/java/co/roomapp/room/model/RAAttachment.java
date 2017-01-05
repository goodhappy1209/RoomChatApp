package co.roomapp.room.model;

import java.io.Serializable;

import co.roomapp.room.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table RAATTACHMENT.
 */
public class RAAttachment implements Serializable {

    private Long id;
    private String albumName;
    private String body;
    private Long filesize;
    private Double latitude;
    private Double longitude;
    private String mediaLocalPath;
    private String mediaURL;
    private Double movieDuration;
    private String thumbLocalPath;
    private String title;
    private Integer type;
    private String vcardName;
    private String vcardString;
    private long messageId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RAAttachmentDao myDao;

    private RAMessage message;
    private Long message__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public RAAttachment() {
    }

    public RAAttachment(Long id) {
        this.id = id;
    }

    public RAAttachment(Long id, String albumName, String body, Long filesize, Double latitude, Double longitude, String mediaLocalPath, String mediaURL, Double movieDuration, String thumbLocalPath, String title, Integer type, String vcardName, String vcardString, long messageId) {
        this.id = id;
        this.albumName = albumName;
        this.body = body;
        this.filesize = filesize;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mediaLocalPath = mediaLocalPath;
        this.mediaURL = mediaURL;
        this.movieDuration = movieDuration;
        this.thumbLocalPath = thumbLocalPath;
        this.title = title;
        this.type = type;
        this.vcardName = vcardName;
        this.vcardString = vcardString;
        this.messageId = messageId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRAAttachmentDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getMediaLocalPath() {
        return mediaLocalPath;
    }

    public void setMediaLocalPath(String mediaLocalPath) {
        this.mediaLocalPath = mediaLocalPath;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public Double getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(Double movieDuration) {
        this.movieDuration = movieDuration;
    }

    public String getThumbLocalPath() {
        return thumbLocalPath;
    }

    public void setThumbLocalPath(String thumbLocalPath) {
        this.thumbLocalPath = thumbLocalPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVcardName() {
        return vcardName;
    }

    public void setVcardName(String vcardName) {
        this.vcardName = vcardName;
    }

    public String getVcardString() {
        return vcardString;
    }

    public void setVcardString(String vcardString) {
        this.vcardString = vcardString;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    /** To-one relationship, resolved on first access. */
    public RAMessage getMessage() {
        long __key = this.messageId;
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
        if (message == null) {
            throw new DaoException("To-one property 'messageId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.message = message;
            messageId = message.getId();
            message__resolvedKey = messageId;
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