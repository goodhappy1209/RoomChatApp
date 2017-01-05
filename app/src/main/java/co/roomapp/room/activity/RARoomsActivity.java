package co.roomapp.room.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.View;
import android.view.View.DragShadowBuilder;
import com.pkmmte.view.CircularImageView;

import java.text.MessageFormat;

import co.roomapp.room.R;
import co.roomapp.room.model.RAMessage;
import co.roomapp.room.model.RARoom;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAObservingService;
import co.roomapp.room.utils.RAUtils;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import co.roomapp.room.adapter.RARoomListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Point;
import android.graphics.Canvas;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
/**
 * Created by manager on 1/3/15.
 */




public class RARoomsActivity extends Activity implements TextWatcher, Observer{

    enum RA_PANEL_STATE
    {
        PANEL_CLOSED,
        PANEL_OPEN_TO_LEFT,
        PANEL_OPEN_TO_RIGHT
    }

    private CircularImageView m_avatar;
    private TextView m_userName;
    private RelativeLayout m_content1;
    private RelativeLayout m_content11;
    private ArrayList<RARoom> m_roomsList;
    private ArrayList<RA_PANEL_STATE> m_itemsStateList;

    private ListView m_list;
    private RARoomListAdapter m_adapter;
    private int m_position;

    private SearchView searchView;

    private VelocityTracker m_velocityTracker;
    private float m_lastX;
    private float m_orgX;

    private double m_distance;
    private float m_lastX1;
    private float m_lastY1;

    private int m_screenWidth;
    private int m_screenHeight;

    private RelativeLayout m_centerPanel;
    private RelativeLayout m_leftPanel;
    private RelativeLayout m_rightPanel;

    private static RARoomsActivity self;
    private boolean m_horizon_swipping;
    private boolean m_scrolling_started;

    private float scaledDpi;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setupNotifications();

        setContentView(R.layout.activity_rarooms);

        self = this;

        m_content1 = (RelativeLayout)findViewById(R.id.content1);
        m_content11 = (RelativeLayout)findViewById(R.id.content11);



        m_avatar = (CircularImageView)findViewById(R.id.imgPhoto);
        m_avatar.setImageBitmap(RAUtils.loadProfileImageURL("jpg"));
        m_avatar.setBorderColor(Color.WHITE);
        m_avatar.setBorderWidth(1);

        m_userName = (TextView)findViewById(R.id.heyLabel);
        m_userName.setText(MessageFormat.format(getString(R.string.hey_name), RAUtils.decryptObjectForKey(RAConstant.kRAOwnFirstName)));

        m_list=(ListView)findViewById(R.id.listView);
        m_horizon_swipping = false;

        reloadData();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        scaledDpi = dm.density;


    }

    public void setupNotifications(){
        RAObservingService.getInstance().addObserver(RAConstant.kReloadRoomObserver,this);
        RAObservingService.getInstance().addObserver(RAConstant.kSyncRoomObserver,this);
        RAObservingService.getInstance().addObserver(RAConstant.kReloadListRoomNotification,this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        m_screenWidth = dm.widthPixels;
        m_screenHeight = dm.heightPixels;


    }


    public void searchRooms(String roomName)
    {
        System.out.println(roomName);
        //Filter rooms by room name

    }

    public void exploreRooms()
    {
        RATabBarActivity.getInstance().setCurrentTab(2);
    }

    public void joinRoom()
    {

    }

    public void joinAtRoom(int index)
    {
        RARoom room = m_roomsList.get(index-1);
        Intent intent = new Intent(this, RATimelineActivity.class);
        intent.putExtra("room", room);
        startActivity(intent);
    }

    void reloadData()
    {

        //Get list of rooms
        //
//        m_roomsList = RARoom.getListRooms();

        m_roomsList = new ArrayList<RARoom>();

        for(int i = 0; i < 6; i ++)
        {

            RARoom room = new RARoom();
            room.setId(new Long(1));
            room.setRoomkey("hahaha");
            room.setRoomJID("12955");
            room.setOwnerDisplayName("Damien");
            room.setName("Welcome Room");
            room.setMembersCount(new Long(1000));
            room.setCoverURL("http://ts4.mm.bing.net/th?id=HN.607993839974744283&pid=1.7");

//            RAMessage message = new RAMessage();
//            message.setRoomJID("12955");
//            message.setRoomId(new Long(1));
//            message.setBody("That sounds interesting. Move on please");
//            room.setLastMessage(message);

            m_roomsList.add(room);
        }


        if(m_roomsList.size() > 0)
        {
            m_content1.setVisibility(View.GONE);
            m_content11.setVisibility(View.VISIBLE);

            m_itemsStateList = new ArrayList<RA_PANEL_STATE>();
            for(int i = 0; i < m_roomsList.size(); i ++)
                m_itemsStateList.add(RA_PANEL_STATE.PANEL_CLOSED);

            m_adapter=new RARoomListAdapter(this, m_roomsList);
            m_list.setAdapter(m_adapter);


            m_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if(scrollState == 0) {

                        int offset = computeScrollOffset(m_list);
                        if(offset < 70)
                            m_list.smoothScrollToPosition(0);
                        else if(offset < 100) {
                            m_list.setSelection(1);
                        }
                    }
                    else
                    {
                        int offset = computeScrollOffset(m_list);
                        if(offset < 70)
                            m_list.setSelection(0);
                        else if(offset < 100) {
                            m_list.setSelection(1);
                        }
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

                    return true;
                }
            };

            final GestureDetector gd = new GestureDetector(this, gl);

            m_list.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent e) {


                    if(e.getAction() == MotionEvent.ACTION_DOWN)
                    {

                        m_position = RAUtils.getListItemPositionFromPoint(e.getRawX(), e.getRawY(), m_list);

                        if(m_position < 2)
                            return false;

                        View itemView = RAUtils.getViewByPosition(m_position, m_list);

                        m_centerPanel = (RelativeLayout)itemView.findViewById(R.id.centerPanel);
                        m_leftPanel = (RelativeLayout)itemView.findViewById(R.id.leftPanel);
                        m_rightPanel = (RelativeLayout)itemView.findViewById(R.id.rightPanel);

                        if(m_velocityTracker == null)
                            m_velocityTracker = VelocityTracker.obtain();
                        else
                            m_velocityTracker.clear();
                        m_velocityTracker.addMovement(e);

                        m_horizon_swipping = false;
                        m_scrolling_started = false;
                        m_lastX = e.getX();
                        m_lastX1 = e.getX();
                        m_lastY1 = e.getY();
                        m_orgX = m_centerPanel.getX();
                        m_distance = 0;
                    }
                    else if(e.getAction() == MotionEvent.ACTION_MOVE)
                    {
                        
                        if(m_position < 2)
                            return false;
                        
                        if(m_velocityTracker == null)
                            m_velocityTracker = VelocityTracker.obtain();

                        m_velocityTracker.addMovement(e);
                        m_velocityTracker.computeCurrentVelocity(1000);

                        float curX = e.getX();
                        float curY = e.getY();
                        double dist = Math.sqrt((curX - m_lastX1)*(curX - m_lastX1) + (curY - m_lastY1)*(curY-m_lastY1));
                        m_distance += dist;
                        m_lastX1 = curX;
                        m_lastY1 = curY;

                        float xSpeed = Math.abs(m_velocityTracker.getXVelocity());
                        float ySpeed = Math.abs(m_velocityTracker.getYVelocity());

                        if(!m_horizon_swipping)
                        {
                            if(!m_scrolling_started)
                            {
                                if((xSpeed > RAConstant.gesture_filter_value)||(ySpeed > RAConstant.gesture_filter_value))
                                {
                                    if(xSpeed > ySpeed) {
                                        m_horizon_swipping = true; //Start horizontal swipping on item view
                                        return true;
                                    }
                                    else {
                                        m_scrolling_started = true;
                                        return false; //Vertical scrolling
                                    }
                                }

                            }
                            else
                                return false; //Vertical scrolling
                        }
                        else
                        {
                            //Move the panel on item view
                            if(xSpeed > ySpeed) {
                                System.out.println("Dragging");
                                float currentX = e.getX();
                                float offset = currentX - m_lastX;
                                m_centerPanel.setX(m_orgX + offset);
                                if (m_centerPanel.getX() < 5) {
                                    m_rightPanel.setVisibility(View.VISIBLE);
                                    m_leftPanel.setVisibility(View.GONE);
                                } else {
                                    m_rightPanel.setVisibility(View.GONE);
                                    m_leftPanel.setVisibility(View.VISIBLE);
                                }
                            }
                            return true;
                        }



                    }
                    else if(e.getAction() ==MotionEvent.ACTION_UP)
                    {

                        if(m_horizon_swipping)
                        {
                            m_scrolling_started= false;

                            if(m_velocityTracker==null)
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

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        m_horizon_swipping = false;
                                        RA_PANEL_STATE state = m_itemsStateList.get(m_position - 2);
                                        if (state == RA_PANEL_STATE.PANEL_CLOSED) {
                                            m_leftPanel.setVisibility(View.GONE);
                                            m_rightPanel.setVisibility(View.GONE);
                                        } else if (state == RA_PANEL_STATE.PANEL_OPEN_TO_LEFT) {
                                            m_leftPanel.setVisibility(View.VISIBLE);
                                            m_rightPanel.setVisibility(View.GONE);
                                        } else if (state == RA_PANEL_STATE.PANEL_OPEN_TO_RIGHT) {
                                            m_leftPanel.setVisibility(View.GONE);
                                            m_rightPanel.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });

                                ani.start();
                            } else {
                                float dropX = m_centerPanel.getX();

                                if (m_itemsStateList.get(m_position - 2) == RA_PANEL_STATE.PANEL_CLOSED) {


                                    if ((dropX > 100) || (xSpeed > RAConstant.swipe_filter_value)) {

                                        if (xSpeed < -RAConstant.swipe_filter_value) {
                                            //Back to orginal position
                                            ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                            ani.setDuration(RAConstant.duration_without_velocity);
                                            ani.addListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {

                                                }


                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    m_horizon_swipping = false;
                                                    m_leftPanel.setVisibility(View.GONE);
                                                    m_rightPanel.setVisibility(View.GONE);
                                                }
                                            });

                                            ani.start();

                                        } else {
                                            if (dropX > 100) {
                                                //Open panel to left
                                                ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_screenWidth - 60 * scaledDpi);

                                                if (xSpeed < RAConstant.swip_standard_velocity)
                                                    xSpeed = RAConstant.swip_standard_velocity;
                                                else if (xSpeed > RAConstant.swip_standard_velocity)
                                                    xSpeed = xSpeed / 2 + (float) (Math.sqrt(xSpeed));

                                                ani.setDuration((long) (RAConstant.numerator_duration_with_velocity / Math.sqrt(xSpeed / RAConstant.swip_standard_velocity)));
                                                ani.addListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        m_itemsStateList.set(self.m_position - 2, RA_PANEL_STATE.PANEL_OPEN_TO_LEFT);
                                                        m_leftPanel.setVisibility(View.VISIBLE);
                                                        m_rightPanel.setVisibility(View.GONE);
                                                        m_horizon_swipping = false;

                                                    }
                                                });

                                                ani.start();
                                            } else {
                                                //Back to original position
                                                ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                                ani.setDuration(RAConstant.duration_without_velocity);
                                                ani.addListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {

                                                    }


                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        m_horizon_swipping = false;
                                                        m_leftPanel.setVisibility(View.GONE);
                                                        m_rightPanel.setVisibility(View.GONE);
                                                    }
                                                });

                                                ani.start();
                                            }

                                        }


                                    } else if ((dropX < -100) || (xSpeed < -RAConstant.swipe_filter_value)) {
                                        if (xSpeed > RAConstant.swipe_filter_value) {
                                            ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                            ani.setDuration(RAConstant.duration_without_velocity);
                                            ani.addListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {

                                                }


                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    m_horizon_swipping = false;
                                                    m_leftPanel.setVisibility(View.GONE);
                                                    m_rightPanel.setVisibility(View.GONE);
                                                }
                                            });

                                            ani.start();

                                        } else {
                                            if (dropX < -100) {
                                                ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", -m_screenWidth + 60 * scaledDpi);

                                                if (xSpeed > -RAConstant.swip_standard_velocity)
                                                    xSpeed = -RAConstant.swip_standard_velocity;
                                                else if (xSpeed < -RAConstant.swip_standard_velocity)
                                                    xSpeed = xSpeed / 2 - (float) (Math.sqrt(-xSpeed));

                                                ani.setDuration((long) (RAConstant.numerator_duration_with_velocity / Math.sqrt((-xSpeed / RAConstant.swip_standard_velocity))));
                                                ani.addListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
//                                        self.m_rightPanel.setVisibility(View.VISIBLE);
//                                        self.m_leftPanel.setVisibility(View.INVISIBLE);
                                                        m_itemsStateList.set(self.m_position - 2, RA_PANEL_STATE.PANEL_OPEN_TO_RIGHT);
                                                        m_leftPanel.setVisibility(View.GONE);
                                                        m_rightPanel.setVisibility(View.VISIBLE);
                                                        m_horizon_swipping = false;
                                                    }
                                                });

                                                ani.start();
                                            } else {
                                                ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                                ani.setDuration(RAConstant.duration_without_velocity);
                                                ani.addListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {

                                                    }


                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        m_horizon_swipping = false;
                                                        m_leftPanel.setVisibility(View.GONE);
                                                        m_rightPanel.setVisibility(View.GONE);
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

                                            }


                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                m_horizon_swipping = false;
                                                m_leftPanel.setVisibility(View.GONE);
                                                m_rightPanel.setVisibility(View.GONE);
                                            }
                                        });

                                        ani.start();


                                    }

                                } else if (m_itemsStateList.get(m_position - 2) == RA_PANEL_STATE.PANEL_OPEN_TO_LEFT) {
                                    if ((dropX < m_screenWidth - 190) || (xSpeed < -RAConstant.swipe_filter_value)) {
                                        ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", 5 * scaledDpi);
                                        if (xSpeed > -RAConstant.swip_standard_velocity)
                                            xSpeed = -RAConstant.swip_standard_velocity;
                                        else if (xSpeed < -RAConstant.swip_standard_velocity)
                                            xSpeed = xSpeed / 2 - (float) (Math.sqrt(-xSpeed));

                                        ani.setDuration((long) (RAConstant.numerator_duration_with_velocity / Math.sqrt((-xSpeed / RAConstant.swip_standard_velocity))));
                                        ani.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                m_itemsStateList.set(self.m_position - 2, RA_PANEL_STATE.PANEL_CLOSED);
                                                m_horizon_swipping = false;
                                                m_leftPanel.setVisibility(View.GONE);
                                                m_rightPanel.setVisibility(View.GONE);
                                            }
                                        });
                                        ani.start();
                                    } else {
                                        ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                        ani.setDuration(RAConstant.duration_without_velocity);
                                        ani.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                m_horizon_swipping = false;
                                            }
                                        });

                                        ani.start();
                                    }

                                } else if (m_itemsStateList.get(m_position - 2) == RA_PANEL_STATE.PANEL_OPEN_TO_RIGHT) {
                                    if ((dropX > -m_screenWidth + 190) || (xSpeed > RAConstant.swipe_filter_value)) {
                                        ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", 5 * scaledDpi);

                                        if (xSpeed < RAConstant.swip_standard_velocity)
                                            xSpeed = RAConstant.swip_standard_velocity;
                                        else if (xSpeed > RAConstant.swip_standard_velocity)
                                            xSpeed = xSpeed / 2 + (float) (Math.sqrt(xSpeed));

                                        ani.setDuration((long) (RAConstant.numerator_duration_with_velocity / Math.sqrt((xSpeed / RAConstant.swip_standard_velocity))));
                                        ani.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                m_itemsStateList.set(self.m_position - 2, RA_PANEL_STATE.PANEL_CLOSED);
                                                m_horizon_swipping = false;
                                                m_leftPanel.setVisibility(View.GONE);
                                                m_rightPanel.setVisibility(View.GONE);
                                            }
                                        });
                                        ani.start();
                                    } else {
                                        ObjectAnimator ani = ObjectAnimator.ofFloat(m_centerPanel, "X", m_orgX);
                                        ani.setDuration(RAConstant.duration_without_velocity);
                                        ani.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                m_horizon_swipping = false;
                                            }
                                        });
                                        ani.start();
                                    }
                                }
                                m_velocityTracker.recycle();
                                m_velocityTracker = null;
                            }
                        }
                        else
                        {
                            float curX = e.getX();
                            float curY = e.getY();
                            double dist = Math.sqrt((curX - m_lastX1)*(curX - m_lastX1) + (curY - m_lastY1)*(curY-m_lastY1));
                            m_distance += dist;
                            if(m_distance == 0)
                            {
                                System.out.println("Click item, Distance :" + m_distance);
                                m_position = RAUtils.getListItemPositionFromPoint(e.getRawX(), e.getRawY(), m_list);
                                joinAtRoom(m_position);
                            }
                        }

                    }

                    gd.onTouchEvent(e);
                    // ... handle TOUCH_UP and TOUCH_CANCEL
                    return false;
                }
            });

            m_list.setSelection(1);
        }
        else
        {
            m_content1.setVisibility(View.VISIBLE);
            m_content11.setVisibility(View.GONE);
        }
    }






    //Room delegate methods
    public void muteRoom(int position)
    {
        RARoom room = m_roomsList.get(position-2);
        System.out.println("Mute room \'" + room.getName() + "\'");
    }

    public void addGuestToRoom(int position)
    {
        RARoom room = m_roomsList.get(position-2);
        System.out.println("Add a guest to room \'" + room.getName() + "\'");
    }

    public void settingRoom(int position)
    {
        RARoom room = m_roomsList.get(position-2);
        System.out.println("Setting room \'" + room.getName() + "\'");
    }

    public void deleteRoom(int position)
    {
        RARoom room = m_roomsList.get(position-2);
        System.out.println("Delete room \'" + room.getName() + "\'");
    }



    public void refreshItem(int position)
    {
        RA_PANEL_STATE state = m_itemsStateList.get(position-2);
        View itemView = RAUtils.getViewByPosition(position, m_list);
        RelativeLayout centerPanel = (RelativeLayout)itemView.findViewById(R.id.centerPanel);
        RelativeLayout leftPanel = (RelativeLayout)itemView.findViewById(R.id.leftPanel);
        RelativeLayout rightPanel = (RelativeLayout)itemView.findViewById(R.id.rightPanel);
        if(state == RA_PANEL_STATE.PANEL_CLOSED)
        {
            centerPanel.setX(8);
            leftPanel.setVisibility(View.GONE);
            rightPanel.setVisibility(View.GONE);
        }
        else if(state == RA_PANEL_STATE.PANEL_OPEN_TO_LEFT)
        {
            centerPanel.setX(m_screenWidth - 90);
            leftPanel.setVisibility(View.VISIBLE);
            rightPanel.setVisibility(View.GONE);
        }
        else if(state == RA_PANEL_STATE.PANEL_OPEN_TO_RIGHT)
        {
            centerPanel.setX(-m_screenWidth + 90);
            leftPanel.setVisibility(View.GONE);
            rightPanel.setVisibility(View.VISIBLE);
        }

    }

    private int computeScrollOffset(ListView listView)
    {
        final int firstPosition = listView.getFirstVisiblePosition();
        final int childCount = listView.getChildCount();
        if (firstPosition >= 0 && childCount > 0) {
            if (m_list.isSmoothScrollbarEnabled()) {
                final View childview = listView.getChildAt(0);
                final int top = childview.getTop();

                int height = childview.getHeight();
                if (height > 0) {

                    // The core of the change is here (mHeaderRowm_screenHeight)
                    return Math.max(firstPosition * 100 - (top * 100) / height +
                            (int) ((float) (listView.getScrollY() + 0) / (listView.getHeight() + 0) * listView.getCount() * 100), 0);
                }
            } else {
                int index;
                final int count = listView.getCount();
                if (firstPosition == 0) {
                    index = 0;
                } else if (firstPosition + childCount == count) {
                    index = count;
                } else {
                    index = firstPosition + childCount / 2;
                }
                return (int) (firstPosition + childCount * (index / (float) count));
            }
        }
        return 0;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {


    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        System.out.println(charSequence);

    }

    @Override
    public void afterTextChanged(Editable editable) {


    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
