package co.roomapp.room.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.customcell.ImageLoader;
import co.roomapp.room.R;
import co.roomapp.room.activity.RARoomsActivity;
import co.roomapp.room.model.RAAttachment;
import co.roomapp.room.model.RARoom;
import co.roomapp.room.model.RAChatSession;
import co.roomapp.room.model.RAMessage;

import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAConstant.*;
import co.roomapp.room.utils.RAUtils;

/**
 * Created by luoqi on 2/22/2015.
 */
public class RAChatSessionListAdapter extends BaseAdapter {

    enum RA_PANEL_STATE
    {
        PANEL_CLOSED,
        PANEL_OPEN_TO_LEFT,
        PANEL_OPEN_TO_RIGHT
    }

    private Activity activity;
    private RAChatSession chatSession;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    ArrayList<RA_PANEL_STATE> itemsStateList;
    View vi;
    VelocityTracker mVelocityTracker;
    float lastX;
    float orgX;
    private float scaledDpi;

    public RAChatSessionListAdapter(Activity a, RAChatSession d) {
        activity = a;
        chatSession = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        DisplayMetrics dm = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(dm);
        scaledDpi = dm.density;
    }

    public int getCount() {
        return chatSession.getMessages().size() + 1;
    }

    public Object getItem(int position) {

        if(position == 0)
            return null;
        List<RAMessage> messageArray = chatSession.getMessages();
        if(position <= messageArray.size())
            return messageArray.get(position-1);
        else
            return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if(position == 0) {
            vi = inflater.inflate(R.layout.chatsession_list_header, null);
            return vi;
        }
        else {
            Object item = getItem(position);
            if (item != null) {
                RAMessage message = (RAMessage) item;
                RAMessageDirection msgDirection = (message.getFromJID().equals(chatSession.getContactJID()))?
                        RAMessageDirection.RAMessageDirectionLeft:RAMessageDirection.RAMessageDirectionRight;

                MessageItem tagObj = null;
                if (message.getFromJID().equals(chatSession.getContactJID())) {
                    vi = inflater.inflate(R.layout.chatsession_list_item_left, null);
                    tagObj = new MessageItem(RAMessageDirection.RAMessageDirectionLeft);

                } else {
                    vi = inflater.inflate(R.layout.chatsession_list_item_right, null);
                    tagObj = new MessageItem(RAMessageDirection.RAMessageDirectionRight);
                }

                RelativeLayout thumbLayout = (RelativeLayout) vi.findViewById(R.id.msgThumbLayout);
                RelativeLayout audioLayout = (RelativeLayout) vi.findViewById(R.id.audioLayout);
                RelativeLayout playLayout = (RelativeLayout) vi.findViewById(R.id.playLayout);
                RelativeLayout gridLayout = (RelativeLayout) vi.findViewById(R.id.gridLayout);
                GridView gridView = (GridView) vi.findViewById(R.id.gridView);
                ImageButton btnPlay = (ImageButton) vi.findViewById(R.id.btnPlay);
                ImageView thumbnail = (ImageView) vi.findViewById(R.id.msgThumbnail);
                TextView msgText = (TextView) vi.findViewById(R.id.msgText);
                TextView msgDate = (TextView) vi.findViewById(R.id.msgDate);
                TextView durationLabel = (TextView) vi.findViewById(R.id.durationLabel);
                RelativeLayout checkedLayout = (RelativeLayout) vi.findViewById(R.id.checkedLayout);
                ImageView singleChecked = (ImageView) vi.findViewById(R.id.singleChecked);
                ImageView doubleChecked = (ImageView) vi.findViewById(R.id.doubleChecked);

                if(message.getType() == RAMessageType.RAMessageTypeRegularPost.ordinal()) {
                    tagObj.setMsgType(RAMessageType.RAMessageTypeRegularPost);
                    msgText.setVisibility(View.VISIBLE);
                    thumbLayout.setVisibility(View.GONE);
                    audioLayout.setVisibility(View.GONE);
                    String body = message.getBody();
                    msgText.setText(body);
                }
                else if(message.getType() == RAMessageType.RAMessageTypeOnePhotoPost.ordinal()) {
                    tagObj.setMsgType(RAMessageType.RAMessageTypeOnePhotoPost);
                    thumbLayout.setVisibility(View.VISIBLE);
                    thumbnail.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    audioLayout.setVisibility(View.GONE);
                    playLayout.setVisibility(View.GONE);
                    String body = message.getBody();
                    if((body!=null)&&(!body.equals(""))) {
                        msgText.setVisibility(View.VISIBLE);
                        msgText.setText(body);
                    }
                    else {
                        msgText.setVisibility(View.GONE);
                    }
                    List<RAAttachment> attachments = message.getAttachmentsFake();
                    if(attachments.size() == 1)
                    {
                        RAAttachment attachment = attachments.get(0);
                        String imagePath = attachment.getMediaURL();
                        imageLoader.DisplayImage(imagePath, thumbnail);
                    }
                }
                else if(message.getType() == RAMessageType.RAMessageTypeMultiplePhotoPost.ordinal()) {
                    tagObj.setMsgType(RAMessageType.RAMessageTypeMultiplePhotoPost);
                    thumbLayout.setVisibility(View.VISIBLE);
                    thumbnail.setVisibility(View.GONE);
                    gridLayout.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.VISIBLE);
                    audioLayout.setVisibility(View.GONE);
                    playLayout.setVisibility(View.GONE);
                    String body = message.getBody();
                    if((body!=null)&&(!body.equals(""))) {
                        msgText.setVisibility(View.VISIBLE);
                        msgText.setText(body);
                    }
                    else {
                        msgText.setVisibility(View.GONE);
                    }

                    List<RAAttachment> attachments = message.getAttachmentsFake();
                    if(attachments.size() > 1)
                    {
                        audioLayout.setVisibility(View.GONE);
                        PhotoGridViewAdapter m_photoListAdapter = new PhotoGridViewAdapter(activity, attachments);
                        gridView.setAdapter(m_photoListAdapter);
                    }
                }
                else if(message.getType() == RAMessageType.RAMessageTypeVideoPost.ordinal()) {
                    tagObj.setMsgType(RAMessageType.RAMessageTypeVideoPost);
                    thumbLayout.setVisibility(View.VISIBLE);
                    thumbnail.setVisibility(View.VISIBLE);
                    gridLayout.setVisibility(View.GONE);
                    playLayout.setVisibility(View.VISIBLE);
                    audioLayout.setVisibility(View.GONE);
                    String body = message.getBody();
                    if((body!=null)&&(!body.equals(""))) {
                        msgText.setVisibility(View.VISIBLE);
                        msgText.setText(body);
                    }
                    else {
                        msgText.setVisibility(View.GONE);
                    }

                    List<RAAttachment> attachments = message.getAttachmentsFake();
                    if(attachments.size() == 1)
                    {
                        RAAttachment attachment = attachments.get(0);
                        String videoPath = attachment.getMediaURL();
                        Bitmap thumbnailImage = RAUtils.createThumbnailForPath(videoPath);
                        thumbnail.setImageBitmap(thumbnailImage);
                    }
                }
                else if(message.getType() == RAMessageType.RAMessageTypeAudioPost.ordinal()) {
                    tagObj.setMsgType(RAMessageType.RAMessageTypeAudioPost);
                    thumbLayout.setVisibility(View.GONE);
                    audioLayout.setVisibility(View.VISIBLE);
                    String body = message.getBody();
                    if((body!=null)&&(!body.equals(""))) {
                        msgText.setText(body);
                        msgText.setText(body);
                    }
                    else {
                        msgText.setVisibility(View.GONE);
                    }
                    List<RAAttachment> attachments = message.getAttachmentsFake();

                    if(attachments.size() == 1)
                    {
                        RAAttachment attachment = attachments.get(0);
                        String videoPath = attachment.getMediaURL();
                        Double duration = attachment.getMovieDuration();
                        Bitmap thumbnailImage = RAUtils.createThumbnailForPath(videoPath);
                        thumbnail.setImageBitmap(thumbnailImage);
                        durationLabel.setText(duration.toString());
                    }
                }
                else if(message.getType() == RAMessageType.RAMessageTypeLocationPost.ordinal()) {
                    tagObj.setMsgType(RAMessageType.RAMessageTypeLocationPost);
                }

                Date currentdate = new Date();
                Date date = message.getMessagedate();
                if(currentdate.getDate() != date.getDate()) {
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault());
                    String dateStr = dateFormat.format(date);
                    msgDate.setText(dateStr);

                }
                else {
                    DateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
                    String dateStr = dateFormat.format(date);
                    msgDate.setText(dateStr);
                }

                if(msgDirection == RAMessageDirection.RAMessageDirectionRight) {
                    if (message.getIsDelivered()) {
                        checkedLayout.setVisibility(View.VISIBLE);
                        singleChecked.setVisibility(View.VISIBLE);
                        if (message.getIsRead())
                            doubleChecked.setVisibility(View.VISIBLE);
                        else
                            doubleChecked.setVisibility(View.GONE);
                    } else
                        checkedLayout.setVisibility(View.GONE);
                }
                vi.setTag(tagObj);

            }

            return vi;
        }
    }

    private class MessageItem
    {
        RAMessageDirection msgDirection;
        RAMessageType msgType;
        public MessageItem(RAMessageDirection direction) {
            this.msgDirection = direction;
        }

        public void setMsgType(RAMessageType msgType) { this.msgType = msgType; }
    }

    private class PhotoGridViewAdapter extends BaseAdapter
    {

        private int top_margin;
        private int left_margin;
        private int right_margin;
        private int bottom_margin;
        private List<RAAttachment> attchments;
        private Context mContext;

        public PhotoGridViewAdapter(Context c) {
            mContext = c;
        }

        public PhotoGridViewAdapter(Context c, List<RAAttachment> _attchments) {
            mContext = c;
            attchments = _attchments;
        }

        @Override
        public int getCount() {
            return attchments.size();
        }

        @Override
        public Object getItem(int position)
        {
            return attchments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RAAttachment attachment = attchments.get(position);

            if(attachment.getType() == RAConstant.RAAttachmentType.RAAttachmentTypeImage.ordinal())
            {
                String imagePath = attachment.getMediaURL();

                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                System.out.println("Image Path: " + imagePath);
                if((imagePath != null)&&(!imagePath.equals("")))
                    imageLoader.DisplayImage(imagePath, imageView);
//                    imageView.setImageDrawable(R.id.icon);

                imageView.setLayoutParams(new GridView.LayoutParams((int)(60*scaledDpi),(int)(60*scaledDpi)));
                imageView.setPadding(10,10,10,10);
                return imageView;
            }

            return null;
        }
    }
}
