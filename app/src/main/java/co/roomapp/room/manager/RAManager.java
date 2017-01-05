package co.roomapp.room.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.listener.RAXMPPStreamListener;
import co.roomapp.room.model.RAContact;
import co.roomapp.room.model.RAContactDao;
import co.roomapp.room.model.RAContactSection;
import co.roomapp.room.model.RAPhone;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAObservingService;
import co.roomapp.room.utils.RAUtils;

/**
 * Created by drottemberg on 9/16/14.
 */
public class RAManager {

    private final static RAManager instance = new RAManager();

    public static RAManager getInstance(){
        return instance;
    }

    private RAManager() {

    }


    public void syncPhoneContacts(){

        try {

            ConcurrentHashMap sections = RAContactSection.getAllSections();
            ArrayList<RAContactSection> insertContactSection = new ArrayList<RAContactSection>();


            ConcurrentHashMap contacts = RAContact.getAllContacts();
            ConcurrentHashMap allContacts = new ConcurrentHashMap();
            ArrayList<RAContact> updateContacts = new ArrayList<RAContact>();
            ArrayList<RAContact> insertContacts = new ArrayList<RAContact>();



            ConcurrentHashMap phones = RAPhone.getAllPhones();
            ConcurrentHashMap allPhones = new ConcurrentHashMap();
            ArrayList<RAPhone> insertPhones = new ArrayList<RAPhone>();



            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE};

            Cursor people = RoomApplication.getInstance().getApplicationContext().getContentResolver().query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            if (people.getCount() > 0) {
                while (people.moveToNext()) {
                    String fullname = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).trim();
                    String contactIndex;
                    if (fullname.length() > 0) {
                        String c = fullname.substring(0, 1).toUpperCase();
                        if (Character.isLetter(c.charAt(0))) {
                            contactIndex = c;
                        } else {
                            contactIndex = "#";
                        }
                    } else {
                        contactIndex = "#";
                    }
                    if (!sections.containsKey(contactIndex)) {
                        RAContactSection contactSection = new RAContactSection();
                        contactSection.setTitle(contactIndex);
                        sections.putIfAbsent(contactIndex, contactSection);
                        insertContactSection.add(contactSection);
                    }
                }
                people.close();
                if(insertContactSection.size()>0){
                    RACoreDataManager.getInstance().getDaoSession().getRAContactSectionDao().insertInTx(insertContactSection);
                }
            }

            people = RoomApplication.getInstance().getApplicationContext().getContentResolver().query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (people.getCount() > 0) {

                while (people.moveToNext()) {
                    RAContact newPerson = null;
                    String id = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String phoneNo = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();
                    String fullname = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).trim();
                    String label = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)).trim();
                    String firstName = fullname;

                    if(!allContacts.containsKey(id)) {

                        String[] names = fullname.split(" ", 2);
                        firstName = names[0];
                        boolean isNew = true;
                        boolean toUpdate = false;

                        if (!contacts.containsKey(id)) {
                            newPerson = new RAContact();
                            insertContacts.add(newPerson);
                        } else {
                            newPerson = (RAContact) contacts.get(id);
                            updateContacts.add(newPerson);
                        }

                        if (!fullname.equals(newPerson.getFullname())) {
                            newPerson.setFullname(fullname);
                        }
                        if (!firstName.equals(newPerson.getFirstname())) {
                            newPerson.setFirstname(firstName);
                        }
                        if (!firstName.equals(newPerson.getIndexname())) {
                            newPerson.setIndexname(firstName);
                        }

                        String contactIndex;
                        if (fullname.length() > 0) {
                            String c = fullname.substring(0, 1).toUpperCase();
                            if (Character.isLetter(c.charAt(0))) {
                                contactIndex = c;
                            } else {
                                contactIndex = "#";
                            }
                        } else {
                            contactIndex = "#";
                        }

                        if (newPerson.getSectionId() == 0) {
                            newPerson.setSectionId(((RAContactSection) sections.get(contactIndex)).getId());
                        }
                        if (newPerson.getAbuserid() == null) {
                            newPerson.setAbuserid(id);
                        }
                        allContacts.putIfAbsent(newPerson.getAbuserid(),newPerson);
                    }
                }

                if(insertContacts.size()>0){
                    RACoreDataManager.getInstance().getDaoSession().getRAContactDao().insertInTx(insertContacts);
                }
                if(updateContacts.size()>0){
                    RACoreDataManager.getInstance().getDaoSession().getRAContactDao().updateInTx(updateContacts);
                }

                people.close();
                Enumeration e = contacts.keys();
                while (e.hasMoreElements()) {

                    String key = (String) e.nextElement();
                    if (!allContacts.containsKey(key)) {
                        RAContact p = (RAContact) contacts.get(key);
                        p.delete();
                    }
                }


            }

            people = RoomApplication.getInstance().getApplicationContext().getContentResolver().query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (people.getCount() > 0) {
                while (people.moveToNext()) {
                    String id = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                    String phoneNo = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();
                    String fullname = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).trim();
                    String label = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)).trim();
                    String firstName = fullname;
                    String uniqueIdentifier = id + "_" + label + "_" + phoneNo;

                    if(allContacts.containsKey(id)){
                        RAContact newPerson = (RAContact)allContacts.get(id);
                        if (phones.get(uniqueIdentifier) == null) {
                            RAPhone raPhone = new RAPhone();
                            raPhone.setPhone(phoneNo);
                            raPhone.setLabel(label);
                            raPhone.setRoomappid(RAUtils.formatRoomappID(phoneNo));
                            raPhone.setContactId(newPerson.getId());
                            insertPhones.add(raPhone);

                        }
                    }
                    allPhones.putIfAbsent(uniqueIdentifier, "");
                }
                people.close();
                if(insertPhones.size()>0){
                    RACoreDataManager.getInstance().getDaoSession().getRAPhoneDao().insertInTx(insertPhones);
                }
                Enumeration e = phones.keys();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    if (!allPhones.containsKey(key)) {
                        RAPhone p = (RAPhone) phones.get(key);
                        p.delete();
                    }
                }
            }


            Log.d("Room", "Contact Loaded");

        }catch (Exception ex){

            Log.d("Room", ex.getLocalizedMessage());

        }



    }


}

