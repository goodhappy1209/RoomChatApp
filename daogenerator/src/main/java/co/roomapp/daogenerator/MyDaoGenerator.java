package co.roomapp.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {


    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "co.roomapp.room.model");
        schema.enableKeepSectionsByDefault();
        setupEntitiest(schema);


        new DaoGenerator().generateAll(schema, "../Source/app/src/main/java");
    }

    private static void setupEntitiest(Schema schema) {

        //RAAttachment
        Entity raAttachment = schema.addEntity("RAAttachment");
        raAttachment.addIdProperty();
        raAttachment.addStringProperty("albumName");
        raAttachment.addStringProperty("body");
        raAttachment.addLongProperty("filesize");
        raAttachment.addDoubleProperty("latitude");
        raAttachment.addDoubleProperty("longitude");
        raAttachment.addStringProperty("mediaLocalPath");
        raAttachment.addStringProperty("mediaURL");
        raAttachment.addDoubleProperty("movieDuration");
        raAttachment.addStringProperty("thumbLocalPath");
        raAttachment.addStringProperty("title");
        raAttachment.addIntProperty("type");
        raAttachment.addStringProperty("vcardName");
        raAttachment.addStringProperty("vcardString");

       //RABlockMember\
        Entity raBlockMember = schema.addEntity("RABlockMember");
        raBlockMember.addIdProperty();
        raBlockMember.addStringProperty("memberJID");

        //RAChatSession
        Entity raChatSession = schema.addEntity("RAChatSession");
        raChatSession.addIdProperty();
        raChatSession.addStringProperty("contactABID");
        raChatSession.addStringProperty("contactJID");
        raChatSession.addBooleanProperty("isConnected");
        raChatSession.addBooleanProperty("isOnline");
        raChatSession.addDateProperty("lastConnected");
        raChatSession.addDateProperty("lastEntered");
        raChatSession.addDateProperty("lastMessageDate");
        raChatSession.addStringProperty("lastMessageText");
        raChatSession.addStringProperty("partnerName");
        raChatSession.addStringProperty("password");
        raChatSession.addStringProperty("status");
        raChatSession.addLongProperty("unreadMessages");

        //RAContact
        Entity raContact = schema.addEntity("RAContact");
        raContact.addIdProperty();
        raContact.addStringProperty("abuserid");
        raContact.addStringProperty("firstname");
        Property contactFullname = raContact.addStringProperty("fullname").getProperty();
        raContact.addStringProperty("indexname");

        //raConctactSection
        Entity raContactSection = schema.addEntity("RAContactSection");
        raContactSection.addIdProperty();
        raContactSection.addLongProperty("contactcount");
        raContactSection.addStringProperty("title");

        //RAMessage
        Entity raMessage = schema.addEntity("RAMessage");
        raMessage.addIdProperty();
        raMessage.addStringProperty("body");
        raMessage.addIntProperty("bubbleType");
        raMessage.addDoubleProperty("cellHeight");
        raMessage.addBooleanProperty("commentsCount");
        raMessage.addStringProperty("fromJID");
        raMessage.addBooleanProperty("fromMe");
        raMessage.addBooleanProperty("isDelivered");
        raMessage.addBooleanProperty("isLike");
        raMessage.addBooleanProperty("isMoreSelected");
        raMessage.addBooleanProperty("isMute");
        raMessage.addBooleanProperty("isPinned");
        raMessage.addBooleanProperty("isRead");
        raMessage.addDateProperty("lastCommentSync");
        raMessage.addDateProperty("lastEntered");
        raMessage.addStringProperty("lastMessageBody");
        raMessage.addDateProperty("lastMessageDate");
        raMessage.addStringProperty("lastMessageFromJID");
        raMessage.addStringProperty("lastMessageFromName");
        raMessage.addStringProperty("lastMessageID");
        raMessage.addDateProperty("lastSync");
        raMessage.addDoubleProperty("likeRating");
        raMessage.addLongProperty("likesCount");
        raMessage.addDateProperty("messagedate");
        raMessage.addStringProperty("password");
        raMessage.addStringProperty("pushname");
        raMessage.addStringProperty("roomappid");
        raMessage.addStringProperty("roomJID");
        raMessage.addStringProperty("subject");
        raMessage.addStringProperty("toJID");
        raMessage.addIntProperty("type");
        raMessage.addDateProperty("updatedAt");

        //RANotification
        Entity raNotification = schema.addEntity("RANotification");
        raNotification.addIdProperty();
        raNotification.addStringProperty("body");
        raNotification.addStringProperty("fromJID");
        raNotification.addBooleanProperty("isRead");
        raNotification.addStringProperty("messageRoomappID");
        raNotification.addDateProperty("notificationDate");
        raNotification.addStringProperty("parentID");
        raNotification.addIntProperty("type");

        //RAPhone
        Entity raPhone = schema.addEntity("RAPhone");
        raPhone.addIdProperty();
        raPhone.addBooleanProperty("hasRoomApp");
        raPhone.addStringProperty("label");
        raPhone.addStringProperty("phone");
        raPhone.addStringProperty("roomappid");

        //RARoomInvite
        Entity raRoomInvite = schema.addEntity("RARoomInvite");
        raRoomInvite.addIdProperty();
        raRoomInvite.addDateProperty("createdAt");
        raRoomInvite.addIntProperty("status");
        raRoomInvite.addDateProperty("updatedAt");

        //RARoom
        Entity raRoom = schema.addEntity("RARoom");
        raRoom.addIdProperty();
        raRoom.addStringProperty("coverLocalPath");
        raRoom.addStringProperty("coverURL");
        raRoom.addDateProperty("createdAt");
        raRoom.addStringProperty("hashtags");
        raRoom.addBooleanProperty("iNewMessagesCount");
        raRoom.addBooleanProperty("isAdmin");
        raRoom.addBooleanProperty("isAdult");
        raRoom.addBooleanProperty("isChatEnable");
        raRoom.addBooleanProperty("isMute");
        raRoom.addBooleanProperty("isPrivate");
        raRoom.addBooleanProperty("isSearchable");
        raRoom.addStringProperty("language");
        raRoom.addDateProperty("lastEntered");
        raRoom.addDateProperty("lastmessageDate");
        raRoom.addStringProperty("lastMessageFromJID");
        raRoom.addStringProperty("lastMessageFromName");
        raRoom.addStringProperty("lastMessageText");
        raRoom.addDateProperty("lastSync");
        raRoom.addDoubleProperty("latitude");
        raRoom.addDateProperty("lockDate");
        raRoom.addDoubleProperty("longitude");
        raRoom.addLongProperty("maxGuests");
        raRoom.addLongProperty("membersCount");
        raRoom.addStringProperty("name");
        raRoom.addStringProperty("nickname");
        raRoom.addStringProperty("ownerDisplayName");
        raRoom.addStringProperty("ownerJID");
        raRoom.addStringProperty("password");
        raRoom.addStringProperty("roomappid");
        raRoom.addStringProperty("roomDescription");
        raRoom.addStringProperty("roomJID");
        raRoom.addStringProperty("roomkey");
        raRoom.addBooleanProperty("toDelete");
        raRoom.addBooleanProperty("toSave");
        raRoom.addLongProperty("unreadMessage");
        raRoom.addDateProperty("updatedAt");



        //Relations RAAttachment
        Property attMessageIdProperty = raAttachment.addLongProperty("messageId").notNull().getProperty();
        raAttachment.addToOne(raMessage, attMessageIdProperty).setName("message");
        raMessage.addToMany(raAttachment, attMessageIdProperty).setName("attachments");



        //Relations RAChatSession

        Property csRoomIdProperty = raChatSession.addLongProperty("roomId").getProperty();
        raChatSession.addToOne(raRoom, csRoomIdProperty).setName("room");
        raRoom.addToMany(raChatSession, csRoomIdProperty).setName("chatsessions");


        //Relation Contact

        Property cSectionIdProperty = raContact.addLongProperty("sectionId").notNull().getProperty();
        raContact.addToOne(raContactSection, cSectionIdProperty).setName("section");

        ToMany sectionToContacts = raContactSection.addToMany(raContact, cSectionIdProperty);
        sectionToContacts.setName("contacts");
        sectionToContacts.orderAsc(contactFullname);


        //Relation RARoomInvite

        Property riPhoneIdProperty = raRoomInvite.addLongProperty("phoneId").notNull().getProperty();
        raRoomInvite.addToOne(raPhone, riPhoneIdProperty).setName("phone");
        raPhone.addToMany(raRoomInvite, riPhoneIdProperty).setName("invites");

        Property riRoomIdProperty = raRoomInvite.addLongProperty("roomId").notNull().getProperty();
        raRoomInvite.addToOne(raRoom, riRoomIdProperty).setName("room");
        raRoom.addToMany(raRoomInvite, riRoomIdProperty).setName("invites");


        //Relation RARoom
        Property rLastMessageIdProperty = raRoom.addLongProperty("lastMessageId").getProperty();
        raRoom.addToOne(raMessage, rLastMessageIdProperty).setName("lastMessage");


        //Relation RAPhone
        Property pContactIdProperty = raPhone.addLongProperty("contactId").notNull().getProperty();
        raPhone.addToOne(raContact, pContactIdProperty).setName("contact");
        raContact.addToMany(raPhone, pContactIdProperty).setName("phones");

        //Relation Notification


        Property nMessageIdProperty = raNotification.addLongProperty("messageId").getProperty();
        raNotification.addToOne(raMessage, nMessageIdProperty).setName("message");


        Property nRoomIdProperty = raNotification.addLongProperty("roomId").getProperty();
        raNotification.addToOne(raRoom, nRoomIdProperty).setName("room");
        raRoom.addToMany(raNotification, nRoomIdProperty).setName("notifications");


        //Relation RAMessage

        Property mChatSessionIdProperty = raMessage.addLongProperty("chatSessionId").getProperty();
        raMessage.addToOne(raChatSession, mChatSessionIdProperty).setName("chatSession");
        raChatSession.addToMany(raMessage, mChatSessionIdProperty).setName("messages");

        Property mRoomIdProperty = raMessage.addLongProperty("roomId").getProperty();
        raMessage.addToOne(raRoom, mRoomIdProperty).setName("room");
        raRoom.addToMany(raMessage, mRoomIdProperty).setName("messages");


        Property mParentMessageIdProperty = raMessage.addLongProperty("parentMessageId").getProperty();
        raMessage.addToOne(raMessage, mParentMessageIdProperty).setName("parentMessage");
        raMessage.addToMany(raMessage, mParentMessageIdProperty).setName("comments");

        Property mNotificationIdProperty = raMessage.addLongProperty("notificationId").getProperty();
        raMessage.addToOne(raNotification, mNotificationIdProperty).setName("notification");

        Property mLastCommentIdProperty = raMessage.addLongProperty("lastCommentId").getProperty();
        raMessage.addToOne(raMessage, mLastCommentIdProperty).setName("lastComment");



    }


}
