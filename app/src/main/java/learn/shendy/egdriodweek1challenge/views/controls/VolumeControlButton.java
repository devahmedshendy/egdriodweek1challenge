package learn.shendy.egdriodweek1challenge.views.controls;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.widget.ImageView;

import learn.shendy.egdriodweek1challenge.utils.AnimUtils;

import static learn.shendy.egdriodweek1challenge.utils.AnimUtils.UnCLICKED_CONTROL_LEAVE_DELAY;

public class VolumeControlButton extends AbstractControlButton {

    public VolumeControlButton(ImageView image) {
        super(null, image);
    }

    @Override
    public void resetAnimationState() {
        mButton.setScaleX(0f);
        mButton.setScaleY(0f);
    }

    @Override
    public AnimatorSet showAnimator() {
        Animator animator = AnimUtils.createPanelButtonEnterAnimator(mButton);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(animator);

        return animatorSet;
    }

    @Override
    public AnimatorSet clickedLeaveAnimator() {
        return null;
    }

    @Override
    public AnimatorSet unClickedLeaveAnimator() {
        Animator controlImageAnimator = AnimUtils.createPanelImageLeaveAnimator(mButton);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(controlImageAnimator);
        animatorSet.setStartDelay(UnCLICKED_CONTROL_LEAVE_DELAY);

        return animatorSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VolumeControlButton that = (VolumeControlButton) o;

        return mButton.getId() == that.mButton.getId();
    }

    @Override
    public int hashCode() {
        final int PRIME = 17;
        return mButton.getId() * PRIME;
    }
}
