package co.pincode.interfaces;

import co.pincode.enums.KeyboardButtonEnum;

public interface KeyboardButtonClickedListener {

    /**
     * Receive the click of a button, just after a {@link android.view.View.OnClickListener} has fired.
     * Called before {@link #onRippleAnimationEnd()}.
     * @param keyboardButtonEnum The organized enum of the clicked button
     */

    public void onKeyboardClick(KeyboardButtonEnum keyboardButtonEnum);

    /**
     * Receive the end of a {@link co.pincode.rippleani.RippleView} animation using a
     * {@link co.pincode.rippleani.RippleAnimationListener} to determine the end.
     * Called after {@link #onKeyboardClick(co.pincode.enums.KeyboardButtonEnum)}.
     */

    public void onRippleAnimationEnd();

}
