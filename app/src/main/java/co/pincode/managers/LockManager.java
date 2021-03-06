package co.pincode.managers;

import android.content.Context;

import co.pincode.PinActivity;
import co.pincode.PinFragmentActivity;


public class LockManager<T extends AppLockActivity> {

    /**
     * The static singleton instance
     */
    private volatile static LockManager mInstance;


    private static AppLock mAppLocker;

    /**
     * Used to retrieve the static instance
     */
    public static LockManager getInstance() {
        synchronized (LockManager.class) {
            if (mInstance == null) {
                mInstance = new LockManager<>();
            }
        }
        return mInstance;
    }

    /**
     * You must call that into your custom {@link android.app.Application} to enable the
     * {@link co.pincode.PinActivity}
     */
    public void enableAppLock(Context context, Class<T> activityClass) {
        if (mAppLocker != null) {
            mAppLocker.disable();
        }
        mAppLocker = new AppLockImpl<>(context, activityClass);
        mAppLocker.enable();
    }


    public boolean isAppLockEnabled() {
        return (mAppLocker != null && (PinActivity.hasListeners() || PinFragmentActivity.hasListeners()));
    }

    /**
     * Disables the app lock by calling {@link AppLock#disable()}
     */
    public void disableAppLock() {
        if (mAppLocker != null) {
            mAppLocker.disable();
        }
        mAppLocker = null;
    }

    /**
     * Disables the previous app lock and set a new one
     */
    public void setAppLock(AppLock appLocker) {
        if (mAppLocker != null) {
            mAppLocker.disable();
        }
        mAppLocker = appLocker;
    }

    /**
     * Get the {@link AppLock}. Used for defining custom timeouts etc...
     */
    public AppLock getAppLock() {
        return mAppLocker;
    }
}
