package co.roomapp.room.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Button;
import co.roomapp.room.R;
import co.roomapp.room.activity.RARoomsActivity;
import co.roomapp.room.model.RARoom;
import co.roomapp.room.model.RAMessage;
import co.customcell.ImageLoader;
import android.view.GestureDetector;
import android.util.DisplayMetrics;
/**
 * Created by manager on 1/8/15.
 */



public class RARoomListAdapter extends BaseAdapter {

    enum RA_PANEL_STATE
    {
        PANEL_CLOSED,
        PANEL_OPEN_TO_LEFT,
        PANEL_OPEN_TO_RIGHT
    }
    private Activity activity;
    private ArrayList<RARoom> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    ArrayList<RA_PANEL_STATE> itemsStateList;
    View vi;
    VelocityTracker mVelocityTracker;
    float lastX;
    float orgX;

    public RARoomListAdapter(Activity a, ArrayList<RARoom> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size() + 2;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        vi = convertView;

        if(convertView==null) {
            if(position > 1) {
                vi = inflater.inflate(R.layout.roomlist_custom_cell, null);
                vi.setTag(2);
                TextView title = (TextView)vi.findViewById(R.id.roomTitle); // room title
                TextView creator= (TextView)vi.findViewById(R.id.roomCreator); // room creator name
                TextView membersCount = (TextView)vi.findViewById(R.id.roomUsersCount); // room users count
                ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
                RARoom room = (RARoom)data.get(position - 2);
                title.setText(room.getName());
                creator.setText("by " + room.getOwnerDisplayName());
                membersCount.setText(room.getMembersCount().toString());
                imageLoader.DisplayImage(room.getCoverURL(), thumb_image);

//                RAMessage message = room.getLastMessage();
//                String msgText = message.getBody();
                TextView msgTextView = (TextView)vi.findViewById(R.id.lastCommentText);
                msgTextView.setText("That sounds very good. Move on please");

                ImageButton btnMute = (ImageButton)vi.findViewById(R.id.imageButton);
                ImageButton btnAddGuest = (ImageButton)vi.findViewById(R.id.imageButton2);
                ImageButton btnSetting = (ImageButton)vi.findViewById(R.id.imageButton3);
                ImageButton btnDelete = (ImageButton)vi.findViewById(R.id.imageButton4);

                btnMute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.muteRoom(position);
                    }
                });
                btnAddGuest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.addGuestToRoom(position);

                    }
                });
                btnSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.settingRoom(position);

                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.deleteRoom(position);

                    }
                });

//                vi.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        System.out.println("Item Clicked");
//                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
//                        roomsActivity.joinAtRoom(position-2);
//                    }
//                });

            }
            else if(position == 0) {
                vi = inflater.inflate(R.layout.roomlist_searchbar_cell, null);
                vi.setTag(0);

                final SearchView searchView = (SearchView)vi.findViewById(R.id.searchBar);

                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        return false;
                    }
                });


                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.searchRooms(searchView.getQuery().toString());
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        System.out.println(newText);
                        return true;
                    }
                });
            }
            else if(position == 1) {
                vi = inflater.inflate(R.layout.roomlist_toolbar_cell, null);
                vi.setTag(1);

                Button btnSearchRoom = (Button)vi.findViewById(R.id.btnSearchRoom);
                Button btnJoinRoom = (Button)vi.findViewById(R.id.btnJoinRoom);

                btnSearchRoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.exploreRooms();
                    }
                });
                btnJoinRoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.joinRoom();
                    }
                });
            }
        }
        else
        {
            if(position > 1)
            {
                if(vi.getTag()!=2) {
                    vi = inflater.inflate(R.layout.roomlist_custom_cell, null);
                    vi.setTag(2);
                }
                TextView title = (TextView)vi.findViewById(R.id.roomTitle); // room title
                TextView creator= (TextView)vi.findViewById(R.id.roomCreator); // room creator name
                TextView membersCount = (TextView)vi.findViewById(R.id.roomUsersCount); // room users count
                ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
                RARoom room = (RARoom)data.get(position - 2);
                title.setText(room.getName());
                creator.setText("by " + room.getOwnerDisplayName());
                membersCount.setText(room.getMembersCount().toString());
                imageLoader.DisplayImage(room.getCoverURL(), thumb_image);

//                RAMessage message = room.getLastMessage();
//                String msgText = message.getBody();
                TextView msgTextView = (TextView)vi.findViewById(R.id.lastCommentText);
                msgTextView.setText("That sounds very interesting.  It's fantastic story. I want to hear more please go on");

                ImageButton btnMute = (ImageButton)vi.findViewById(R.id.imageButton);
                ImageButton btnAddGuest = (ImageButton)vi.findViewById(R.id.imageButton2);
                ImageButton btnSetting = (ImageButton)vi.findViewById(R.id.imageButton3);
                ImageButton btnDelete = (ImageButton)vi.findViewById(R.id.imageButton4);

                btnMute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.muteRoom(position);
                    }
                });
                btnAddGuest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.addGuestToRoom(position);

                    }
                });
                btnSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.settingRoom(position);

                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                        roomsActivity.deleteRoom(position);

                    }
                });


//                vi.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        System.out.println("Item Clicked");
//                        RARoomsActivity roomsActivity = (RARoomsActivity) activity;
//                        roomsActivity.joinAtRoom(position-2);
//                    }
//                });

                RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                roomsActivity.refreshItem(position);
            }
            else
            {
                if(vi.getTag()==2)
                {
                    if(position == 0) {
                        vi = inflater.inflate(R.layout.roomlist_searchbar_cell, null);
                        vi.setTag(0);

                        final SearchView searchView = (SearchView)vi.findViewById(R.id.searchBar);

                        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                            @Override
                            public boolean onClose() {
                                return false;
                            }
                        });


                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                RARoomsActivity roomsActivity = (RARoomsActivity)activity;
                                roomsActivity.searchRooms(searchView.getQuery().toString());
                                searchView.clearFocus();
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                System.out.println(newText);
                                return true;
                            }
                        });
                    }
                    else if(position == 1) {
                        vi = inflater.inflate(R.layout.roomlist_toolbar_cell, null);
                        vi.setTag(1);

                        Button btnSearchRoom = (Button)vi.findViewById(R.id.btnSearchRoom);
                        Button btnJoinRoom = (Button)vi.findViewById(R.id.btnJoinRoom);

                        btnSearchRoom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                                roomsActivity.exploreRooms();
                            }
                        });
                        btnJoinRoom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RARoomsActivity roomsActivity = (RARoomsActivity) activity;
                                roomsActivity.joinRoom();
                            }
                        });
                    }
                }
            }
        }

        return vi;



    }
}
