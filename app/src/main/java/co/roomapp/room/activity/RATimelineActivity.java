package co.roomapp.room.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import co.roomapp.room.adapter.RARoomTimelineAdapter;
import co.roomapp.room.R;
import co.roomapp.room.adapter.RAMembersListAdapter;
import co.roomapp.room.model.RAMessage;
import co.roomapp.room.model.RARoom;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAConstant.*;
import co.roomapp.room.utils.RAUtils;
import co.roomapp.room.widget.RAInputAccessoryView;

import android.content.Intent;
import android.widget.ImageButton;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.widget.RelativeLayout.LayoutParams;

import co.stickylistheaders.StickyListHeadersListView;

import android.annotation.TargetApi;
import android.widget.Toast;
import co.roomapp.room.model.RAMember;
/*
 * Created by manager on 1/13/15.
 */

public class RATimelineActivity extends Activity implements
        StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    enum RA_PANEL_STATE {
        PANEL_CLOSED,
        PANEL_OPEN_TO_RIGHT
    }

    enum RA_SIDE_VIEW_STATE {
        SIDE_VIEW_OPEN,
        SIDE_VIEW_CLOSED
    }

    private static RATimelineActivity self;

    private ArrayList<RAMessage> messageList;

    private RelativeLayout rootView;
    private RelativeLayout contentLayout;
    private RelativeLayout bgLayout;
    private RelativeLayout blackLayout;
    private RelativeLayout mainContentView;
    private RelativeLayout roomMembersView;
    private ListView m_list;
    private StickyListHeadersListView stickyList;
    private RelativeLayout m_centerPanel;
    private RelativeLayout m_rightPanel;
    private ImageButton btn_postMessage;
    private ImageButton btn_back;
    private ImageButton btn_notification;
    private RARoom room;

    private ArrayList<RA_PANEL_STATE> m_itemsStateList;
    private int m_position;

    private VelocityTracker m_velocityTracker;
    private float m_lastX;
    private float m_orgX;

    private boolean m_horizon_swipping;
    private boolean m_scrolling_started;
    private boolean m_animating;
    private boolean m_side_view_touch_started = false;

    private int m_screenWidth;
    private int m_screenHeight;
    private float scaledDpi;


    private RATimelinePostMediaFragment postMediaFragment;
    private RATimelinePostGalleryFragment postGalleryFragment;
    private RAInputAccessoryView inputAccessoryView;

    private RA_ACTION_BAR_STATE m_action_bar_state;
    private RA_SIDE_VIEW_STATE m_side_view_state;
    private RAMessageAction  m_action;

    private RARoomTimelineAdapter mTimelineAdapter;
    private RAMembersListAdapter mMemberListAdapter;
    private boolean fadeHeader = true;

    ArrayList<RAMember> m_roomMembersList;
    ArrayList<RAMember> m_pendingInvitedGuests;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratimeline);
        self = this;

        room = (RARoom) getIntent().getSerializableExtra("room");
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        blackLayout = (RelativeLayout) findViewById(R.id.blackLayout);
        bgLayout = (RelativeLayout) findViewById(R.id.bgLayout);
        mainContentView = (RelativeLayout)findViewById(R.id.mainContentView);
        roomMembersView = (RelativeLayout)findViewById(R.id.roomMembersView);
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        m_list = (ListView) findViewById(R.id.timelineListView);
        btn_notification = (ImageButton) findViewById(R.id.btnNotification);
        btn_postMessage = (ImageButton) findViewById(R.id.btnPostMessage);
        btn_back = (ImageButton)findViewById(R.id.btnBack);

        blackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m_action_bar_state == RA_ACTION_BAR_STATE.ACTION_BAR_MEDIA_SHOWN)
                    self.hideMediaActionBar();
                else if (m_action_bar_state == RA_ACTION_BAR_STATE.ACTION_BAR_GALLERY_SHOWN)
                    self.hideGalleryActionBar();

            }
        });
        btn_postMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        scaledDpi = dm.density;
        m_screenWidth = dm.widthPixels;
        m_screenHeight = dm.heightPixels;

        m_animating = false;
        inputAccessoryView = null;

        postMediaFragment = (RATimelinePostMediaFragment) getFragmentManager().findFragmentById(R.id.postMediaFragment);
        postGalleryFragment = (RATimelinePostGalleryFragment) getFragmentManager().findFragmentById(R.id.postGalleryFragment);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft.hide(postMediaFragment);
        ft.commit();

        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.setCustomAnimations(R.animator.entry, R.animator.exit);
        ft1.hide(postGalleryFragment);
        ft1.commit();


        m_action_bar_state = RA_ACTION_BAR_STATE.ACTION_BAR_HIDE;
        m_side_view_state = RA_SIDE_VIEW_STATE.SIDE_VIEW_CLOSED;

        reloadData();

    }


    void reloadData() {

        //Get list of messages
        //
        messageList = new ArrayList<RAMessage>();

        for (int i = 0; i < 5; i++) {
            RAMessage message = new RAMessage();
            message.setId(new Long(1));
            message.setRoomJID("12955");

            if(i == 0) {
                message.setType(RAMessageType.RAMessageTypeMultiplePhotoPost.ordinal());
                message.setAttachments(RAUtils.getTemplate(1));
                message.setCommentsCount(false);
                message.setBody("Looks handsome!");
            }
            if(i == 1) {
                message.setType(RAMessageType.RAMessageTypeOnePhotoPost.ordinal());
                message.setAttachments(RAUtils.getTemplate(2));
                message.setCommentsCount(true);
                message.setBody("What a kitty!!! Looking at heaven?");
            }
            if(i == 2)
            {
                message.setType(RAMessageType.RAMessageTypeVideoPost.ordinal());
                message.setAttachments(RAUtils.getTemplate(3));
                message.setCommentsCount(false);
                message.setBody("Hey Guys! Please see the video. You will find me at ... ");
            }
            if(i == 3)
            {
                message.setType(RAMessageType.RAMessageTypeAudioPost.ordinal());
                message.setAttachments(RAUtils.getTemplate(4));
                message.setCommentsCount(true);
                message.setBody("My voice");
            }
            if(i == 4)
            {
                message.setType(RAMessageType.RAMessageTypeRegularPost.ordinal());
                message.setCommentsCount(true);
                message.setBody("Hi Is there anyone to talk with me? I'm 25/m and looking for somewhere ...");
            }
            messageList.add(message);
        }

        if (messageList.size() > 0) {
            contentLayout.setVisibility(View.VISIBLE);

            m_itemsStateList = new ArrayList<RA_PANEL_STATE>();
            for (int i = 0; i < messageList.size(); i++)
                m_itemsStateList.add(RA_PANEL_STATE.PANEL_CLOSED);

            mTimelineAdapter = new RARoomTimelineAdapter(this, messageList);
            m_list.setAdapter(mTimelineAdapter);
            m_list.setSelection(0);

            m_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (scrollState == 0) {
                        btn_postMessage.setVisibility(View.VISIBLE);
                        Animation myFadeInAnimation1 = AnimationUtils.loadAnimation(self, R.anim.fadein);
                        btn_postMessage.startAnimation(myFadeInAnimation1); //Set animation to your ImageView
                    } else {
                        btn_postMessage.setVisibility(View.GONE);
                        hideInputAccessoryView();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });

            GestureDetector.OnGestureListener gl;
            gl = new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {

                    btn_postMessage.setVisibility(View.GONE);
                    return true;
                }
            };

            final GestureDetector gd = new GestureDetector(this, gl);

            m_list.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent e) {

                    if (m_animating)
                        return false;

                    if (e.getAction() == MotionEvent.ACTION_DOWN) {

                        if(m_side_view_state == RA_SIDE_VIEW_STATE.SIDE_VIEW_CLOSED)
                        {

                            m_position = RAUtils.getListItemPositionFromPoint(e.getRawX(), e.getRawY(), m_list);

                            if (m_position < 1)
                                return false;

                            View itemView = RAUtils.getViewByPosition(m_position, m_list);

                            m_centerPanel = (RelativeLayout) itemView.findViewById(R.id.timelineLayout);
                            m_rightPanel = (RelativeLayout) itemView.findViewById(R.id.rightLayout);

                            if (m_velocityTracker == null)
                                m_velocityTracker = VelocityTracker.obtain();
                            else
                                m_velocityTracker.clear();
                            m_velocityTracker.addMovement(e);
                            m_horizon_swipping = false;
                            m_scrolling_started = false;
                            m_lastX = e.getX();
                            m_orgX = m_centerPanel.getX();
                        }
                    }
                    else if (e.getAction() == MotionEvent.ACTION_MOVE) {

                        if(m_side_view_state == RA_SIDE_VIEW_STATE.SIDE_VIEW_CLOSED) {
                            if (m_position < 1)
                                return false;

                            if (m_velocityTracker == null)
                                m_velocityTracker = VelocityTracker.obtain();

                            m_velocityTracker.addMovement(e);
                            m_velocityTracker.computeCurrentVelocity(1000);

                            float xSpeed = Math.abs(m_velocityTracker.getXVelocity());
                            float ySpeed = Math.abs(m_velocityTracker.getYVelocity());

                            if (!m_horizon_swipping) {
                                if (!m_scrolling_started) {
                                    if ((xSpeed > RAConstant.gesture_filter_value) || (ySpeed > RAConstant.gesture_filter_value)) {
                                        if (xSpeed > ySpeed) {
                                            m_horizon_swipping = true; //Start horizontal swipping on item view
                                            return true;
                                        } else {
                                            m_scrolling_started = true;
                                            return false; //Vertical scrolling
                                        }
                                    }

                                } else
                                    return false; //Vertical scrolling
                            } else {
                                //Move the panel on item view
                                if (xSpeed > ySpeed) {
                                    float currentX = e.getX();
                                    float offset = currentX - m_lastX;
                                    m_centerPanel.setX(m_orgX + offset);
                                    if (m_centerPanel.getX() < 5) {
                                        m_rightPanel.setVisibility(View.VISIBLE);
                                    } else {
                                        m_rightPanel.setVisibility(View.GONE);
                                    }
                                }
                                return true;
                            }
                        }

                    } else if ((e.getAction() == MotionEvent.ACTION_UP) && (m_horizon_swipping)) {

                        if(m_side_view_state == RA_SIDE_VIEW_STATE.SIDE_VIEW_CLOSED) {
                            m_scrolling_started = false;

                            if (m_velocityTracker == null)
                                return false;

                            m_velocityTracker.addMovement(e);
                            m_velocityTracker.computeCurrentVelocity(1000);
                            float xSpeed = m_velocityTracker.getXVelocity();
                            float ySpeed = m_velocityTracker.getYVelocity();

                            if (Math.abs(xSpeed) < Math.abs(ySpeed)) {
                                ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                ani.setDuration(RAConstant.duration_without_velocity);
                                ani.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        m_animating = true;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        m_horizon_swipping = false;
                                        RA_PANEL_STATE state = m_itemsStateList.get(m_position - 1);
                                        if (state == RA_PANEL_STATE.PANEL_CLOSED) {
                                            m_rightPanel.setVisibility(View.GONE);
                                        } else if (state == RA_PANEL_STATE.PANEL_OPEN_TO_RIGHT) {
                                            m_rightPanel.setVisibility(View.VISIBLE);
                                        }
                                        m_animating = false;
                                    }
                                });

                                ani.start();
                            } else {
                                float dropX = m_centerPanel.getX();

                                if (m_itemsStateList.get(m_position - 1) == RA_PANEL_STATE.PANEL_CLOSED) {


                                    if ((dropX < -50) || (xSpeed < -RAConstant.swipe_filter_value)) {
                                        if (xSpeed > RAConstant.swipe_filter_value) {
                                            ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                            ani.setDuration(RAConstant.duration_without_velocity);
                                            ani.addListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {
                                                    m_animating = true;
                                                }


                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    m_horizon_swipping = false;
                                                    m_rightPanel.setVisibility(View.GONE);
                                                    m_animating = false;
                                                }
                                            });

                                            ani.start();

                                        } else {
                                            if (dropX < -50) {
                                                ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", -120 * scaledDpi);

                                                if (xSpeed > -RAConstant.swip_standard_velocity)
                                                    xSpeed = -RAConstant.swip_standard_velocity;
                                                else if (xSpeed < -RAConstant.swip_standard_velocity)
                                                    xSpeed = xSpeed / 2 - (float) (Math.sqrt(-xSpeed));

                                                ani.setDuration((long) (RAConstant.numerator_duration_with_velocity / Math.sqrt((-xSpeed / RAConstant.swip_standard_velocity))));
                                                ani.addListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {
                                                        m_animating = true;
                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
//                                        self.m_rightPanel.setVisibility(View.VISIBLE);
//                                        self.m_leftPanel.setVisibility(View.INVISIBLE);
                                                        m_itemsStateList.set(self.m_position - 1, RA_PANEL_STATE.PANEL_OPEN_TO_RIGHT);
                                                        m_rightPanel.setVisibility(View.VISIBLE);
                                                        m_horizon_swipping = false;
                                                        m_animating = false;
                                                    }
                                                });

                                                ani.start();
                                            } else {
                                                ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                                ani.setDuration(RAConstant.duration_without_velocity);
                                                ani.addListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {
                                                        m_animating = true;
                                                    }


                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        m_horizon_swipping = false;
                                                        m_rightPanel.setVisibility(View.GONE);
                                                        m_animating = false;
                                                    }
                                                });

                                                ani.start();

                                            }

                                        }

                                    } else {
                                        ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                        ani.setDuration(RAConstant.duration_without_velocity);
                                        ani.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                m_animating = true;
                                            }


                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                m_horizon_swipping = false;
                                                m_rightPanel.setVisibility(View.GONE);
                                                m_animating = false;
                                            }
                                        });

                                        ani.start();


                                    }

                                } else if (m_itemsStateList.get(m_position - 1) == RA_PANEL_STATE.PANEL_OPEN_TO_RIGHT) {
                                    if ((dropX > -90) || (xSpeed > RAConstant.swipe_filter_value)) {
                                        ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", 0 * scaledDpi);

                                        if (xSpeed < RAConstant.swip_standard_velocity)
                                            xSpeed = RAConstant.swip_standard_velocity;
                                        else if (xSpeed > RAConstant.swip_standard_velocity)
                                            xSpeed = xSpeed / 2 + (float) (Math.sqrt(xSpeed));

                                        ani.setDuration((long) (RAConstant.numerator_duration_with_velocity / Math.sqrt((xSpeed / RAConstant.swip_standard_velocity))));
                                        ani.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                m_animating = true;
                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                m_itemsStateList.set(self.m_position - 1, RA_PANEL_STATE.PANEL_CLOSED);
                                                m_horizon_swipping = false;
                                                m_rightPanel.setVisibility(View.GONE);
                                                m_animating = false;
                                            }
                                        });
                                        ani.start();
                                    } else {
                                        ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                        ani.setDuration(RAConstant.duration_without_velocity);
                                        ani.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                m_animating = true;
                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                m_horizon_swipping = false;
                                                m_animating = false;
                                            }
                                        });
                                        ani.start();
                                    }
                                }
                                m_velocityTracker.recycle();
                                m_velocityTracker = null;
                            }
                        }

                    }

                    gd.onTouchEvent(e);
                    // ... handle TOUCH_UP and TOUCH_CANCEL
                    return false;
                }
            });


            m_list.setSelection(0);
        } else {
            contentLayout.setVisibility(View.GONE);
        }
    }

    public void settingTimeline(int position) {
        System.out.println("Setting timeline");
    }

    public void deleteTimeline(int position) {
        System.out.println("Delete timeline");
    }

    public void allMedias() {

        Intent intent = new Intent(RATimelineActivity.this, RAMediaGalleryActivity.class);
        intent.putExtra("room", room);
        startActivity(intent);
    }

    public void allGuests() {

        //Get the members of room
        m_roomMembersList = new ArrayList<RAMember>();
        for(int i = 0; i < 10; i ++)
        {
            RAMember member = new RAMember();
            member.setDescription("He is top app user");
            member.setUsername("yang");
            member.setFullname("Yang Guo");
            member.setMemberJID("abc");
            if(i % 2 == 0) {
                member.setStatus("offline");
            }
            else {
                member.setStatus("online");
            }
            m_roomMembersList.add(member);
        }

        //Get the guests invited by admin
        m_pendingInvitedGuests = new ArrayList<RAMember>();
        for(int i = 0; i < 5; i ++)
        {
            RAMember member = new RAMember();
            member.setDescription("my friend");
            member.setUsername("zhang");
            member.setFullname("Zhang");
            m_pendingInvitedGuests.add(member);
        }

        mMemberListAdapter = new RAMembersListAdapter(this, m_roomMembersList, m_pendingInvitedGuests);

        stickyList = (StickyListHeadersListView) findViewById(R.id.membersListView);
//        stickyList.setOnItemClickListener(this);
        stickyList.setOnHeaderClickListener(this);
        stickyList.setOnStickyHeaderChangedListener(this);
        stickyList.setOnStickyHeaderOffsetChangedListener(this);
        stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.member_list_header, null));
        stickyList.addFooterView(getLayoutInflater().inflate(R.layout.member_list_footer, null));
//        stickyList.setEmptyView(findViewById(R.id.empty));
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        stickyList.setAdapter(mMemberListAdapter);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object selectedItem = mMemberListAdapter.getItem(position-1);
                if(selectedItem != null)
                {
                    if(selectedItem.getClass() == RAMember.class)
                    {
                        RAMember selectedMemeber = (RAMember)selectedItem;
                        viewUser(selectedMemeber);
                    }
                }
            }
        });

        roomMembersView.setVisibility(View.VISIBLE);
        roomMembersView.getLayoutParams().width = (int)(m_screenWidth - 60 * scaledDpi);
        roomMembersView.requestLayout();
        ObjectAnimator ani = ObjectAnimator.ofFloat(mainContentView, "X", -m_screenWidth + 60 * scaledDpi);
        ani.setDuration(RAConstant.duration_without_velocity);
        ani.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                m_side_view_state = RA_SIDE_VIEW_STATE.SIDE_VIEW_OPEN;
            }
        });
        ani.start();

        btn_notification.setClickable(false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (m_side_view_state == RA_SIDE_VIEW_STATE.SIDE_VIEW_OPEN) {
                        if (!m_animating) {
                            if (m_velocityTracker == null)
                                m_velocityTracker = VelocityTracker.obtain();
                            else
                                m_velocityTracker.clear();
                            m_velocityTracker.addMovement(event);
                            m_lastX = event.getX();
                            m_orgX = mainContentView.getX();
                            m_side_view_touch_started = true;
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (m_side_view_touch_started) {
                        if (m_velocityTracker == null)
                            m_velocityTracker = VelocityTracker.obtain();
                        m_velocityTracker.addMovement(event);
                        m_velocityTracker.computeCurrentVelocity(1000);
                        float currentX = event.getX();
                        System.out.println("Current X : " + currentX);
                        float offset = currentX - m_lastX;
                        mainContentView.setX(m_orgX + offset);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    m_velocityTracker.addMovement(event);
                    m_velocityTracker.computeCurrentVelocity(1000);
                    float xSpeed = m_velocityTracker.getXVelocity();
                    ObjectAnimator ani = ObjectAnimator.ofFloat(mainContentView, "X", 0 * scaledDpi);

                    if (xSpeed < RAConstant.swip_standard_velocity)
                        xSpeed = RAConstant.swip_standard_velocity;
                    else if (xSpeed > RAConstant.swip_standard_velocity)
                        xSpeed = xSpeed / 2 + (float) (Math.sqrt(xSpeed));

                    ani.setDuration((long) (RAConstant.numerator_duration_with_velocity / Math.sqrt((xSpeed / RAConstant.swip_standard_velocity))));
                    ani.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            m_animating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            m_side_view_state = RA_SIDE_VIEW_STATE.SIDE_VIEW_CLOSED;
                            m_animating = false;
                            m_side_view_touch_started = false;
                            btn_notification.setClickable(true);
                        }
                    });
                    ani.start();
                }
                return true;
            }
        });

    }

    void postMessage() {

        if (inputAccessoryView == null) {
            inputAccessoryView = new RAInputAccessoryView(this);
            inputAccessoryView.setDelegate(this);
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.bgLayout);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layout.addView(inputAccessoryView, layoutParams);
            postMediaFragment.setParent(inputAccessoryView);
            postGalleryFragment.setParent(inputAccessoryView);
        }
        else
            inputAccessoryView.setVisibility(View.VISIBLE);
    }

    public void hideInputAccessoryView()
    {
        if(inputAccessoryView != null)
            inputAccessoryView.setVisibility(View.GONE);
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
        LayoutParams layoutParams = (RelativeLayout.LayoutParams) inputAccessoryView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        inputAccessoryView.setLayoutParams(layoutParams);
    }

    public void moveInputAccessoryViewByYPos(float yPos, final int trigger) {
        ObjectAnimator ani = ObjectAnimator.ofFloat(inputAccessoryView, "Y", yPos);
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

        inputAccessoryView.onActivityResult(requestCode, resultCode, data);
    }

    public void doTestByAdding(RAMessage message)
    {

    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        Toast.makeText(this, "Header " + headerId + " currentlySticky ? " + currentlySticky, Toast.LENGTH_SHORT).show();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (fadeHeader && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(1);
    }

    public void searchMembersByQuery(String query)
    {
        ArrayList<RAMember> filteredMemberList = new ArrayList<RAMember>();
        ArrayList<RAMember> filteredInvitedList = new ArrayList<RAMember>();
        for(int i = 0; i < m_roomMembersList.size(); i ++)
        {
            RAMember member = m_roomMembersList.get(i);
            String name = member.getUsername();
            if(name.contains(query))
            {
                filteredMemberList.add(member);
            }
        }
        for(int i = 0; i < m_pendingInvitedGuests.size(); i ++)
        {
            RAMember member = m_pendingInvitedGuests.get(i);
            String name = member.getUsername();
            if(name.contains(query))
            {
                filteredInvitedList.add(member);
            }
        }
        if(mMemberListAdapter!=null)
        {
            mMemberListAdapter.setMembers(filteredMemberList, filteredInvitedList);
        }
    }

    public void viewUser(RAMember member)
    {
        Intent intent = new Intent(RATimelineActivity.this, RAChatActivity.class);
        intent.putExtra("member", member);
        startActivity(intent);
    }
}
