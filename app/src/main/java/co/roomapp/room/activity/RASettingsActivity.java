package co.roomapp.room.activity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

import co.pincode.managers.AppLock;
import co.pincode.managers.LockManager;
import co.roomapp.room.R;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import org.apache.log4j.chainsaw.Main;

import co.roomapp.room.widget.RARoundImageView;
import android.widget.Toast;
/**
 * Created by manager on 1/3/15.
 */

public class RASettingsActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private static final int REQUEST_CODE_ENABLE = 11;
    private static final int REQUEST_CODE_DISABLE = 12;

    ImageButton btnAboutRoom;
    ImageButton btnTellFriend;
    ImageButton btnProfile;
    Switch switchNotification;
    Switch switchPasswordLock;
    RARoundImageView networkState;
    protected LockManager mLockManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasettings);

        btnAboutRoom = (ImageButton)findViewById(R.id.settingbtn1);
        btnTellFriend = (ImageButton)findViewById(R.id.settingbtn2);
        btnProfile = (ImageButton)findViewById(R.id.settingbtn3);
        switchNotification = (Switch)findViewById(R.id.settingbtn4);
        switchPasswordLock = (Switch)findViewById(R.id.settingbtn5);
        networkState = (RARoundImageView)findViewById(R.id.settingbtn6);

        mLockManager = LockManager.getInstance();
        if(mLockManager.isAppLockEnabled())
            switchNotification.setChecked(true);

        btnAboutRoom.setOnClickListener(this);
        btnTellFriend.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        switchNotification.setOnCheckedChangeListener(this);
        switchPasswordLock.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if(v.getClass() == ImageButton.class)
        {
            if((ImageButton)v == btnAboutRoom)
            {
                Intent intent = new Intent(this, RAAboutRoomActivity.class);
                startActivity(intent);
            }
            else if((ImageButton)v == btnTellFriend)
            {
                String message = getResources().getString(R.string.check_out_room);
                String link = "http://RoomApp.co/download";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message + " " + link);
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
            else if((ImageButton)v == btnProfile)
            {
                Intent intent = new Intent(this, RAProfileSettingActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(((Switch)buttonView) == switchPasswordLock)
        {
            if(isChecked)
            {
                Intent intent = new Intent(RASettingsActivity.this, RAPinCodeActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                startActivityForResult(intent, REQUEST_CODE_ENABLE);
            }
            else {
                Intent intent = new Intent(RASettingsActivity.this, RAPinCodeActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.DISABLE_PINLOCK);
                startActivityForResult(intent, REQUEST_CODE_DISABLE);
            }
        }
        else if(((Switch)buttonView) == switchNotification)
        {
            if(isChecked)
            {

            }
            else {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_ENABLE:
                Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT).show();
                break;

            case REQUEST_CODE_DISABLE:
                Toast.makeText(this, "PinCode disabled", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
