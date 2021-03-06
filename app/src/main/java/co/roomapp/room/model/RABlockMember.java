package co.roomapp.room.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import java.io.Serializable;

/**
 * Entity mapped to table RABLOCK_MEMBER.
 */
public class RABlockMember implements Serializable {

    private Long id;
    private String memberJID;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public RABlockMember() {
    }

    public RABlockMember(Long id) {
        this.id = id;
    }

    public RABlockMember(Long id, String memberJID) {
        this.id = id;
        this.memberJID = memberJID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberJID() {
        return memberJID;
    }

    public void setMemberJID(String memberJID) {
        this.memberJID = memberJID;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
