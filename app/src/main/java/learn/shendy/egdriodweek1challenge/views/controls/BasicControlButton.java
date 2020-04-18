package learn.shendy.egdriodweek1challenge.views.controls;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import learn.shendy.egdriodweek1challenge.utils.AnimUtils;

import static learn.shendy.egdriodweek1challenge.utils.AnimUtils.*;

public class BasicControlButton extends AbstractControlButton {
    public final TextView mTitle;
    public final View mSelectBg;

    public BasicControlButton(View background, ImageView image, TextView title, View selectBg) {
        super(background, image);
        mTitle = title;
        mSelectBg = selectBg;
    }

    @Override
    public void resetAnimationState() {
        mButton.setScaleX(0f);
        mButton.setScaleY(0f);
        mTitle.setAlpha(0f);
    }

    @Override
    public AnimatorSet showAnimator() {
        Animator controlImage = AnimUtils.createPanelButtonEnterAnimator(mButton);
        Animator controlText = AnimUtils.createPanelTitleEnterAnimator(mTitle);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                controlImage,
                controlText
        );

        return animatorSet;
    }

    @Override
    public AnimatorSet clickedLeaveAnimator() {
        Animator bgAnimator = AnimUtils.createPanelBgLeaveAnimator(mBg);
        bgAnimator.setStartDelay(CLICKED_CONTROL_BG_LEAVE_DELAY);

        Animator buttonAnimator = AnimUtils.createPanelButtonHideAnimator(mButton);
        buttonAnimator.setStartDelay(CLICKED_CONTROL_BUTTON_LEAVE_DELAY);

        Animator selectBgShowAnimator = AnimUtils.createPanelSelectBgShowAnimator(mSelectBg);

        Animator selectBgHideAnimator = AnimUtils.createPanelSelectBgHideAnimator(mSelectBg);
        selectBgHideAnimator.setStartDelay(CLICKED_CONTROL_SELECT_BG_HIDE_DELAY);

        Animator titleAnimator = AnimUtils.createPanelTitleHideAnimator(mTitle);
        titleAnimator.setStartDelay(CLICKED_CONTROL_TITLE_LEAVE_DELAY);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                bgAnimator,
                buttonAnimator,
                selectBgShowAnimator,
                selectBgHideAnimator,
                titleAnimator
        );

        return animatorSet;
    }

    @Override
    public AnimatorSet unClickedLeaveAnimator() {
        Animator controlImageAnimator = AnimUtils.createPanelImageLeaveAnimator(mButton);
        Animator titleAnimator = AnimUtils.createPanelTitleHideAnimator(mTitle);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                controlImageAnimator,
                titleAnimator
        );
        animatorSet.setStartDelay(UnCLICKED_CONTROL_LEAVE_DELAY);

        return animatorSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControlButton that = (ControlButton) o;

        return mTitle.getId() == that.mTitle.getId();
    }

    @Override
    public int hashCode() {
        final int PRIME = 17;
        return mTitle.getId() * PRIME;
    }
}
