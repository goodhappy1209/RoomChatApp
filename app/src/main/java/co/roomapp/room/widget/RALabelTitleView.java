package co.roomapp.room.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



public class RALabelTitleView extends TextView{


    public RALabelTitleView(Context context) {
        super(context);
        setup(null);
    }

    public RALabelTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public RALabelTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(attrs);
    }

    private void setup(AttributeSet attrs){

        Typeface font = Typeface.createFromAsset(this.getContext().getAssets(), "HelveticaNeue.ttf");
        this.setTypeface(font);
        this.setTextSize(25.0f);
        this.setTextColor(Color.parseColor("#FFFFFF"));
    }

}
