package co.roomapp.room.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ImageButton;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import co.roomapp.room.R;
import co.roomapp.room.model.RAMember;
import co.roomapp.room.model.RAMessage;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAUtils;
import co.roomapp.room.widget.RAInputAccessoryView;
import co.roomapp.room.utils.RAConstant.*;
import android.widget.RelativeLayout;
import android.widget.ListView;
import co.roomapp.room.model.RAChatSession;
import co.roomapp.room.adapter.RAChatSessionListAdapter;

/**
 * Created by luoqi on 2/20/2015.
 */
public class RAChatActivity extends Activity implements View.OnClickListener{

    private static RAChatActivity self;

    private RAMember m_user;
    private RAChatSession m_chatSession;

    private TextView m_nameLabel;
    private TextView m_status;
    private RelativeLayout bgLayout;
    private RelativeLayout blackLayout;
    private RelativeLayout conversationLayout;
    private ListView chatListView;
    private ImageButton btnBack;
    private ImageButton btnMedia;
    private RAInputAccessoryView m_inputAccessoryView;

    private RATimelinePostMediaFragment postMediaFragment;
    private RATimelinePostGalleryFragment postGalleryFragment;

    private RA_ACTION_BAR_STATE m_action_bar_state;

    private int m_screenWidth;
    private int m_screenHeight;
    private float scaledDpi;
    private boolean fadeHeader = true;
    private RAChatSessionListAdapter mAdapter;

    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_rachat);
        self = this;

        bgLayout = (RelativeLayout)findViewById(R.id.bgLayout);
        blackLayout = (RelativeLayout)findViewById(R.id.blackLayout);
        conversationLayout = (RelativeLayout)findViewById(R.id.conversationLayout);
        chatListView = (ListView)findViewById(R.id.conversationList);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnMedia = (ImageButton)findViewById(R.id.btnMedia);
        m_nameLabel = (TextView)findViewById(R.id.nameLabel);
        m_status = (TextView)findViewById(R.id.statusLabel);
        m_inputAccessoryView = (RAInputAccessoryView)findViewById(R.id.postMenu);
        m_inputAccessoryView.setDelegate(this);
        m_inputAccessoryView.hideKeyboard();


        btnBack.setOnClickListener(this);
        btnMedia.setOnClickListener(this);
        blackLayout.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        scaledDpi = dm.density;
        m_screenWidth = dm.widthPixels;
        m_screenHeight = dm.heightPixels;

        postMediaFragment = (RATimelinePostMediaFragment) getFragmentManager().findFragmentById(R.id.postMediaFragment);
        postGalleryFragment = (RATimelinePostGalleryFragment) getFragmentManager().findFragmentById(R.id.postGalleryFragment);
        postMediaFragment.setParent(m_inputAccessoryView);
        postGalleryFragment.setParent(m_inputAccessoryView);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft.hide(postMediaFragment);
        ft.commit();

        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft1.hide(postGalleryFragment);
        ft1.commit();

        m_action_bar_state = RA_ACTION_BAR_STATE.ACTION_BAR_HIDE;


        List<RAMessage> msgList = new List<RAMessage>() {

            @Override public void add(int location, RAMessage object) {}

            @Override public boolean add(RAMessage object) { return true; }

            @Override public boolean addAll(int location, Collection<? extends RAMessage> collection) { return false; }

            @Override public boolean addAll(Collection<? extends RAMessage> collection) { return false; }

            @Override public void clear() { }

            @Override public boolean contains(Object object) { return false; }

            @Override public boolean containsAll(Collection<?> collection) { return false; }

            @Override public RAMessage get(int location) {

                RAMessage message = new RAMessage();
                message.setId(new Long(1));
                message.setRoomJID("12955");
                if(location == 0) {
                    message.setType(RAMessageType.RAMessageTypeMultiplePhotoPost.ordinal());
                    message.setAttachments(RAUtils.getTemplate(1));
                    message.setCommentsCount(false);
                    message.setBody("Looks handsome!");

                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault());
                    try {
                        Date msgDate = dateFormat.parse("2/12/2015 14:12");
                        message.setMessagedate(msgDate);
                    }
                    catch(ParseException e) {
                        e.printStackTrace();
                    }

                }
                if(location == 1) {
                    message.setType(RAMessageType.RAMessageTypeOnePhotoPost.ordinal());
                    message.setAttachments(RAUtils.getTemplate(2));
                    message.setCommentsCount(true);
                    message.setBody("What a kitty!!! Looking at heaven?");
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault());
                    try {
                        Date msgDate = dateFormat.parse("2/12/2015 14:15");
                        message.setMessagedate(msgDate);
                    }
                    catch(ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(location == 2)
                {
                    message.setType(RAMessageType.RAMessageTypeVideoPost.ordinal());
                    message.setAttachments(RAUtils.getTemplate(3));
                    message.setCommentsCount(false);
                    message.setBody("Hey Guys! Please see the video. You will find me at ... ");
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault());
                    try {
                        Date msgDate = dateFormat.parse("2/12/2015 15:10");
                        message.setMessagedate(msgDate);
                    }
                    catch(ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(location == 3)
                {
                    message.setType(RAMessageType.RAMessageTypeAudioPost.ordinal());
                    message.setAttachments(RAUtils.getTemplate(4));
                    message.setCommentsCount(true);
                    message.setBody("My voice");
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault());
                    try {
                        Date msgDate = dateFormat.parse("2/12/2015 16:12");
                        message.setMessagedate(msgDate);
                    }
                    catch(ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(location == 4)
                {
                    message.setType(RAMessageType.RAMessageTypeRegularPost.ordinal());
                    message.setCommentsCount(true);
                    message.setBody("Hi Is there anyone to talk with me? I'm 25/m and looking for somewhere ...");
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault());
                    try {
                        Date msgDate = dateFormat.parse("2/23/2015 16:20");
                        message.setMessagedate(msgDate);
                    }
                    catch(ParseException e) {
                        e.printStackTrace();
                    }
                }

                if(location % 2 == 0)
                    message.setFromJID("AAA");
                else
                    message.setFromJID("BBB");
                message.setIsDelivered(true);
                message.setIsRead(true);
                return message;
            }

            @Override public int indexOf(Object object) { return 0; }

            @Override public boolean isEmpty() { return false; }

            @NonNull
            @Override public Iterator<RAMessage> iterator() { return null; }

            @Override public int lastIndexOf(Object object) { return 0; }

            @NonNull
            @Override public ListIterator<RAMessage> listIterator() { return null; }

            @NonNull
            @Override public ListIterator<RAMessage> listIterator(int location) { return null; }

            @Override public RAMessage remove(int location) { return null; }

            @Override public boolean remove(Object object) { return false; }

            @Override public boolean removeAll(Collection<?> collection) { return false; }

            @Override public boolean retainAll(Collection<?> collection) { return false; }

            @Override public RAMessage set(int location, RAMessage object) { return null; }

            @Override public int size() { return 5; }

            @NonNull
            @Override public List<RAMessage> subList(int start, int end) { return null; }

            @NonNull
            @Override public Object[] toArray() { return new Object[0]; }

            @NonNull
            @Override public <T> T[] toArray(T[] array) { return null; }
        };


        //RAChatSession chatSession = [RAChatSession chatSEssionWithMember: m];
        final RAChatSession chatSession = new RAChatSession();
        chatSession.setContactJID("AAA");
        chatSession.setMessages(msgList);

        mAdapter = new RAChatSessionListAdapter(this, chatSession);
        chatListView.setAdapter(mAdapter);
        chatListView.setSelection(chatSession.getMessages().size());

        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("member");
        RAMember member = (RAMember)serializable;

        //name = [RAPhone getPhoneName: [[XMPPJID jidWithString: self.chatSession.contactJID] user] withDefault:self.chatSession.partnerName]];
        m_status.setText(member.getStatus());
        m_nameLabel.setText(member.getFullname());

        //reloadData();
    }

    @Override
    public void onClick(View v)
    {
        if(v.getClass() == ImageButton.class)
        {
            if((ImageButton)v == btnBack)
            {
                finish();
            }
            else if((ImageButton)v == btnMedia)
            {
                Intent intent = new Intent(RAChatActivity.this, RAMediaGalleryActivity.class);
                //get attachments list from chat session
                startActivity(intent);
            }
        }
        else if(v.getClass() == RelativeLayout.class)
        {
            if((RelativeLayout)v == blackLayout)
            {
                if (m_action_bar_state == RA_ACTION_BAR_STATE.ACTION_BAR_MEDIA_SHOWN)
                    self.hideMediaActionBar();
                else if (m_action_bar_state == RA_ACTION_BAR_STATE.ACTION_BAR_GALLERY_SHOWN)
                    self.hideGalleryActionBar();
            }
        }
    }


    public void showActionBar(int type) {
        if (type == 0) {
            blackLayout.setVisibility(View.VISIBLE);
            Animation myFadeInAnimation1 = AnimationUtils.loadAnimation(self, R.anim.lightfadein);
            blackLayout.startAnimation(myFadeInAnimation1); //Set animation to your ImageView
            bgLayout.setEnabled(false);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.entry, R.animator.exit);
            ft.show(postMediaFragment);
            ft.commit();

            m_action_bar_state = RA_ACTION_BAR_STATE.ACTION_BAR_MEDIA_SHOWN;

        } else if (type == 1) {
            blackLayout.setVisibility(View.VISIBLE);
            Animation myFadeInAnimation1 = AnimationUtils.loadAnimation(self, R.anim.lightfadein);
            blackLayout.startAnimation(myFadeInAnimation1); //Set animation to your ImageView

            bgLayout.setEnabled(false);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.entry, R.animator.exit);
            ft.show(postGalleryFragment);
            ft.commit();

            m_action_bar_state = RA_ACTION_BAR_STATE.ACTION_BAR_GALLERY_SHOWN;

        }
    }

    public void hideMediaActionBar() {
        if (m_action_bar_state == RA_ACTION_BAR_STATE.ACTION_BAR_MEDIA_SHOWN) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.entry, R.animator.exit);
            ft.hide(postMediaFragment);
            ft.commit();
        }
        m_action_bar_state = RA_ACTION_BAR_STATE.ACTION_BAR_HIDE;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.lightfadeout);
        blackLayout.startAnimation(animation);
        blackLayout.setVisibility(View.GONE);
        bgLayout.setEnabled(true);
    }

    public void hideGalleryActionBar() {
        if (m_action_bar_state == RA_ACTION_BAR_STATE.ACTION_BAR_GALLERY_SHOWN) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.entry, R.animator.exit);
            ft.hide(postGalleryFragment);
            ft.commit();
        }
        m_action_bar_state = RA_ACTION_BAR_STATE.ACTION_BAR_HIDE;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.lightfadeout);
        blackLayout.startAnimation(animation);
        blackLayout.setVisibility(View.GONE);
        bgLayout.setEnabled(true);
    }


    public void moveInputAccessoryViewToBottom() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) m_inputAccessoryView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        m_inputAccessoryView.setLayoutParams(layoutParams);
    }

    public void moveInputAccessoryViewByYPos(float yPos, final int trigger) {
        ObjectAnimator ani = ObjectAnimator.ofFloat(m_inputAccessoryView, "Y", yPos);
        ani.setDuration(RAConstant.duration_without_velocity);
        ani.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (trigger == 0) {

                }
            }
        });
        ani.start();
    }

    public RA_ACTION_BAR_STATE getActionBarState() {
        return m_action_bar_state;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        m_inputAccessoryView.onActivityResult(requestCode, resultCode, data);
    }

}
