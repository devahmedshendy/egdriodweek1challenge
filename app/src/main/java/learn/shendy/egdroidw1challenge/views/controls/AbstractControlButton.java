package learn.shendy.egdroidw1challenge.views.controls;

import android.animation.AnimatorSet;
import android.view.View;
import android.widget.ImageView;

public abstract class AbstractControlButton {

    public final View mBg;
    public final ImageView mButton;

    AbstractControlButton(View background, ImageView button) {
        mButton = button;
        mBg = background;
    }

    public abstract AnimatorSet showAnimator();
    public abstract AnimatorSet clickedLeaveAnimator();
    public abstract AnimatorSet unClickedLeaveAnimator();
    public abstract void resetAnimationState();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
