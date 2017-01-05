package co.roomapp.room.adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import co.customcell.ImageLoader;
import co.roomapp.room.R;
import co.roomapp.room.activity.RATimelineActivity;
import co.roomapp.room.model.RAAttachment;
import co.roomapp.room.model.RAMember;
import co.roomapp.room.model.RAMessage;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAUtils;
import co.roomapp.room.widget.RAGridView;
import co.roomapp.room.widget.RARoundImageView;

import co.stickylistheaders.StickyListHeadersAdapter;
import android.widget.SectionIndexer;

/**
 * Created by luoqi on 2/20/2015.
 */
public class RAMediaGalleryListAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer{

    private Activity activity;
    private ArrayList<RAAttachment> data;

    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    private RAGridView photoGridView;
    private RelativeLayout photoGridLayout;

    private float scaledDpi;
    private int[] mSectionIndexes;
    private int mSectionCount = 0;
    private String[] mSectionTitles;

    int m_screenWidth;
    int m_screenHeight;

    public RAMediaGalleryListAdapter(Activity a, ArrayList<RAAttachment> d) {

        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());

        DisplayMetrics dm = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(dm);
        scaledDpi = dm.density;
        m_screenWidth = dm.widthPixels;
        m_screenHeight = dm.heightPixels;

        mSectionCount = calcSectionsCount();
        mSectionIndexes = getSectionIndexes();
    }

    @Override
    public int getCount() {

        return mSectionCount;
    }

    protected int calcSectionsCount() {

        int sectionCount = 0;
        if(data.size() > 0) {
            RAAttachment attachment = data.get(0);
            RAMessage lastMessage = attachment.getMessage();
            Date lastAttachmentDate = lastMessage.getMessagedate();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String lastAttachmentDateStr = dateFormat.format(lastAttachmentDate);
            sectionCount = 1;
            for(int i = 1; i < data.size(); i ++)
            {
                RAAttachment temp = data.get(i);
                RAMessage message = temp.getMessage();
                Date date = message.getMessagedate();
                String attachmentDateStr = dateFormat.format(date);
                if(!lastAttachmentDateStr.equals(attachmentDateStr)) {
//                    sectionIndexes[sectionCount] = i;
                    lastAttachmentDateStr = attachmentDateStr;
                    sectionCount ++;

                }
            }
        }
        return sectionCount;
    }

    protected int[] getSectionIndexes() {

        int sectionCount = 0;

        int[] _mSectionIndexes = new int[mSectionCount];
        mSectionTitles = new String[mSectionCount];
        if(data.size() > 0) {
            _mSectionIndexes[0] = 0;
            RAAttachment attachment = data.get(0);
            RAMessage lastMessage = attachment.getMessage();
            Date lastAttachmentDate = lastMessage.getMessagedate();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String lastAttachmentDateStr = dateFormat.format(lastAttachmentDate);
            mSectionTitles[0] = lastAttachmentDateStr;
            sectionCount = 1;
            for(int i = 1; i < data.size(); i ++)
            {
                RAAttachment temp = data.get(i);
                RAMessage message = temp.getMessage();
                Date date = message.getMessagedate();
                String attachmentDateStr = dateFormat.format(date);
                if(!lastAttachmentDateStr.equals(attachmentDateStr)) {
                    _mSectionIndexes[sectionCount] = i;
                    mSectionTitles[sectionCount] = attachmentDateStr;
                    lastAttachmentDateStr = attachmentDateStr;
                    sectionCount ++;
                }
            }
        }
        return _mSectionIndexes;
    }

    @Override
    public Object getItem(int position) {

        if(position < mSectionIndexes.length) {
            ArrayList<RAAttachment> itemsInSection = new ArrayList<RAAttachment>();
            int from = mSectionIndexes[position];
            int to = data.size();
            if(position + 1 < mSectionIndexes.length)
                to = mSectionIndexes[position + 1];
            for(int i = from; i < to; i ++) {
                RAAttachment attachment = data.get(i);
                itemsInSection.add(attachment);
            }
            return itemsInSection;
        }
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.media_gallery_list_item, parent, false);
        RAGridView gridView = (RAGridView) convertView.findViewById(R.id.mediaGridView);
        gridView.setNumColumns(3);
        ArrayList<RAAttachment> attchs = (ArrayList)getItem(position);
        PhotoGridViewAdapter m_photoListAdapter = new PhotoGridViewAdapter(activity, attchs);
        gridView.setAdapter(m_photoListAdapter);
        int rowCount = (attchs.size()%3==0)?attchs.size()/3:attchs.size()/3+1;
        convertView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(95*rowCount*scaledDpi)));
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        int headerId = getSectionForPosition(position);
        View vi = inflater.inflate(R.layout.media_gallery_list_section_header, parent, false);
        TextView sectionHeader = (TextView) vi.findViewById(R.id.text1);
        String title = mSectionTitles[headerId];
        sectionHeader.setText(title);
        return vi;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {

        if(position < mSectionCount)
            return position;
        else
            return mSectionCount - 1;
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndexes.length == 0) {
            return 0;
        }
        if (section >= mSectionIndexes.length) {
            section = mSectionIndexes.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndexes[section];
    }

    @Override
    public int getSectionForPosition(int position) {

        return (int)getHeaderId(position);
    }

    @Override
    public Object[] getSections() {
        return mSectionTitles;
    }

    public void clear() {

    }

    private class PhotoGridViewAdapter extends BaseAdapter
    {

        private ArrayList<RAAttachment> attchments;
        private Context mContext;

        public PhotoGridViewAdapter(Context c) {
            mContext = c;
        }

        public PhotoGridViewAdapter(Context c, ArrayList<RAAttachment> _attchments) {
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RAAttachment attachment = attchments.get(position);

            if(attachment!=null) {
                if(attachment.getType() == RAConstant.RAAttachmentType.RAAttachmentTypeImage.ordinal())
                {
                    String imagePath = attachment.getMediaURL();

                    ImageView imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    System.out.println("Image Path: " + imagePath);
                    if((imagePath != null)&&(!imagePath.equals("")))
                        imageLoader.DisplayImage(imagePath, imageView);

                    imageView.setLayoutParams(new GridView.LayoutParams((int)(80*scaledDpi),(int)(80*scaledDpi)));
                    imageView.setPadding((int)(10 * scaledDpi), (int)(10 * scaledDpi), (int)(10 * scaledDpi), (int)(10 * scaledDpi));
                    return imageView;
                }
            }

            return null;
        }

    }
}
