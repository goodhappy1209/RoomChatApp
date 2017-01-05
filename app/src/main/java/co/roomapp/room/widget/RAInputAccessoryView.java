package co.roomapp.room.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import co.roomapp.room.R;
import co.roomapp.room.activity.RAAudioRecorderActivity;
import co.roomapp.room.activity.RAChatActivity;
import co.roomapp.room.activity.RAMapViewActivity;
import co.roomapp.room.activity.RATimelineActivity;
import co.roomapp.room.model.RAAttachment;
import co.roomapp.room.model.RAChatSession;
import co.roomapp.room.model.RAMessage;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAUtils;

import android.app.Activity;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;

import android.graphics.Bitmap;
import android.widget.ProgressBar;
import co.roomapp.room.utils.RAConstant.*;
/**
 * Created by a on 2/3/15.
 */


public class RAInputAccessoryView extends RelativeLayout implements TextWatcher, View.OnClickListener{

    final int INTENT_ACTION_TAKE_PHOTO = 0;
    final int INTENT_ACTION_TAKE_VIDEO = 1;
    final int INTENT_ACTION_SELECT_PHOTO = 2;
    final int INTENT_ACTION_SELECT_VIDEO = 3;
    final int INTENT_ACTION_RECORD_AUDIO = 4;
    final int INTENT_ACTION_PIN_LOCATION = 5;

    enum BUTTON_TAGS {
        edt_tag_msg,
        btn_tag_post,
        btn_tag_status,
        btn_tag_media,
        btn_tag_gallery,
        btn_tag_audio,
        btn_tag_checkin
    };

    Context mContext;
    EditText edtMessage;
    Button btnPost;
    ImageButton btnStatus;
    ImageButton btnMedia;
    ImageButton btnGallery;
    ImageButton btnAudio;
    ImageButton btnCheckin;
    ImageView imgUnderline;
    RAHorizontalListView listView;
    ProgressBar progressBar;

    ArrayList<Object> thumbImagesList;
    private ArrayList<Object> m_arrAttachments;
    private ArrayList<Object> m_arrThumbnails;

    RelativeLayout edtMsgLayout;
    Activity delegate;
    float scaledDpi;
    boolean isAttached;
    private RAConstant.RAMessageType m_type;
    private RAChatSession m_chatSession;

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return thumbImagesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(thumbImagesList != null) {
                View thumbView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail_listview_item, null);
                ImageView thumbImage = (ImageView) thumbView.findViewById(R.id.thumbnailView);
                if(thumbImagesList.size() - 1 < position)
                    return null;
                Bitmap thumb = (Bitmap) thumbImagesList.get(position);
                thumbImage.setImageBitmap(thumb);
                return thumbView;
            }
            else
                return null;
        }
    };

    public RAInputAccessoryView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.activity_ratimeline_inputaccessory_view, this, true);
        setup(null);
    }

    public RAInputAccessoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.activity_ratimeline_inputaccessory_view, this, true);
        setup(attrs);
    }

    public RAInputAccessoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.activity_ratimeline_inputaccessory_view, this, true);
        setup(attrs);
    }

    public void setDelegate(Activity _delegate)
    {
        delegate = _delegate;
        DisplayMetrics dm = new DisplayMetrics();
        delegate.getWindowManager().getDefaultDisplay().getMetrics(dm);
        scaledDpi = dm.density;
    }

    public void setThumbImagesList(ArrayList<Object> _thumbImagesList)
    {
        thumbImagesList = _thumbImagesList;
        mAdapter.notifyDataSetChanged();

        if(_thumbImagesList.size() > 0) {
            thumbImagesList = _thumbImagesList;
            attachedThumbnail();
        }
        else {

            removedAllThumbnails();
        }
    }

    private void setup(AttributeSet attrs){

        isAttached = false;
        edtMessage = (EditText)findViewById(R.id.editMessage);
        btnPost = (Button)findViewById(R.id.btnPost);
        btnStatus = (ImageButton)findViewById(R.id.btnStatus);
        btnMedia = (ImageButton)findViewById(R.id.btnMedia);
        btnGallery = (ImageButton)findViewById(R.id.btnGallery);
        btnAudio = (ImageButton)findViewById(R.id.btnAudio);
        btnCheckin = (ImageButton)findViewById(R.id.btnCheckin);
        imgUnderline = (ImageView)findViewById(R.id.underlineImg);
        listView = (RAHorizontalListView)findViewById(R.id.thumbnailScrollView);
        listView.setAdapter(mAdapter);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);


        edtMessage.setTag(BUTTON_TAGS.edt_tag_msg);
        btnPost.setTag(BUTTON_TAGS.btn_tag_post);
        btnStatus.setTag(BUTTON_TAGS.btn_tag_status);
        btnMedia.setTag(BUTTON_TAGS.btn_tag_media);
        btnGallery.setTag(BUTTON_TAGS.btn_tag_gallery);
        btnAudio.setTag(BUTTON_TAGS.btn_tag_audio);
        btnCheckin.setTag(BUTTON_TAGS.btn_tag_checkin);

        edtMessage.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        btnStatus.setOnClickListener(this);
        btnMedia.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
        btnCheckin.setOnClickListener(this);

        edtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });

        edtMessage.addTextChangedListener(this);

        edtMsgLayout = (RelativeLayout)findViewById(R.id.edtMessageLayout);

        m_arrAttachments = new ArrayList<>();
        m_arrThumbnails = new ArrayList<>();
    }

    public void hideKeyboard()
    {
        delegate.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        int lineCount = edtMessage.getLineCount();
        int lineHeight = edtMessage.getLineHeight();

        int desiredHeight = (int)(lineCount * lineHeight * scaledDpi);

        if(desiredHeight  >= 160 * scaledDpi)
            desiredHeight  = (int)(160 * scaledDpi);
        else if(desiredHeight <= 70 * scaledDpi)
            desiredHeight = (int)(70 * scaledDpi);

        LayoutParams layoutParams = (RelativeLayout.LayoutParams)edtMsgLayout.getLayoutParams();

        layoutParams.height = (isAttached)?desiredHeight + (int)(60 * scaledDpi):desiredHeight;

        edtMsgLayout.setLayoutParams(layoutParams);
    }


    @Override
    public void onClick(View view)
    {
        if(view.getTag() == BUTTON_TAGS.edt_tag_msg)
        {
            if(delegate.getClass() == RATimelineActivity.class) {
                RATimelineActivity parentActivity = (RATimelineActivity) delegate;
                if (parentActivity.getActionBarState() == RA_ACTION_BAR_STATE.ACTION_BAR_MEDIA_SHOWN) {
                    parentActivity.hideMediaActionBar();
                } else {
                    parentActivity.hideGalleryActionBar();
                }
                parentActivity.moveInputAccessoryViewToBottom();
            }
            else if(delegate.getClass() == RAChatActivity.class) {

            }
            RelativeLayout btnStatusLayout = (RelativeLayout)findViewById(R.id.btnStatusLayout);
            float centerX = btnStatusLayout.getX() + btnStatusLayout.getWidth()/2;
            moveToPosition(imgUnderline, centerX - imgUnderline.getWidth() / 2);
        }
        else if(view.getTag() == BUTTON_TAGS.btn_tag_post)
        {
            postAction();
        }
        else if(view.getTag() == BUTTON_TAGS.btn_tag_status)
        {
            RelativeLayout btnStatusLayout = (RelativeLayout)findViewById(R.id.btnStatusLayout);
            float centerX = btnStatusLayout.getX() + btnStatusLayout.getWidth()/2;
            moveToPosition(imgUnderline, centerX - imgUnderline.getWidth()/2);
        }
        else if(view.getTag() == BUTTON_TAGS.btn_tag_media)
        {
            InputMethodManager imm = (InputMethodManager)delegate.getSystemService(delegate.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( edtMessage.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
            RelativeLayout btnMediaLayout = (RelativeLayout)findViewById(R.id.btnMediaLayout);
            float centerX = btnMediaLayout.getX() + btnMediaLayout.getWidth()/2;
            moveToPosition(imgUnderline, centerX - imgUnderline.getWidth()/2);
            if(delegate.getClass() == RATimelineActivity.class) {
                RATimelineActivity parentActivity = (RATimelineActivity) delegate;
                parentActivity.showActionBar(0);
            }
            else if(delegate.getClass() == RAChatActivity.class) {
                RAChatActivity parentActivity = (RAChatActivity) delegate;
                parentActivity.showActionBar(0);
            }
        }
        else if(view.getTag() == BUTTON_TAGS.btn_tag_gallery)
        {
            InputMethodManager imm = (InputMethodManager)delegate.getSystemService(delegate.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow( edtMessage.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);

            RelativeLayout btnGalleryLayout = (RelativeLayout)findViewById(R.id.btnGalleryLayout);
            float centerX = btnGalleryLayout.getX() + btnGalleryLayout.getWidth()/2;
            moveToPosition(imgUnderline, centerX - imgUnderline.getWidth()/2);

            if(delegate.getClass() == RATimelineActivity.class) {
                RATimelineActivity parentActivity = (RATimelineActivity) delegate;
                parentActivity.showActionBar(1);
            }
            else if(delegate.getClass() == RAChatActivity.class) {
                RAChatActivity parentActivity = (RAChatActivity) delegate;
                parentActivity.showActionBar(1);
            }

        }
        else if(view.getTag() == BUTTON_TAGS.btn_tag_audio)
        {
            RelativeLayout btnAudioLayout = (RelativeLayout)findViewById(R.id.btnAudioLayout);
            float centerX = btnAudioLayout.getX() + btnAudioLayout.getWidth()/2;
            moveToPosition(imgUnderline, centerX - imgUnderline.getWidth()/2);
            recordAudio();
        }
        else if(view.getTag() == BUTTON_TAGS.btn_tag_checkin)
        {
            RelativeLayout btnCheckinLayout = (RelativeLayout)findViewById(R.id.btnCheckinLayout);
            float centerX = btnCheckinLayout.getX() + btnCheckinLayout.getWidth()/2;
            moveToPosition(imgUnderline, centerX - imgUnderline.getWidth()/2);
            pinLocation();
        }
    }

    public void takePhoto() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        delegate.startActivityForResult(intent, INTENT_ACTION_TAKE_PHOTO);
    }

    public void takeVideo() {
        Intent intent = new Intent(
                MediaStore.ACTION_VIDEO_CAPTURE);
        delegate.startActivityForResult(intent, INTENT_ACTION_TAKE_VIDEO);

    }

    public void selectPhoto() {

        Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        delegate.startActivityForResult(Intent.createChooser( intent, "Select Picture" ), INTENT_ACTION_SELECT_PHOTO);
    }

    public void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT );
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        delegate.startActivityForResult(Intent.createChooser( intent, "Select Video" ), INTENT_ACTION_SELECT_VIDEO);
    }

    public void recordAudio()
    {
        Intent intent = new Intent(delegate, RAAudioRecorderActivity.class);
        delegate.startActivityForResult(intent, INTENT_ACTION_RECORD_AUDIO);
    }

    public void pinLocation()
    {
        Intent intent = new Intent(delegate, RAMapViewActivity.class);
        delegate.startActivityForResult(intent, INTENT_ACTION_PIN_LOCATION);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        Bitmap bitmap = null;

        if((requestCode == INTENT_ACTION_TAKE_PHOTO)||(requestCode == INTENT_ACTION_TAKE_VIDEO)) {
            if(delegate.getClass() == RATimelineActivity.class)
                ((RATimelineActivity) delegate).hideMediaActionBar();
            else if(delegate.getClass() == RAChatActivity.class)
                ((RAChatActivity) delegate).hideMediaActionBar();
        }
        else if((requestCode == INTENT_ACTION_SELECT_PHOTO)||(requestCode == INTENT_ACTION_SELECT_VIDEO)) {
            if(delegate.getClass() == RATimelineActivity.class)
                ((RATimelineActivity) delegate).hideGalleryActionBar();
            else if(delegate.getClass() == RAChatActivity.class)
                ((RAChatActivity) delegate).hideGalleryActionBar();
        }
        if((m_type != RAConstant.RAMessageType.RAMessageTypeOnePhotoPost) && (m_type != RAConstant.RAMessageType.RAMessageTypeMultiplePhotoPost)) {
            m_arrAttachments = new ArrayList<Object>();
            m_arrThumbnails = new ArrayList<Object>();
            setThumbImagesList(m_arrThumbnails);
        }

        if ((requestCode == INTENT_ACTION_TAKE_PHOTO)||(requestCode == INTENT_ACTION_SELECT_PHOTO)) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    // recyle unused bitmaps
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    stream = delegate.getContentResolver().openInputStream(data.getData());
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeStream(stream, null, o);
                    m_type = RAConstant.RAMessageType.RAMessageTypeOnePhotoPost;
                    m_arrAttachments.add(RAUtils.resizedBitmapByMagick(bitmap, "1280x1280"));
                    m_arrThumbnails.add(RAUtils.resizedBitmapByMagick(bitmap, "1280x1280"));
                    if(listView != null) {
                        listView.setVisibility(View.VISIBLE);
                        setThumbImagesList(m_arrThumbnails);
                    }

                    System.out.println("Photo Attached: " + m_arrThumbnails.size());


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                finally {
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                }
            }
        }
        else if ((requestCode == INTENT_ACTION_TAKE_VIDEO)||(requestCode == INTENT_ACTION_SELECT_VIDEO)) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    // recyle unused bitmaps
                    if (bitmap != null) {
                        bitmap.recycle();
                    }

                    m_type = RAConstant.RAMessageType.RAMessageTypeVideoPost;

                    String selectedVideoPath = getPath(data.getData());
                    System.out.println("Video Path: " + selectedVideoPath);

                    if(selectedVideoPath == null) {
                        Log.e("%s", "selected video path = null!");
                    } else {
                        Bitmap bmpThumbnail = RAUtils.createThumbnailForPath(selectedVideoPath);
                        Cursor cursor = MediaStore.Video.query(delegate.getContentResolver(),data.getData(),
                                new String[] { MediaStore.Video.VideoColumns.DURATION });
                        System.out.println(">>>>>>>>>>"+cursor.getCount());
                        cursor.moveToFirst();
                        String durationStr = cursor.getString(cursor.getColumnIndex("duration"));
                        double duration = Double.parseDouble(durationStr);

                        System.out.println("Video duration: " + duration);
                        RAAttachment att = new RAAttachment();
                        att.setMediaURL(selectedVideoPath);
                        att.setMovieDuration(duration);
                        att.setType(Integer.valueOf(RAConstant.RAAttachmentType.RAAttachmentTypeVideo.ordinal()));
                        m_arrAttachments.add(att);
                        m_arrThumbnails.add(bmpThumbnail);
                        if(listView != null) {
                            listView.setVisibility(View.VISIBLE);
                            setThumbImagesList(m_arrThumbnails);
                        }

                        System.out.println("Video Attached: " + m_arrThumbnails.size());
                    }


                } catch (Exception e) {

                } finally {
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                }
            }
        }
        else if (requestCode == INTENT_ACTION_RECORD_AUDIO) {
            if(resultCode == Activity.RESULT_OK)
            {
                String audioPath = data.getStringExtra("audioPath");
                Bitmap audioThumb = BitmapFactory.decodeResource(getResources(), R.drawable.audio_thumbnail);

                System.out.println("Audio attached");

                try
                {
                    MediaPlayer mp = new MediaPlayer();
                    File file = new File(audioPath);
                    mp.setDataSource(file.getAbsolutePath());
                    mp.prepare();

                    int duration = mp.getDuration();
                    System.out.println("Video duration: " + duration);
                    m_arrThumbnails = new ArrayList<Object>();
                    m_arrAttachments = new ArrayList<Object>();
                    m_type = RAConstant.RAMessageType.RAMessageTypeAudioPost;
                    RAAttachment att = new RAAttachment();
                    att.setMediaURL(audioPath);
                    att.setType(RAConstant.RAAttachmentType.RAAttachmentTypeAudio.ordinal());
                    att.setMovieDuration((double)duration);
                    m_arrThumbnails.add(audioThumb);
                    m_arrAttachments.add(att);
                    if(listView != null) {
                        listView.setVisibility(View.VISIBLE);
                        setThumbImagesList(m_arrThumbnails);
                    }

                    System.out.println("Audio Attached: " + m_arrThumbnails.size());

                }
                catch(IOException ie)
                {
                    ie.printStackTrace();
                }

            }
            else
            {

            }
        }
        else if(requestCode == INTENT_ACTION_PIN_LOCATION)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                System.out.println("Map attached");
                Bundle bundle = data.getExtras();
                String title = bundle.getString("locationTitle");
                String sub_title = bundle.getString("locationTitle");
                Double latitude = bundle.getDouble("latitude");
                Double longitude = bundle.getDouble("longitude");
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                RAAttachment att = new RAAttachment();
                att.setLatitude(latitude);
                att.setLongitude(longitude);
                att.setTitle(title);
                att.setBody(sub_title);
                att.setType(RAConstant.RAAttachmentType.RAAttachmentTypeMap.ordinal());
                Bitmap mapThumb = BitmapFactory.decodeResource(getResources(), R.drawable.map_thumb);

                m_arrAttachments.add(att);
                m_arrThumbnails.add(mapThumb);
                if(listView != null) {
                    edtMessage.setText(title);
                    setThumbImagesList(m_arrThumbnails);
                }
                System.out.println("Map Attached: " + m_arrThumbnails.size());
            }
        }
    }

    public void hideMediaActionBar() {

        if(delegate.getClass() == RATimelineActivity.class)
            ((RATimelineActivity)delegate).hideMediaActionBar();
        else if(delegate.getClass() == RAChatActivity.class)
            ((RAChatActivity)delegate).hideMediaActionBar();
    }

    public void hideGalleryActionBar() {

        if(delegate.getClass() == RATimelineActivity.class)
            ((RATimelineActivity)delegate).hideGalleryActionBar();
        else if(delegate.getClass() == RAChatActivity.class)
            ((RAChatActivity)delegate).hideGalleryActionBar();
    }

    void moveToPosition(Object target, float xPos)
    {
        ObjectAnimator ani = ObjectAnimator.ofFloat(target, "X", xPos);
        ani.setDuration(RAConstant.duration_without_velocity);
        ani.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {

            }
        });

        ani.start();
    }

    public void attachedThumbnail()
    {
        if(!isAttached)
        {
            int lineCount = edtMessage.getLineCount();
            int lineHeight = edtMessage.getLineHeight();

            int desiredHeight = (int)(lineCount * lineHeight * scaledDpi);

            if(desiredHeight  >= 160 * scaledDpi)
                desiredHeight  = (int)(160 * scaledDpi);
            else if(desiredHeight <= 70 * scaledDpi)
                desiredHeight = (int)(70 * scaledDpi);

            LayoutParams layoutParams = (RelativeLayout.LayoutParams)edtMsgLayout.getLayoutParams();
            layoutParams.height = desiredHeight + (int)(60 * scaledDpi);

            edtMsgLayout.setLayoutParams(layoutParams);
            listView.setVisibility(View.VISIBLE);
            isAttached = true;
        }
    }

    public void removedAllThumbnails()
    {
        if(isAttached)
        {
            listView.setVisibility(View.GONE);
            int lineCount = edtMessage.getLineCount();
            int lineHeight = edtMessage.getLineHeight();

            int desiredHeight = (int)(lineCount * lineHeight * scaledDpi);

            if(desiredHeight  >= 160 * scaledDpi)
                desiredHeight  = (int)(160 * scaledDpi);
            else if(desiredHeight <= 70 * scaledDpi)
                desiredHeight = (int)(70 * scaledDpi);

            LayoutParams layoutParams = (RelativeLayout.LayoutParams)edtMsgLayout.getLayoutParams();
            layoutParams.height = desiredHeight;

            edtMsgLayout.setLayoutParams(layoutParams);

            thumbImagesList = null;

            isAttached = false;
        }
    }

    public void postAction()
    {
        RAConstant.RAMessageType type = RAConstant.RAMessageType.RAMessageTypeRegularPost;
        ArrayList<Object> mediaToUpload = new ArrayList<Object>();
        String trimmedString = edtMessage.getText().toString().trim();
        Bitmap thumbnail = null;
        RAAttachment attachment = new RAAttachment();
        RAMessage message = new RAMessage();
        message.setBody(trimmedString);
//        message.setRoomappid("....");
        message.setPushname(RAUtils.decryptObjectForKey(RAConstant.kRAOwnName));
        message.setBubbleType(0);

        for(int i = 0; i < m_arrAttachments.size(); i ++)
        {
            Object media = m_arrAttachments.get(i);
            if(media.getClass() == Bitmap.class)
            {
                Bitmap bmUpload = (Bitmap)media;
                mediaToUpload.add(bmUpload);
                type = RAConstant.RAMessageType.RAMessageTypeOnePhotoPost;
            }
            else if(media.getClass() == RAAttachment.class)
            {
                attachment = (RAAttachment)media;
                if(attachment.getType() == RAConstant.RAAttachmentType.RAAttachmentTypeVideo.ordinal())
                {
                    type = RAConstant.RAMessageType.RAMessageTypeVideoPost;
                    String videoPath = attachment.getMediaURL();
                    thumbnail = RAUtils.createThumbnailForPath(videoPath);

                    /* Do something
                    //
                    //
                    */

                }
                else if(attachment.getType() == RAConstant.RAAttachmentType.RAAttachmentTypeMap.ordinal())
                {
                    type = RAConstant.RAMessageType.RAMessageTypeLocationPost;
                }
                else if(attachment.getType() == RAConstant.RAAttachmentType.RAAttachmentTypeAudio.ordinal())
                {
                    type = RAConstant.RAMessageType.RAMessageTypeAudioPost;
                    String audioUrl = attachment.getMediaURL();

                    /*  Do something
                    //
                    //
                    */
                }

            }
        }

        if(type == RAConstant.RAMessageType.RAMessageTypeOnePhotoPost)
        {
//            params = @{
//            @"type":@"uploadmedia",
//            @"username":[RAUtils decryptObjectForKey:kRAOwnPhoneNumber],
//            @"authUsername": [RAUtils decryptObjectForKey:kRAOwnPhoneNumber],
//            @"authPassword":[RAUtils decryptObjectForKey:kRAOwnPassword] ,
//            @"mediaType":[NSNumber numberWithInt:RAAttachmentTypeImage],
//            @"image":mediaToUpload,
//            @"roomid":(self.room!=nil)?self.room.roomappid:@"",
//            @"chatsession":(self.chatSession!=nil)?[self.chatSession getRemoteSessionId]:@"",
//            @"culture":[[NSLocale preferredLanguages] objectAtIndex:0]

//
//            Post medias to server and get the response
//            NSArray *images = [responseObject objectForKey:@"images"];

            System.out.println("Image Post");
//
            ArrayList<Object> images = new ArrayList<Object>();

            ArrayList<Object> attachments = new ArrayList<Object>();

            for(int i = 0; i < images.size(); i ++) {

                Dictionary imageDict = (Dictionary)images.get(i);

                RAAttachment att = new RAAttachment();
                att.setMediaURL(imageDict.get("path").toString());
                att.setType(RAConstant.RAAttachmentType.RAAttachmentTypeImage.ordinal());
                if(m_chatSession!=null) {
                    //Do something
                    //
                    //
                }
                attachments.add(att);
            }

            if(attachments.size() == 1)
                message.setType(RAConstant.RAMessageType.RAMessageTypeOnePhotoPost.ordinal());

            if(attachments.size() > 1)
                message.setType(RAConstant.RAMessageType.RAMessageTypeMultiplePhotoPost.ordinal());

            m_arrAttachments.clear();
            m_arrThumbnails.clear();
            removedAllThumbnails();

            if(attachments.size() > 0)
            {
                //Will post message
                //
                //
            }
            message = null;
            attachment = null;
        }
        else if((type == RAConstant.RAMessageType.RAMessageTypeVideoPost)||(type == RAConstant.RAMessageType.RAMessageTypeAudioPost))
        {
            //NSString *video = [responseObject objectForKey: @"media"];
            System.out.println("Video/Audio Post");

            String mediaURL = "";
            ArrayList<Object> attachments = new ArrayList<Object>();
            attachment.setMediaURL(mediaURL);
            if(m_chatSession!=null)
            {
                // Do something
                //
                //
            }

            attachments.add(attachment);
            message.setType(type.ordinal());

            m_arrAttachments.clear();
            m_arrThumbnails.clear();
            removedAllThumbnails();

            if(attachments.size() > 0)
            {
                //Will post message
                //
                //
            }
        }
        else if(type == RAConstant.RAMessageType.RAMessageTypeLocationPost)
        {
            System.out.println("Location Post");
            ArrayList<Object> attachments = new ArrayList<Object>();
            attachments.add(attachment);

            message.setType(RAConstant.RAMessageType.RAMessageTypeLocationPost.ordinal());

            removedAllThumbnails();
            m_arrAttachments.clear();
            m_arrThumbnails.clear();

            //Will post message
            //
            //
        }
        else
        {
            System.out.println("Text Post");
            //Will post message
            //
            //
        }

        if(delegate.getClass() == RATimelineActivity.class) {
            ((RATimelineActivity) delegate).hideInputAccessoryView();
            ((RATimelineActivity) delegate).doTestByAdding(message);
        }
        else if(delegate.getClass() == RAChatActivity.class) {

        }
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = delegate.managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }


}
