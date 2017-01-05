package co.roomapp.room.widget;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import co.roomapp.room.model.RAAttachment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;
/**
 * Created by a on 2/8/15.
 */
public class RAMessageEditText extends EditText {

    private int mTextSize;

    public RAMessageEditText(Context context) {
        super(context);
        setup(null);
    }

    public RAMessageEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public RAMessageEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(attrs);
    }

    private void setup(AttributeSet attrs){

        Typeface font = Typeface.createFromAsset(this.getContext().getAssets(), "HelveticaNeue.ttf");
        this.setTypeface(font);
        this.setTextSize(15.0f);
        this.setTextColor(Color.parseColor("#000000"));

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        mTextSize = (int)getTextSize();
    }

    public void addAttachment(ArrayList<Object> thumbnailsList)
    {
        removeBitmaps();
        for(int i = 0; i < thumbnailsList.size(); i ++)
        {
            Bitmap bitmap = (Bitmap)thumbnailsList.get(i);
            insertBitmap(bitmap);
        }
    }

    public void insertBitmap(final Bitmap bitmap)
    {
        Resources resources = getContext().getResources();
        final Drawable drawable = new BitmapDrawable(resources, bitmap);
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                drawable.setBounds(0, 0, mTextSize*3, mTextSize*3);
                return drawable;
            }
        };

        String img = "<img src=\"" + drawable.toString() + "\" />";
        Spanned spanned = Html.fromHtml(img, imageGetter, null);
        int start= this.getSelectionStart();
        int end = this.getSelectionEnd();
        this.getText().replace(Math.min(start, end), Math.max(start, end), spanned, 0, spanned.length());
    }

    public void removeBitmaps()
    {

        this.setText("");
    }
}
