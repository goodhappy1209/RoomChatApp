package co.roomapp.room.activity;

/**
 * Created by manager on 12/16/14.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ImageView;

import co.roomapp.room.R;


public class RAPaginationActivity extends FragmentActivity{

    private static final int NUM_PAGES = 4;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    public static RelativeLayout bgLayout;
    public static LinearLayout bgLayout1;
    public static LinearLayout bgLayout2;

    private ImageView pgIndicator1;
    private ImageView pgIndicator2;
    private ImageView pgIndicator3;

    private int scrollingPageIdx;
    private boolean flag;

    RAPaginationActivity self;
    private ProgressBar loader;
    private RelativeLayout bottomLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_rapagination);

        pgIndicator1 = (ImageView)findViewById(R.id.pgdot1);
        pgIndicator2 = (ImageView)findViewById(R.id.pgdot2);
        pgIndicator3 = (ImageView)findViewById(R.id.pgdot3);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.myfourpanelpager);
        mPager.setAdapter(mPagerAdapter);

        ZoomOutPageTransformer transformer = new ZoomOutPageTransformer();
        transformer.setParent(this);
        mPager.setPageTransformer(true, transformer);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

                scrollingPageIdx = i;
                if(i==0)
                {
                    bgLayout1.setAlpha(1-v);
                    bgLayout2.setAlpha(v);
                }
                else if(i==1)
                {
                    bgLayout1.setAlpha(v);
                    bgLayout2.setAlpha(1-v);
                }
                else if(i==2)
                {
                    bgLayout1.setAlpha(1-v);
                    bottomLayout.setAlpha(1-v);
                    bgLayout2.setAlpha(0);
                    bgLayout.setAlpha(v);
                }
//                else if(i==3)
//                {
//                    bgLayout1.setAlpha(v);
//                    bgLayout2.setAlpha(v);
//                }
            }

            @Override
            public void onPageSelected(int i) {
                pgIndicator1.setBackgroundResource(R.drawable.pagedot);
                pgIndicator2.setBackgroundResource(R.drawable.pagedot);
                pgIndicator3.setBackgroundResource(R.drawable.pagedot);
                if(i==0)
                {
                    pgIndicator1.setBackgroundResource(R.drawable.dotfill);
                }
                else if(i==1)
                {
                    pgIndicator2.setBackgroundResource(R.drawable.dotfill);
                }
                else if(i==2)
                {
                    pgIndicator3.setBackgroundResource(R.drawable.dotfill);
                }
                else if(i==3)
                {
                    loader.setVisibility(View.VISIBLE);

                    //Display phone number input screen
                    Intent intent = new Intent(self, RARegisterPhoneActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bgLayout = (RelativeLayout)findViewById(R.id.bgLayout);
        bgLayout1 = (LinearLayout)findViewById(R.id.bgLayout1);
        bgLayout2 = (LinearLayout)findViewById(R.id.bgLayout2);
        bottomLayout = (RelativeLayout)findViewById(R.id.bottomBar);

        mPager.setCurrentItem(0);
        pgIndicator1.setBackgroundResource(R.drawable.dotfill);


        loader = (ProgressBar) findViewById(R.id.loader);

        loader.setVisibility(View.GONE);

    }


    public int getScrollingPageIndex()
    {
        return scrollingPageIdx;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new ScreenSlidePageFragment1();
                case 1:
                    return new ScreenSlidePageFragment2();
                case 2:
                    return new ScreenSlidePageFragment3();
                case 3:
                    return new ScreenSlidePageFragment4();
                default:
                    return new ScreenSlidePageFragment1();
            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}
