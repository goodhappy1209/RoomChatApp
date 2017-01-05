package co.roomapp.room.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import co.roomapp.room.R;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.roomapp.room.adapter.RAMediaGalleryListAdapter;
import co.roomapp.room.adapter.RAMembersListAdapter;
import co.roomapp.room.model.RAAttachment;
import co.roomapp.room.model.RAMember;
import co.roomapp.room.model.RAMessage;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAUtils;
import co.stickylistheaders.StickyListHeadersListView;

/**
 * Created by luoqi on 2/20/2015.
 */
public class RAMediaGalleryActivity extends Activity implements View.OnClickListener,
        StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    private ImageButton btnBack;
    private StickyListHeadersListView mGalleryListView;
    private RAMediaGalleryListAdapter mAdapter;

    private boolean fadeHeader = true;

    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_ramediagallery);
        btnBack = (ImageButton)findViewById(R.id.btnBack);

        btnBack.setOnClickListener(this);


        mGalleryListView = (StickyListHeadersListView)findViewById(R.id.mediaGalleryListView);

        ArrayList<RAAttachment> attachmentList = new ArrayList<RAAttachment>();
        for(int i = 0; i < 30; i ++) {
            RAAttachment attachment = new RAAttachment();
            RAMessage message = new RAMessage();
            message.setId(new Long(1));
            message.setRoomJID("12955");
            message.setType(RAConstant.RAMessageType.RAMessageTypeOnePhotoPost.ordinal());
            message.setAttachments(RAUtils.getTemplate(2));
            message.setCommentsCount(true);
            message.setBody("What a kitty!!! Looking at heaven?");
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault());
            try {
                if(i<16) {
                    Date msgDate = dateFormat.parse("2/23/2015 16:20");
                    message.setMessagedate(msgDate);
                }
                else if(i<22) {
                    Date msgDate = dateFormat.parse("2/24/2015 16:20");
                    message.setMessagedate(msgDate);
                }
                else{
                    Date msgDate = dateFormat.parse("3/1/2015 16:20");
                    message.setMessagedate(msgDate);
                }
            }
            catch(ParseException e) {
                e.printStackTrace();
            }
            attachment.setMessage(message);
            attachment.setType(RAConstant.RAAttachmentType.RAAttachmentTypeImage.ordinal());
            attachment.setMediaURL("http://ts4.mm.bing.net/th?id=HN.607993839974744283&pid=1.7");
            attachmentList.add(attachment);
        }

        mAdapter = new RAMediaGalleryListAdapter(this, attachmentList);

        mGalleryListView.setOnHeaderClickListener(this);
        mGalleryListView.setOnStickyHeaderChangedListener(this);
        mGalleryListView.setOnStickyHeaderOffsetChangedListener(this);
//        mGalleryListView.addHeaderView(getLayoutInflater().inflate(R.layout.member_list_header, null));
//        mGalleryListView.addFooterView(getLayoutInflater().inflate(R.layout.member_list_footer, null));
//        stickyList.setEmptyView(findViewById(R.id.empty));
        mGalleryListView.setDrawingListUnderStickyHeader(true);
        mGalleryListView.setAreHeadersSticky(true);
        mGalleryListView.setAdapter(mAdapter);
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
        }
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

}
