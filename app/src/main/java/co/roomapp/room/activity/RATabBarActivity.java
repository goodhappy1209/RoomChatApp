package co.roomapp.room.activity;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.ImageView;
import android.graphics.Typeface;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import co.roomapp.room.R;

public class RATabBarActivity extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    static private RATabBarActivity self;
    ViewPager mViewPager;
    TabWidget widget;
    TabHost mTabHost;

    private int[] default_icons = {R.drawable.roomsoff, 					//tab1 icon
            R.drawable.chatsoff,						                    //tab2 icon
            R.drawable.searchoff,					                        //tab3 icon
            R.drawable.settingsoff};						                //tab4 icon


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ramain);

        self = this;

        widget = (TabWidget)findViewById(android.R.id.tabs);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        mTabHost.setup(mLocalActivityManager);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                for(int i=0; i<widget.getTabCount(); i++)
                {
                    View view = widget.getChildTabViewAt(i);
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    imageView.setColorFilter(Color.argb(255,146,146,146));
                }

                View view = mTabHost.getCurrentTabView();
                ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                imageView.setColorFilter(getResources().getColor(R.color.ralightBlue));
                if(tabId.equals("FirstPage"))
                {

                }
                else if(tabId.equals("SecondPage"))
                {

                }
                else if(tabId.equals("ThirdPage"))
                {

                }
                else if(tabId.equals("FourthPage"))
                {

                }
            }
        });

        //mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
        //First Page

        setupTab(
                "FirstPage",
                this.getString(R.string.title_tabbar_section1),
                new Intent().setClass(this, RARoomsActivity.class),
                0);

        //Second Page
        setupTab(
                "SecondPage",
                this.getString(R.string.title_tabbar_section2),
                new Intent().setClass(this, RAChatsActivity.class),
                1);

        //Third Page
        setupTab(
                "ThirdPage",
                this.getString(R.string.title_tabbar_section3),
                new Intent().setClass(this, RAExploreActivity.class),
                2);

        //Four Page
        setupTab(
                "FourthPage",
                this.getString(R.string.title_tabbar_section4),
                new Intent().setClass(this, RASettingsActivity.class),
                3);

    }

    private void setupTab(final String tag, String text, Intent intent, int index) {
        View tabView = createTabView(mTabHost.getContext(), text, index);
        TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabView.setMinimumWidth(300);
        tabSpec.setIndicator(tabView);
        tabSpec.setContent(intent);
        mTabHost.addTab(tabSpec);
    }

    private static View createTabView(final Context context, final String text, final int index) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_tab_bar, null);
        TextView textView = (TextView) view.findViewById(R.id.text);

        Typeface type = Typeface.createFromAsset(context.getAssets(),"HelveticaNeueBold.ttf");
        textView.setTypeface(type);

        textView.setText(text);
        textView.setPadding(0, 0, 0, 5);

        Resources res = self.getResources();
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        imageView.setImageDrawable(res.getDrawable(self.default_icons[index]));

        return view;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_bar, menu);
        return true;
    }

    public void setCurrentTab(int position)
    {
        mTabHost.setCurrentTab(position);
    }

    public static RATabBarActivity getInstance()
    {
        return self;
    }

}
