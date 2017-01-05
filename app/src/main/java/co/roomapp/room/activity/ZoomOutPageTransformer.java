package co.roomapp.room.activity;

/**
 * Created by manager on 12/17/14.
 */

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.content.Context;

import co.roomapp.room.R;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.0f;
    private static final float MIN_ALPHA = 0.5f;
    private RAPaginationActivity myParent;

    public void setParent(RAPaginationActivity parent)
    {
        myParent= parent;
    }

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();


//        System.out.println(position);
        if (position <= -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
//            view.setAlpha(0);
        } else if (position < 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;

            if(myParent.getScrollingPageIndex()!=2)
            {
                if (position < 0) {
//                    view.setTranslationX(horzMargin - vertMargin / 2);
                    view.setTranslationX(0);

                } else if (position > 0) {
//                    view.setTranslationX(-horzMargin + vertMargin / 2);
                    view.setTranslationX(0);

                }
            }
            else
            {
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                    view.setAlpha(scaleFactor);

                } else if (position > 0) {
                    view.setAlpha(scaleFactor);

                }
            }
            // Fade the page relative to its size.
//            view.setAlpha(MIN_ALPHA +
//                    (scaleFactor - MIN_SCALE) /
//                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
//            view.setAlpha(0);

        }
    }
}
