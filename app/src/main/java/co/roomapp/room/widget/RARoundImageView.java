package co.roomapp.room.widget;

import co.roomapp.room.utils.RAUtils;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.RectF;
import android.graphics.Path;
/**
 * Created by manager on 1/6/15.
 */
public class RARoundImageView extends ImageView{

    private int RADIUS = 10;
    private RectF mRect;
    private Path mClip;

    public RARoundImageView(Context context) {
        super(context);
        init();
    }

    public RARoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RARoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap fullSizeBitmap = drawable.getBitmap();

        int scaledWidth = getMeasuredWidth();
        int scaledHeight = getMeasuredHeight();

        Bitmap mScaledBitmap;

        if(fullSizeBitmap != null)
        {
            if (scaledWidth == fullSizeBitmap.getWidth() && scaledHeight == fullSizeBitmap.getHeight()) {
                mScaledBitmap = fullSizeBitmap;
            } else {
                mScaledBitmap = Bitmap.createScaledBitmap(fullSizeBitmap, scaledWidth, scaledHeight, true /* filter */);
            }

            Bitmap roundBitmap = RAUtils.getRoundedCornerBitmap( getContext(), mScaledBitmap, scaledWidth/2 , scaledWidth, scaledHeight , false, false, false, false);
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        }

    }

    public void setRadius(int radius){
        this.RADIUS = radius;
    }

    private void init(){
        mRect = new RectF();
        mClip = new Path();
    }

}
