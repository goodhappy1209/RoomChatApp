package co.roomapp.room.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.util.ArrayList;
import java.net.URL;
import java.io.InputStream;

import co.roomapp.room.R;
import co.roomapp.room.activity.RARoomsActivity;
import co.roomapp.room.activity.RATimelineActivity;
import co.roomapp.room.model.RAAttachment;
import co.roomapp.room.model.RAMessage;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAUtils;
import co.roomapp.room.widget.RARoundImageView;
import co.customcell.ImageLoader;
import android.widget.GridView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import org.w3c.dom.Text;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
/**
 * Created by manager on 1/8/15.
 */
public class RARoomTimelineAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<RAMessage> data;

    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    private GridView photoGridView;
    private RelativeLayout photoGridLayout;

    private ImageView msgBgBar;
    private ImageView largeThumbnailView;
    private ImageButton btnPlay;
    private RelativeLayout btnPlayLayout;
    private TextView postedMessage;
    private RelativeLayout audioLayout;
    private TextView durationLabel;
    private LinearLayout commentLayout;
    private GoogleMap gMap;
    private float scaledDpi;

    public RARoomTimelineAdapter(Activity a, ArrayList<RAMessage> d) {

        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());

        DisplayMetrics dm = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(dm);
        scaledDpi = dm.density;
    }

    public int getCount() {
        return data.size() + 1;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if(convertView==null) {

            if(position > 0) {

                vi = inflater.inflate(R.layout.room_timeline_custom_cell, null);
                vi.setTag(1);

                RARoundImageView msgUserPhoto = (RARoundImageView)vi.findViewById(R.id.msgPostUserPhoto);
                RARoundImageView cmtUserPhoto = (RARoundImageView)vi.findViewById(R.id.cmtUserPhoto);
                largeThumbnailView = (ImageView)vi.findViewById(R.id.msgThumbnail);
                btnPlayLayout = (RelativeLayout)vi.findViewById(R.id.playLayout);
                btnPlay = (ImageButton)vi.findViewById(R.id.btnPlay);
                audioLayout = (RelativeLayout)vi.findViewById(R.id.audioLayout);
                photoGridLayout = (RelativeLayout)vi.findViewById(R.id.gridLayout);
                photoGridView = (GridView)vi.findViewById(R.id.gridView);
                postedMessage = (TextView)vi.findViewById(R.id.postedMsg);
                commentLayout = (LinearLayout)vi.findViewById(R.id.commentLayout1);
                msgBgBar = (ImageView)vi.findViewById(R.id.msgBgbar);
                durationLabel = (TextView)vi.findViewById(R.id.durationLabel);


                ImageButton btnTimelineSetting = (ImageButton)vi.findViewById(R.id.btnTimelineSetting);
                ImageButton btnTimelineDelete= (ImageButton)vi.findViewById(R.id.btnTimelineDelete);
                btnTimelineSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RATimelineActivity timelineActivity = (RATimelineActivity) activity;
                        timelineActivity.settingTimeline(position);
                    }
                });
                btnTimelineDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RATimelineActivity timelineActivity = (RATimelineActivity) activity;
                        timelineActivity.deleteTimeline(position);
                    }
                });

                new DownloadImageTask(msgUserPhoto).execute("http://images.picturesdepot.com/photo/s/smiles-10753.jpg");
                new DownloadImageTask(cmtUserPhoto).execute("http://images.picturesdepot.com/photo/s/smiles-10753.jpg");

                RAMessage message = data.get(position - 1);
                if(message.getType() == RAConstant.RAMessageType.RAMessageTypeOnePhotoPost.ordinal())
                {
                    List<RAAttachment> attachments = message.getAttachmentsFake();
                    if(attachments.size() == 1)
                    {
                        audioLayout.setVisibility(View.GONE);
                        btnPlayLayout.setVisibility(View.GONE);
                        msgBgBar.setAlpha(0.5f);
                        RAAttachment attachment = attachments.get(0);
                        String imagePath = attachment.getMediaURL();
                        imageLoader.DisplayImage(imagePath, largeThumbnailView);
                        String body = message.getBody();
                        if((body == null)||(body.equals("")))
                            postedMessage.setVisibility(View.GONE);
                        else
                            postedMessage.setText(body);
                    }

                }
                else if(message.getType() == RAConstant.RAMessageType.RAMessageTypeMultiplePhotoPost.ordinal())
                {
                    List<RAAttachment> attachments = message.getAttachmentsFake();
                    if(attachments.size() > 1)
                    {
                        audioLayout.setVisibility(View.GONE);
                        largeThumbnailView.setVisibility(View.GONE);
                        btnPlayLayout.setVisibility(View.GONE);
                        photoGridLayout.setVisibility(View.VISIBLE);
                        photoGridView.setVisibility(View.VISIBLE);
                        String body = message.getBody();
                        if((body == null)||(body.equals("")))
                            postedMessage.setVisibility(View.GONE);
                        else
                            postedMessage.setText(body);
                        PhotoGridViewAdapter m_photoListAdapter = new PhotoGridViewAdapter(activity, attachments);
                        photoGridView.setAdapter(m_photoListAdapter);
                    }
                }
                else if(message.getType() == RAConstant.RAMessageType.RAMessageTypeVideoPost.ordinal())
                {
                    List<RAAttachment> attachments = message.getAttachmentsFake();
                    if(attachments.size() == 1)
                    {
                        audioLayout.setVisibility(View.GONE);
                        largeThumbnailView.setVisibility(View.VISIBLE);
                        btnPlayLayout.setVisibility(View.VISIBLE);
                        msgBgBar.setAlpha(0.5f);
                        photoGridLayout.setVisibility(View.GONE);

                        RAAttachment attachment = attachments.get(0);
                        String videoPath = attachment.getMediaURL();
                        Bitmap thumbnailImage = RAUtils.createThumbnailForPath(videoPath);
                        largeThumbnailView.setImageBitmap(thumbnailImage);
                        String body = message.getBody();
                        if((body == null)||(body.equals("")))
                            postedMessage.setVisibility(View.GONE);
                        else postedMessage.setText(body);

                        //if there isn't any comment yet, hide comment layout
                        commentLayout.setVisibility(View.GONE);
                    }
                }
                else if(message.getType() == RAConstant.RAMessageType.RAMessageTypeAudioPost.ordinal())
                {

                    List<RAAttachment> attachments = message.getAttachmentsFake();

                    if(attachments.size() == 1)
                    {
                        audioLayout.setVisibility(View.VISIBLE);
                        largeThumbnailView.setVisibility(View.GONE);
                        btnPlayLayout.setVisibility(View.GONE);
                        photoGridLayout.setVisibility(View.GONE);

                        RAAttachment attachment = attachments.get(0);
                        String videoPath = attachment.getMediaURL();
                        Double duration = attachment.getMovieDuration();

                        Bitmap thumbnailImage = RAUtils.createThumbnailForPath(videoPath);
                        largeThumbnailView.setImageBitmap(thumbnailImage);

                        durationLabel.setText(duration.toString());

                        String body = message.getBody();
                        if((body == null)||(body.equals("")))
                            postedMessage.setVisibility(View.GONE);
                        else postedMessage.setText(body);

                    }
                }
                else if(message.getType() == RAConstant.RAMessageType.RAMessageTypeLocationPost.ordinal())
                {

                }
                else
                {
                    audioLayout.setVisibility(View.GONE);
                    largeThumbnailView.setVisibility(View.GONE);
                    btnPlayLayout.setVisibility(View.GONE);
                    photoGridLayout.setVisibility(View.GONE);
                    String body = message.getBody();
                    postedMessage.setText(body);
                }

                //if there isn't any comment yet, hide comment layout
                if(!message.getCommentsCount())
                    commentLayout.setVisibility(View.GONE);
                else
                    commentLayout.setVisibility(View.VISIBLE);

            }
            else if(position ==0) {
                vi = inflater.inflate(R.layout.room_timeline_meta_cell, null);
                vi.setTag(0);

                ImageButton btnMediaAll = (ImageButton)vi.findViewById(R.id.btnMediaAll);
                ImageButton btnGuestsAll = (ImageButton)vi.findViewById(R.id.btnGuestsAll);



                btnMediaAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RATimelineActivity timelineActivity = (RATimelineActivity) activity;
                        timelineActivity.allMedias();
                    }
                });
                btnGuestsAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RATimelineActivity timelineActivity = (RATimelineActivity) activity;
                        timelineActivity.allGuests();

                    }
                });
                RARoundImageView userPhotoView = (RARoundImageView)vi.findViewById(R.id.roomOwnerPhoto);
                new DownloadImageTask(userPhotoView).execute("http://images.picturesdepot.com/photo/s/smiles-10753.jpg");



        }
        }

        if(position > 0)
        {
            if(vi.getTag()!=1) {
                vi = inflater.inflate(R.layout.room_timeline_custom_cell, null);
                vi.setTag(1);
            }

            RARoundImageView msgUserPhoto = (RARoundImageView)vi.findViewById(R.id.msgPostUserPhoto);
            RARoundImageView cmtUserPhoto = (RARoundImageView)vi.findViewById(R.id.cmtUserPhoto);
            largeThumbnailView = (ImageView)vi.findViewById(R.id.msgThumbnail);
            btnPlayLayout = (RelativeLayout)vi.findViewById(R.id.playLayout);
            btnPlay = (ImageButton)vi.findViewById(R.id.btnPlay);
            audioLayout = (RelativeLayout)vi.findViewById(R.id.audioLayout);
            photoGridLayout = (RelativeLayout)vi.findViewById(R.id.gridLayout);
            photoGridView = (GridView)vi.findViewById(R.id.gridView);
            postedMessage = (TextView)vi.findViewById(R.id.postedMsg);
            commentLayout = (LinearLayout)vi.findViewById(R.id.commentLayout1);
            msgBgBar = (ImageView)vi.findViewById(R.id.msgBgbar);
            durationLabel = (TextView)vi.findViewById(R.id.durationLabel);

            ImageButton btnTimelineSetting = (ImageButton)vi.findViewById(R.id.btnTimelineSetting);
            ImageButton btnTimelineDelete= (ImageButton)vi.findViewById(R.id.btnTimelineDelete);
            btnTimelineSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RATimelineActivity timelineActivity = (RATimelineActivity) activity;
                    timelineActivity.settingTimeline(position);
                }
            });
            btnTimelineDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RATimelineActivity timelineActivity = (RATimelineActivity) activity;
                    timelineActivity.deleteTimeline(position);
                }
            });

            new DownloadImageTask(msgUserPhoto).execute("http://images.picturesdepot.com/photo/s/smiles-10753.jpg");
            new DownloadImageTask(cmtUserPhoto).execute("http://images.picturesdepot.com/photo/s/smiles-10753.jpg");

            RAMessage message = data.get(position - 1);
            if(message.getType() == RAConstant.RAMessageType.RAMessageTypeOnePhotoPost.ordinal())
            {
                List<RAAttachment> attachments = message.getAttachmentsFake();

                if(attachments.size() == 1)
                {
                    audioLayout.setVisibility(View.GONE);
                    btnPlayLayout.setVisibility(View.GONE);
                    msgBgBar.setAlpha(0.5f);
                    RAAttachment attachment = attachments.get(0);
                    String imagePath = attachment.getMediaURL();
                    imageLoader.DisplayImage(imagePath, largeThumbnailView);

                    String body = message.getBody();
                    if((body == null)||(body.equals("")))
                        postedMessage.setVisibility(View.GONE);
                    else
                        postedMessage.setText(body);

                }

            }
            else if(message.getType() == RAConstant.RAMessageType.RAMessageTypeMultiplePhotoPost.ordinal())
            {
                List<RAAttachment> attachments = message.getAttachmentsFake();

                if(attachments.size() > 1)
                {
                    audioLayout.setVisibility(View.GONE);
                    largeThumbnailView.setVisibility(View.GONE);
                    btnPlayLayout.setVisibility(View.GONE);
                    photoGridLayout.setVisibility(View.VISIBLE);
                    photoGridView.setVisibility(View.VISIBLE);
                    String body = message.getBody();
                    if((body == null)||(body.equals("")))
                        postedMessage.setVisibility(View.GONE);
                    else
                        postedMessage.setText(body);


                    PhotoGridViewAdapter m_photoListAdapter = new PhotoGridViewAdapter(activity, attachments);
                    photoGridView.setAdapter(m_photoListAdapter);

                }
            }
            else if(message.getType() == RAConstant.RAMessageType.RAMessageTypeVideoPost.ordinal())
            {
                List<RAAttachment> attachments = message.getAttachmentsFake();

                if(attachments.size() == 1)
                {
                    audioLayout.setVisibility(View.GONE);
                    largeThumbnailView.setVisibility(View.VISIBLE);
                    btnPlayLayout.setVisibility(View.VISIBLE);
                    msgBgBar.setAlpha(0.5f);
                    photoGridLayout.setVisibility(View.GONE);

                    RAAttachment attachment = attachments.get(0);
                    String videoPath = attachment.getMediaURL();
                    Bitmap thumbnailImage = RAUtils.createThumbnailForPath(videoPath);
                    largeThumbnailView.setImageBitmap(thumbnailImage);

                    String body = message.getBody();
                    if((body == null)||(body.equals("")))
                        postedMessage.setVisibility(View.GONE);
                    else postedMessage.setText(body);

                    //if there isn't any comment yet, hide comment layout
                    commentLayout.setVisibility(View.GONE);
                }
            }
            else if(message.getType() == RAConstant.RAMessageType.RAMessageTypeAudioPost.ordinal())
            {

                List<RAAttachment> attachments = message.getAttachmentsFake();

                if(attachments.size() == 1)
                {
                    audioLayout.setVisibility(View.VISIBLE);
                    largeThumbnailView.setVisibility(View.GONE);
                    btnPlayLayout.setVisibility(View.GONE);
                    photoGridLayout.setVisibility(View.GONE);

                    RAAttachment attachment = attachments.get(0);
                    String videoPath = attachment.getMediaURL();
                    Double duration = attachment.getMovieDuration();

                    Bitmap thumbnailImage = RAUtils.createThumbnailForPath(videoPath);
                    largeThumbnailView.setImageBitmap(thumbnailImage);

                    durationLabel.setText(duration.toString());

                    String body = message.getBody();
                    if((body == null)||(body.equals("")))
                        postedMessage.setVisibility(View.GONE);
                    else postedMessage.setText(body);

                }
            }
            else if(message.getType() == RAConstant.RAMessageType.RAMessageTypeLocationPost.ordinal())
            {

            }
            else
            {
                audioLayout.setVisibility(View.GONE);
                largeThumbnailView.setVisibility(View.GONE);
                btnPlayLayout.setVisibility(View.GONE);
                photoGridLayout.setVisibility(View.GONE);
                String body = message.getBody();
                postedMessage.setText(body);
            }

            //if there isn't any comment yet, hide comment layout
            if(!message.getCommentsCount())
                commentLayout.setVisibility(View.GONE);
            else
                commentLayout.setVisibility(View.VISIBLE);

            return vi;
        }
        else
        {
            if(vi.getTag()==1)
            {
                vi = inflater.inflate(R.layout.room_timeline_meta_cell, null);
                vi.setTag(0);

                ImageButton btnMediaAll = (ImageButton)vi.findViewById(R.id.btnMediaAll);
                ImageButton btnGuestsAll = (ImageButton)vi.findViewById(R.id.btnGuestsAll);

                btnMediaAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RATimelineActivity timelineActivity = (RATimelineActivity) activity;
                        timelineActivity.allMedias();
                    }
                });
                btnGuestsAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RATimelineActivity timelineActivity = (RATimelineActivity) activity;
                        timelineActivity.allGuests();

                    }
                });

                RARoundImageView userPhotoView = (RARoundImageView)vi.findViewById(R.id.roomOwnerPhoto);
                new DownloadImageTask(userPhotoView).execute("http://images.picturesdepot.com/photo/s/smiles-10753.jpg");



                return vi;
            }
            else return vi;
        }

    }

    @SuppressWarnings("unused")
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        private Bitmap newBitmap;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
//            if(pDialog.isShowing()){
//                pDialog.dismiss();
//            }
//            iv_contents_mark.setVisibility(View.GONE);
//            int scrWidth  = ly_webview_content.getWidth();
//            int scrHeight = ly_webview_content.getHeight();

            newBitmap = Bitmap.createScaledBitmap(result, bmImage.getWidth(), bmImage.getHeight(), true);
            Bitmap roundBitmap = RAUtils.getRoundedShape(newBitmap, bmImage.getWidth());
            bmImage.setImageBitmap(roundBitmap);

        }
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

                imageView.setLayoutParams(new GridView.LayoutParams((int)(80*scaledDpi),(int)(80*scaledDpi)));
                imageView.setPadding(10,10,10,10);
                return imageView;
            }

            return null;
        }
    }
}


