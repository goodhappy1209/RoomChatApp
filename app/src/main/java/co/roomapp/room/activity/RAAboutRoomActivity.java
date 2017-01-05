package co.roomapp.room.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import co.roomapp.room.R;

/**
 * Created by a on 2/12/15.
 */
public class RAAboutRoomActivity extends Activity implements View.OnClickListener, Animation.AnimationListener{

    private ImageView roomTitleImage;
    private ImageView roomLogoIcon;
    private ImageView taglineImage;
    private TextView creditsLabel;
    private TextView alltheRoomersLabel;
    private TextView thanksLabel;
    private TextView roomIncLabel;
    private TextView versionLabel;
    private ImageButton btnDone;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raabout);

        roomTitleImage = (ImageView)findViewById(R.id.room);
        roomLogoIcon = (ImageView)findViewById(R.id.logo);
        taglineImage = (ImageView)findViewById(R.id.tagline);
        creditsLabel = (TextView)findViewById(R.id.creditsLabel);
        alltheRoomersLabel = (TextView)findViewById(R.id.alltheRoomersLabel);
        thanksLabel = (TextView)findViewById(R.id.thanksLabel);
        roomIncLabel = (TextView)findViewById(R.id.roomIncLabel);
        versionLabel = (TextView)findViewById(R.id.versionLabel);
        btnDone = (ImageButton)findViewById(R.id.btnOk);
        btnDone.setOnClickListener(this);

        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1500);
        anim.setRepeatCount(0);
        roomTitleImage.startAnimation(anim);

        AlphaAnimation anim2 = new AlphaAnimation(0.0f, 0.5f);
        anim2.setDuration(500);
        anim2.setStartOffset(1000);
        anim2.setRepeatCount(0);
        taglineImage.startAnimation(anim2);
        creditsLabel.startAnimation(anim2);
        alltheRoomersLabel.startAnimation(anim2);
        thanksLabel.startAnimation(anim2);

        anim2.setAnimationListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if((ImageButton)v == btnDone)
            finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        taglineImage.setAlpha(1.0f);
        creditsLabel.setAlpha(1.0f);
        alltheRoomersLabel.setAlpha(1.0f);
        thanksLabel.setAlpha(1.0f);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
