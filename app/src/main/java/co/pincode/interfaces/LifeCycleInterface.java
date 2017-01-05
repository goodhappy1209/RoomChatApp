package co.pincode.interfaces;

import android.app.Activity;


public interface LifeCycleInterface {

    /**
     * Called in {@link android.app.Activity#onResume()}
     */
    public void onActivityResumed(Activity activity);

    /**
     * Called in {@link android.app.Activity#onPause()}
     */
    public void onActivityPaused(Activity activity);
}
