package co.roomapp.room.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * @author Eric Frohnhoefer
 */
public class RAUnderlineTextView extends TextView {
    private final Paint mPaint = new Paint();
    private int mUnderlineHeight = 0;

    public RAUnderlineTextView(Context context) {
        this(context, null);
    }

    public RAUnderlineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RAUnderlineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources r = getResources();
        mUnderlineHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom + mUnderlineHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the underline the same color as the text
        mPaint.setColor(getTextColors().getDefaultColor());
//        canvas.drawRect(0, getHeight() - mUnderlineHeight, getWidth(), getHeight(), mPaint);
        canvas.drawLine(0, 0, getWidth(), 0, mPaint);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), mPaint);
        canvas.drawLine(0, 0, 0, getHeight(), mPaint);
        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), mPaint);
    }
}
