package co.roomapp.room.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.Observable;
import java.util.Observer;

import co.roomapp.room.R;
import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.manager.RAXMPPManager;
import co.roomapp.room.utils.RAConstant;
import co.roomapp.room.utils.RAObservingService;
import co.roomapp.room.utils.RAUtils;


public class RAStartupActivity extends Activity implements Animation.AnimationListener {

    private ImageView logo;
    private ImageView tag;
    private ImageView room;
    private ProgressBar loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        RoomApplication.getInstance().setLauncher(this);

        setContentView(R.layout.activity_rastartup);

        logo = (ImageView) findViewById(R.id.logo);
        room = (ImageView) findViewById(R.id.room);
        tag = (ImageView) findViewById(R.id.tagline);
        loader = (ProgressBar) findViewById(R.id.loader);

        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1500);
        anim.setRepeatCount(0);
        room.startAnimation(anim);

        AlphaAnimation anim2 = new AlphaAnimation(0.0f, 1.0f);
        anim2.setDuration(500);
        anim2.setStartOffset(1000);
        anim2.setRepeatCount(0);
        tag.startAnimation(anim2);

        anim2.setAnimationListener(this);
        loader.setVisibility(View.GONE);

    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        loader.setVisibility(View.VISIBLE);

        RAXMPPManager.getInstance().setupStream();
        RAXMPPManager.getInstance().connect();

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }



}
