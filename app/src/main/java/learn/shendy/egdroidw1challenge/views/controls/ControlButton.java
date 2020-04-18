package learn.shendy.egdroidw1challenge.views.controls;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import learn.shendy.egdroidw1challenge.utils.AnimUtils;

import static learn.shendy.egdroidw1challenge.utils.AnimUtils.*;

public class ControlButton extends AbstractControlButton {
    public final TextView mTitle;
    public final TextView mSubtitle;
    public final View mSelectBg;

    public ControlButton(View background, ImageView image, TextView title, TextView subtitle, View selectBg) {
        super(background, image);
        mTitle = title;
        mSubtitle = subtitle;
        mSelectBg = selectBg;
    }

    @Override
    public void resetAnimationState() {
        mButton.setScaleX(0f);
        mButton.setScaleY(0f);
        mTitle.setAlpha(0f);
        mSubtitle.setAlpha(0f);
    }

    @Override
    public AnimatorSet showAnimator() {
        Animator controlImage = AnimUtils.createPanelButtonEnterAnimator(mButton);
        Animator controlTitle = AnimUtils.createPanelTitleEnterAnimator(mTitle);
        Animator controlSubtitle = AnimUtils.createPanelTitleEnterAnimator(mSubtitle);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                controlImage,
                controlTitle,
                controlSubtitle
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

        Animator subtitleAnimator = AnimUtils.createPanelTitleHideAnimator(mSubtitle);
        subtitleAnimator.setStartDelay(CLICKED_CONTROL_TITLE_LEAVE_DELAY);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                bgAnimator,
                buttonAnimator,
                selectBgShowAnimator,
                selectBgHideAnimator,
                titleAnimator,
                subtitleAnimator
        );

        return animatorSet;
    }

    @Override
    public AnimatorSet unClickedLeaveAnimator() {
        Animator controlImageAnimator = AnimUtils.createPanelImageLeaveAnimator(mButton);
        Animator titleAnimator = AnimUtils.createPanelTitleHideAnimator(mTitle);
        Animator subtitleAnimator = AnimUtils.createPanelTitleHideAnimator(mSubtitle);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(controlImageAnimator, titleAnimator, subtitleAnimator);
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
        return mTitle.getId() * mSubtitle.getId() * PRIME;
    }
}
