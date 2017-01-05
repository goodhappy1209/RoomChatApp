package co.pincode;

import android.app.Activity;

import co.pincode.interfaces.LifeCycleInterface;


public class PinActivity extends Activity {
    private static LifeCycleInterface mLifeCycleListener;

    @Override
    protected void onResume() {
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityResumed(PinActivity.this);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityPaused(PinActivity.this);
        }
        super.onPause();
    }

    public static void setListener(LifeCycleInterface listener) {
        if (mLifeCycleListener != null) {
            mLifeCycleListener = null;
        }
        mLifeCycleListener = listener;
    }

    public static void clearListeners() {
        mLifeCycleListener = null;
    }

    public static boolean hasListeners() {
        return (mLifeCycleListener != null);
    }
}
