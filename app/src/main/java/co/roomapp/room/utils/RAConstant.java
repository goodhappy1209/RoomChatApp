package co.roomapp.room.utils;

/**
 * Created by drottemberg on 9/16/14.
 */
public class RAConstant {

    public static final String preferences = "co.roomapp.preferences";
    public static final String kRAOwnName ="RAOwnName";
    public static final String kRAOwnJID ="RAOwnJID";
    public static final String kRAOwnPassword ="RAOwnPassword";
    public static final String kRAOwnPhoneNumber ="RAOwnPhoneNumber";
    public static final String kRAOwnCountryCode ="RAOwnCountryCode";
    public static final String kRAOwnFirstName ="RAOwnFirstName";
    public static final String kRADeviceToken ="RADeviceToken";
    public static final String kRALocalKey ="Bf_{=OvZvFa'>$J{9KD6)).HX%&#)W^f~kbyN@y'3,VQnN(iSe@+jcj=39:z<:";
    public static final String kRARemoteKey ="1GI4*h.kKH5H+m@'E1*uc3`N}!Pq{,W{O20<C#i'ddfBo5hL}3RsW$%TZAr<R+X";
    public static final String kRARemoteUser ="roomadminghostuser";
    public static final String kRARemotePassword ="123456";
    public static final String kRAPropertyNotification ="RAPropertyNotification";
    public static final String kRAPropertyPassword ="RAPropertyPassword";
    public static final String kRALoadCustomURL ="RALoadCustomURL";
    public static final String kRANotification ="kRANotification";
    public static final String kRAObject ="kRAObject";


    public static final String kRAHasUserRequest =  "RAHasUserRequest";
    public static final String kLastFullSync =  "kLastFullSync";




    //Observer
    public static final String kXMPPAuthenticationObserver = "kXMPPAuthenticationObserver";
    public static final String kReloadListRoomNotification = "kReloadListRoomNotification";
    public static final String kReloadCommentsObserver = "kReloadCommentsObserver";
    public static final String kReloadTimelineObserver = "kReloadTimelineObserver";
    public static final String kReloadRoomObserver = "kReloadRoomObserver";
    public static final String kRAUpdateTabChatBage = "kRAUpdateTabChatBage";
    public static final String kJoinSocialRoomGoOnlineObserver = "kJoinSocialRoomGoOnlineObserver";
    public static final String kSyncRoomObserver = "kSyncRoomObserver";

    //Constants Environment



    //DEV

    public static final String  HOST           ="ezrdv.org";
    public static final String  MEDIA_HOST     ="ezrdv.org";
    public static final String  HOSTNAME       ="im.roomapp.local";
    public static final String  CONFERENCE     ="rc.im.roomapp.local";
    public static final int     PORT_XMPP      = 5222;
    public static final String  PORT_NODEJS    ="6556";
    public static final String  PORT_OF        ="9090";
    public static final String  FLURRY_KEY     ="RQK2PXFR466SWJW";


    public static final int gesture_filter_value = 100;
    public static final int swipe_filter_value = 800;
    public static final int duration_without_velocity = 300;
    public static final int numerator_duration_with_velocity = 500;
    public static final int swip_standard_velocity = 300;

    public enum RAMessageType {
        RAMessageTypeRegularPost,
                RAMessageTypeAdminPost,
                RAMessageTypeOnePhotoPost,
                RAMessageTypeMultiplePhotoPost,
                RAMessageTypeVideoPost,
                RAMessageTypeAudioPost,
                RAMessageTypeUrlPost,
                RAMessageTypeLocationPost
    };

    public enum  RAAttachmentType{
        RAAttachmentTypeImage,
                RAAttachmentTypeVideo,
                RAAttachmentTypeAudio,
                RAAttachmentTypeMap,
                RAAttachmentTypeURL,
                RAAttachmentTypeVCard
    };


    public enum RAMessageMood {
        RAMessageMoodNormal,
                RAMessageMoodThinking,
                RAMessageMoodExcited
    };

    public enum  RAMessageAction{
        RAMessageActionPost,
                RAMessageActionComment,
                RAMessageActionLike,
                RAMessageActionDelete,
                RAMessageActionNotify,
                RAMessageActionChat,
                RAMessageActionReceipt,
                RAMessageActionStartTyping,
                RAMessageActionStopTyping,
                RAMessageActionUnlike
    };


    enum RANotificationType {
        RANotificationTypePost,
                RANotificationTypeComment,
                RANotificationTypeLike,
    };

    public enum RAMessageDirection {
        RAMessageDirectionLeft,
        RAMessageDirectionRight,
    };

    public enum RAScrollDirection {
        RAScrollDirectionNone,
        RAScrollDirectionRight,
        RAScrollDirectionLeft,
        RAScrollDirectionUp,
        RAScrollDirectionDown,
        RAScrollDirectionCrazy,
    };

    public enum RA_ACTION_BAR_STATE {
        ACTION_BAR_HIDE,
        ACTION_BAR_MEDIA_SHOWN,
        ACTION_BAR_GALLERY_SHOWN
    }


}

